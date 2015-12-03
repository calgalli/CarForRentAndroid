package com.example.jed.carforrent;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;





/**
 * Created by cake on 8/20/15 AD.
 */


public class updateTransaction extends AsyncTask<String, Void, JSONObject> {
    private ClientConnectionManager clientConnectionManager;
    //private HttpContext context;
    private HttpParams params;
    private DefaultHttpClient client;
    private JSONObject jj = null;
    public AsyncResponse delegate=null;



    @Override
    protected JSONObject doInBackground(String[] params1) {
        // do above Server call here
        client = new DefaultHttpClient(clientConnectionManager, params);
        HttpPost get = new HttpPost("https://128.199.97.22:1880/updateRentTransaction");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);

        String email = "";
        String subject = "";
        String body = "";


        String customerID = "";
        String customer_Name = "";
        String company_ID = "";
        String company = "";
        String type = "";
        String vehicle_type = "";
        String from = "";
        String to = "";
        String note = "";
        String price = "";
        if(params1.length == 10) {
            customerID = params1[0];
            customer_Name = params1[1];
            company_ID = params1[2];
            company = params1[3];
            type = params1[4];
            vehicle_type = params1[5];
            from = params1[6];
            to = params1[7];
            note = params1[8];
            price = params1[9];

        }

        //Date,
        //        Time, Customer ID ,Customer Name, Company ID, Company Name, Type (Car, Transfer, Boat), Vehicle Type (Vios, PP Ferry, Speedboat),
        //        From, To, Pick Up, Note, Amount, Status (0=Pending, 1=Approved,

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");

        String formattedTime = df2.format(c.getTime());







        nameValuePairs.add(new BasicNameValuePair("date", formattedDate));
        nameValuePairs.add(new BasicNameValuePair("time", formattedTime));
        nameValuePairs.add(new BasicNameValuePair("customerID", customerID));
        nameValuePairs.add(new BasicNameValuePair("customer_Name", customer_Name));
        nameValuePairs.add(new BasicNameValuePair("company_ID", company_ID));
        nameValuePairs.add(new BasicNameValuePair("company", company));
        nameValuePairs.add(new BasicNameValuePair("type", type));
        nameValuePairs.add(new BasicNameValuePair("vehicle_type", vehicle_type));
        nameValuePairs.add(new BasicNameValuePair("from", from));
        nameValuePairs.add(new BasicNameValuePair("to", to));
        nameValuePairs.add(new BasicNameValuePair("note", note));
        nameValuePairs.add(new BasicNameValuePair("price", price));
        nameValuePairs.add(new BasicNameValuePair("status", "0"));

        try {
            get.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        HttpResponse response = null;
        try {
            response = client.execute(get);
            Log.i("Http", response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String json = null;

        try {
            json = EntityUtils.toString(response.getEntity());
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // String retSrc = EntityUtils.toString(entity);
                // parsing JSON
                // JSONArray result = new JSONArray(json); //Convert String to JSON Object
                // jj = result;
                return jj;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jj;
    }

    @Override
    protected void onPostExecute(JSONObject obj) {
        //process message
        if(obj != null)
        {
            //do something
        }



    }

    @Override
    protected void onPreExecute() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();

        // http scheme
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        // https scheme
        schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 1880));

        params = new BasicHttpParams();
        params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 1);
        params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(1));
        params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "utf8");
        clientConnectionManager = new ThreadSafeClientConnManager(params, schemeRegistry);


    }


}

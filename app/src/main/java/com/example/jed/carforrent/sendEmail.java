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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cake on 8/20/15 AD.
 */


public class sendEmail extends AsyncTask<String, Void, JSONObject> {
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
        HttpPost get = new HttpPost("https://128.199.97.22:1880/sendEmail");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

        String email = "";
        String subject = "";
        String body = "";
        if(params1.length == 3) {
            email = params1[0];
            subject = params1[1];
            body = params1[2];
        }



        nameValuePairs.add(new BasicNameValuePair("subject", subject));
        nameValuePairs.add(new BasicNameValuePair("email", email));
        nameValuePairs.add(new BasicNameValuePair("body", body));
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

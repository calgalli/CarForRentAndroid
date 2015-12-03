package com.example.jed.carforrent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class profileActivity extends Activity implements AsyncResponse{

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    private ArrayList<profileDetail> allCompanies = new ArrayList<profileDetail>();


    public class transactionDetail {
        public String companyName = "";
        public String type = "";
        public String price = "";
        public String status = "";

    }

    private ArrayList<transactionDetail> transactions = new ArrayList<transactionDetail>();

    private ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        listView = (ListView) findViewById(R.id.profilelistView);



    }

    @Override
    protected void onResume() {
        super.onResume();

        final globalVar gv = ((globalVar)getApplicationContext());

        String [] loginParameters = {gv.custommerID};

        Log.i("PROFILE", gv.custommerID);

       // String [] loginParameters = {emailLogin,passwordLogin};
        sendRequest loginRequest = new sendRequest();
        loginRequest.delegate = profileActivity.this;
        loginRequest.execute(loginParameters);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onBackClick(View view) {
        Intent i_carent = new Intent(getApplicationContext(), SelectForRent.class);
        startActivity(i_carent);

        /*Intent thisIntent = getIntent();


        int k = thisIntent.getIntExtra("whereFrom", 0);
        if(k == 0) {
            Intent i_carent = new Intent(getApplicationContext(), CarRent.class);
            startActivity(i_carent);
        } else if (k == 1){
            Intent i_carent = new Intent(getApplicationContext(), BoatRent.class);
            startActivity(i_carent);
        } else if (k == 2){
            Intent i_carent = new Intent(getApplicationContext(), BoatTranfer.class);
            startActivity(i_carent);
        }*/
    }

    public void onLogoutClick(View view) {

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("email", "");
        editor.putString("password", "");
        editor.putString("hasLogin", "no");
        editor.commit();
        Intent i_carent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i_carent);
    }

    @Override
    public void processFinish(JSONObject output) {

    }

    @Override
    public void processFinishID(JSONObject output) {

    }

    @Override
    public void loginFinish(JSONObject output) {

    }

    @Override
    public void processFinishString(String output, String idd) {

    }

    @Override
    public void processFinishArray(JSONArray output) {

        transactions.clear();
        for(int i = 0; i < output.length();i++){
            try {
                JSONObject x = output.getJSONObject(i);
                transactionDetail td = new transactionDetail();
                td.price = x.getString("price");
                td.companyName = x.getString("company");
                td.type = x.getString("type");
                td.status = x.getString("status");
                transactions.add(i, td);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        reloadTable();

    }




    public class sendRequest extends AsyncTask<String, Void, JSONArray> {
        private ClientConnectionManager clientConnectionManager;
        //private HttpContext context;
        private HttpParams params;
        private DefaultHttpClient client;
        private JSONArray jj = null;
        public AsyncResponse delegate=null;
        @Override
        protected JSONArray doInBackground(String[] params1) {
            // do above Server call here
            final globalVar gv = ((globalVar)getApplicationContext());

            client = new DefaultHttpClient(clientConnectionManager, params);
            HttpPost get = new HttpPost("https://"+gv.mainHost+":1880/getRentTransactions");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            String custommerID = "";

            if(params1.length == 1) {
                custommerID = params1[0];

            }

            Log.i("PROFILE", "https://"+gv.mainHost+":1880/getRentTransactions");

            nameValuePairs.add(new BasicNameValuePair("customerID", custommerID));
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
                    JSONArray result = new JSONArray(json); //Convert String to JSON Object
                    if(result.length() > 0) {
                        jj = result;
                        // Log.i("Http", response.getStatusLine().toString());
                        Log.i("Http xxxxxx ", jj.toString());
                        return jj;
                    } else {
                        Log.i("Http", "Login fail !!!!!!!");
                        Log.i("Http", response.getStatusLine().toString());
                        Log.i("Http", json.toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jj;
        }

        @Override
        protected void onPostExecute(JSONArray obj) {

            delegate.processFinishArray(obj);


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

    public void reloadTable(){
        //customeCell adapter = new customeCell(this, -1, arrayOfDrivers);

        for(int i = 0; i < transactions.size(); i++){

            transactionDetail x = transactions.get(i);


            profileDetail xx = new profileDetail(x.companyName, "Kind : " + x.type + " Price : " + x.price, x.status);

            allCompanies.add(i, xx);

        }


        profileAdapter  arrayAdapter = new profileAdapter(this, -1, allCompanies);



        listView.setAdapter(arrayAdapter);
    }


}

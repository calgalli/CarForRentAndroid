package com.example.jed.carforrent;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements AsyncResponse{

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String DEFAULT_PREF = "NIL" ;


    SharedPreferences sharedpreferences;
    String emailP = "";
    String passwordP = "";

    Boolean isDownload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();

        String emailLogin = sharedpreferences.getString("email", DEFAULT_PREF);
        String passwordLogin = sharedpreferences.getString("password", DEFAULT_PREF);
        String hasLogin = sharedpreferences.getString("hasLogin", DEFAULT_PREF);


        if(hasLogin.equals("yes")){

            Log.i("Http", "Async task !!!!!!!!!!!!!!!!!!");
            Log.i("YYYY", emailLogin + " " + passwordLogin);

            String [] loginParameters = {emailLogin,passwordLogin};
            sendRequest loginRequest = new sendRequest();
            loginRequest.delegate = MainActivity.this;
            loginRequest.execute(loginParameters);
        }

        Button bt_login = (Button)findViewById(R.id.button_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText text = (EditText)findViewById(R.id.editTextEmail);
                String email = text.getText().toString();
                EditText passwordText = (EditText)findViewById(R.id.editTextPass);
                String password = passwordText.getText().toString();


                emailP = email;
                passwordP = password;
                Log.d("Http", "Async task !!!!!!!!!!!!!!!!!!");
                String [] loginParameters = {email,password};
                sendRequest loginRequest = new sendRequest();
                loginRequest.delegate = MainActivity.this;
                loginRequest.execute(loginParameters);

               // Intent i_select = new Intent(getApplicationContext(), SelectForRent.class);
               // startActivity(i_select);
            }
        });

        Button bt_regis = (Button)findViewById(R.id.button_register);
        bt_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i_regis = new Intent(getApplicationContext(), Register.class);
                startActivity(i_regis);
            }
        });
    }

    @Override
    public void processFinish(JSONObject output) {

    }

    @Override
    public void processFinishID(JSONObject output) {

    }

    @Override
    public void loginFinish(JSONObject output) {
        String hasLogin = sharedpreferences.getString("hasLogin", DEFAULT_PREF);

        if(hasLogin.equals(DEFAULT_PREF) || hasLogin.equals("no")) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("email", emailP);
            editor.putString("password", passwordP);
            editor.putString("hasLogin", "yes");
            editor.commit();
        }





        Log.i("Http", output.toString());

        final globalVar gv = ((globalVar) getApplicationContext());

        try {

            gv.custommerID = (String) output.get("passportID");
            gv.custommerName = (String) output.get("name");
            gv.imageID = (String) output.get("id");

            String url = "http://" + gv.mainHost + gv.pathToImage + gv.imageID + ".png";
            new DownloadImageTask(gv, isDownload).execute(url);




        } catch (JSONException e) {
            e.printStackTrace();
        }


/*
        Intent i_select = new Intent(getApplicationContext(), SelectForRent.class);
        startActivity(i_select);*/
    }

    @Override
    public void processFinishString(String output, String idd) {

    }

    @Override
    public void processFinishArray(JSONArray output) {

    }


    public class sendRequest extends AsyncTask<String, Void, JSONObject> {
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
            HttpPost get = new HttpPost("https://128.199.97.22:1880/customerLogin");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            String userName = "";
            String password = "";
            if(params1.length == 2) {
                userName = params1[0];
                password = params1[1];
            }

            Log.i("XXXX", userName + " " + password);

            nameValuePairs.add(new BasicNameValuePair("email", userName));
            nameValuePairs.add(new BasicNameValuePair("password", password));
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
                    jj = result.getJSONObject(0);
                    if(result.length() == 1) {
                        jj = result.getJSONObject(0);
                        // Log.i("Http", response.getStatusLine().toString());
                        Log.i("Http", jj.toString());
                    } else {
                        Log.i("Http", "Login fail !!!!!!!");
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
        protected void onPostExecute(JSONObject obj) {
            //process message
            if(obj != null)
            {
                //do something
            }


            delegate.loginFinish(obj);
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


            /*CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            //set the user credentials for our site "example.com"
            credentialsProvider.setCredentials(new AuthScope("example.com", AuthScope.ANY_PORT),
                    new UsernamePasswordCredentials("UserNameHere", "UserPasswordHere"));

            context = new BasicHttpContext();
            context.setAttribute("http.auth.credentials-provider", credentialsProvider);*/
        }

    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        globalVar bmImage;
        Boolean isDownload;

        public DownloadImageTask(globalVar bmImage, Boolean isDownload) {
            this.bmImage = bmImage;
            this.isDownload = isDownload;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setCustommerImage(result);

            Intent i_select = new Intent(getApplicationContext(), SelectForRent.class);
            startActivity(i_select);

            Intent intent = new Intent("Download_done");

            sendBroadcast(intent);
            isDownload = true;
        }
    }



}

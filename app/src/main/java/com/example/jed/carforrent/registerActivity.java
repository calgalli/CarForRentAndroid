package com.example.jed.carforrent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class registerActivity extends ActionBarActivity implements AsyncResponse{
    private static final int SELECT_PHOTO = 100;

    String encoded = "";

    EditText name;
    EditText email;
    EditText password;
    EditText creditNumber;
    EditText expireMounth;
    EditText expireYear;
    EditText CVC;
    EditText phonenumber;
    EditText nationality;
    EditText passportID;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button bt_regis = (Button)findViewById(R.id.bt_cancel);
        bt_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i_regis = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i_regis);
            }
        });

        TextView cImage = (TextView)findViewById(R.id.choose_image_from);
        cImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        name = (EditText) findViewById(R.id.edit_name);
        email = (EditText) findViewById(R.id.edit_email);
        password = (EditText) findViewById(R.id.edit_pass);
        creditNumber = (EditText) findViewById(R.id.edit_cradit);
        expireMounth = (EditText) findViewById(R.id.MM);
        expireYear = (EditText) findViewById(R.id.YYYY);
        CVC = (EditText) findViewById(R.id.editCrc);
        phonenumber = (EditText) findViewById(R.id.edit_mobile);
        nationality = (EditText) findViewById(R.id.edit_nation);
        passportID = (EditText) findViewById(R.id.edit_passID);

        Button bt_reg = (Button)findViewById(R.id.bt_regis);
        bt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String[] ll = new String[13];
                ll[0] = passportID.getText().toString();
                ll[1] = name.getText().toString();
                ll[2] = email.getText().toString();
                ll[3] = password.getText().toString();
                ll[4] = creditNumber.getText().toString();
                ll[5] = expireMounth.getText().toString();
                ll[6] = expireYear.getText().toString();
                ll[7] = CVC.getText().toString();
                ll[8] = phonenumber.getText().toString();
                ll[9] = nationality.getText().toString();
                ll[10] = passportID.getText().toString();
                ll[11] = "12345";
                ll[12] = "Phuket";

                Boolean isComplete;
                if(ll[9] == "Thai" || ll[9] == "Thailand"){
                    isComplete = ll[0].isEmpty()  && ll[1].isEmpty() ;
                    isComplete = isComplete  && ll[2].isEmpty() && ll[3].isEmpty() && ll[8].isEmpty() && ll[9].isEmpty() && ll[10].isEmpty();


                } else {



                }

                if( !ll[0].isEmpty() && !ll[1].isEmpty()  && !ll[2].isEmpty() && !ll[3].isEmpty() && !ll[4].isEmpty() && !ll[5].isEmpty() && !ll[6].isEmpty() && !ll[7].isEmpty() && !ll[8].isEmpty() && !ll[9].isEmpty() && !ll[10].isEmpty()
                        ) {
                    sendRegisterRequest loginRequest = new sendRegisterRequest();
                    loginRequest.delegate = registerActivity.this;
                    loginRequest.execute(ll);

                    String filename = passportID.getText().toString() + ".png";
                    String[] lll = {filename, encoded};
                    sendImageRequest imRequest = new sendImageRequest();
                    imRequest.delegate = registerActivity.this;
                    imRequest.execute(lll);

                } else {
                    new AlertDialog.Builder(registerActivity.this)
                            .setCancelable(false)
                            .setTitle("Incomplete data")
                            .setMessage("Please fill all the fields.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            }).show();


                }

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
        Intent i_regis = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i_regis);


    }

    @Override
    public void processFinishString(String output, String idd) {

    }

    @Override
    public void processFinishArray(JSONArray output) {

    }

    public class sendRegisterRequest extends AsyncTask<String, Void, JSONObject> {
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
            HttpPost get = new HttpPost("https://128.199.97.22:1880/uploadCustommerData");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(13);


            String id = "";
            String name1 = "";
            String email1 = "";
            String password1 = "";
            String creditNumber1 = "";
            String expireMounth1 = "";
            String expireYear1 = "";
            String CVC1 = "";
            String phoneNumber = "";
            String nationality1 = "";
            String passportID1 = "";
            String postalCode = "";
            String city = "";

            if(params1.length == 13) {
                id = params1[0];
                name1 = params1[1];
                email1 = params1[2];
                password1 = params1[3];
                creditNumber1 = params1[4];
                expireMounth1 = params1[5];
                expireYear1 = params1[6];
                CVC1 = params1[7];
                phoneNumber = params1[8];
                nationality1 = params1[9];
                passportID1 = params1[10];
                postalCode = params1[11];
                city = params1[12];

            }



            nameValuePairs.add(new BasicNameValuePair("id", id));
            nameValuePairs.add(new BasicNameValuePair("name", name1));
            nameValuePairs.add(new BasicNameValuePair("email", email1));
            nameValuePairs.add(new BasicNameValuePair("password", password1));
            nameValuePairs.add(new BasicNameValuePair("creditNumber", creditNumber1));
            nameValuePairs.add(new BasicNameValuePair("expireMounth", expireMounth1));
            nameValuePairs.add(new BasicNameValuePair("expireYear", expireYear1));
            nameValuePairs.add(new BasicNameValuePair("CVC", CVC1));
            nameValuePairs.add(new BasicNameValuePair("phoneNumber", phoneNumber));
            nameValuePairs.add(new BasicNameValuePair("nationality", nationality1));
            nameValuePairs.add(new BasicNameValuePair("passportID", passportID1));
            nameValuePairs.add(new BasicNameValuePair("postalCode", postalCode));
            nameValuePairs.add(new BasicNameValuePair("city", city));

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
                    if(result.length() == 1) {
                        jj = result.getJSONObject(0);
                        // Log.i("Http", response.getStatusLine().toString());
                        // Log.i("Http", jj.toString());
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



    public class sendImageRequest extends AsyncTask<String, Void, JSONObject> {
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
            HttpPost get = new HttpPost("https://128.199.97.22:1880/uploadImageCustommer");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);


            String filename = "";
            String data = "";


            if(params1.length == 2) {
                filename = params1[0];
                data = params1[1];


            }



            nameValuePairs.add(new BasicNameValuePair("filename", filename));
            nameValuePairs.add(new BasicNameValuePair("data", data));


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
                    if(result.length() == 1) {
                        jj = result.getJSONObject(0);
                        // Log.i("Http", response.getStatusLine().toString());
                        // Log.i("Http", jj.toString());
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


            //delegate.loginFinish(obj);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap yourSelectedImage = decodeUri(selectedImage);

                        ImageView x = (ImageView) findViewById(R.id.avatarImageView);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        yourSelectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream .toByteArray();
                        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                        x.setImageBitmap(yourSelectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }
    }


    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 80;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_register, menu);
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
}

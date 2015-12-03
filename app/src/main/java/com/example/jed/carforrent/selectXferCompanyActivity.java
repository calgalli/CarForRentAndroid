package com.example.jed.carforrent;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class selectXferCompanyActivity extends Activity implements AsyncResponse {



    public class companyDetail {
        String pname;
        String image;
        String priceOnwway;
        String priceRoundTrip;

        public companyDetail(String pname, String image, String priceOnwway, String priceRoundTrip) {
            this.pname = pname;
            this.image = image;
            this.priceOnwway = priceOnwway;
            this.priceRoundTrip = priceRoundTrip;
        }


    }

    TextView mTitleTextView;



    private ArrayList<companyDetail> allDetails = new ArrayList<companyDetail>();
    private ArrayList<itemDetail> allCompanies = new ArrayList<itemDetail>();

    String selectedDestination = "";
    String selectedCompany = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_xfer_company);

        ListView listView = (ListView) findViewById(R.id.xferCompanyListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                final int selectedPos = position;

                selectedCompany = allCompanies.get(selectedPos).pname;

                Log.i("SELECTED_COMPANY", selectedCompany);

                Intent intent = new Intent(getApplicationContext(), BoatTranfer.class);
                intent.putExtra("selectedDestination", selectedDestination);
                intent.putExtra("selectedCompany", selectedCompany);
                startActivity(intent);
            }
        });


        // Set Custom Actionbar
        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);

        final globalVar gv = ((globalVar) getApplicationContext());

        ImageButton imageButton_map = (ImageButton) mCustomView.findViewById(R.id.imageButtonMap);


        imageButton_map.setImageBitmap(getRoundedShape(gv.custommerImage));

        imageButton_map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i_carent = new Intent(getApplicationContext(), profileActivity.class);
                i_carent.putExtra("whereFrom", 0);
                startActivity(i_carent);

            }
        });

        Button back = (Button) mCustomView.findViewById(R.id.actionbar_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i_carent = new Intent(getApplicationContext(), selectDestinationActivity.class);
                startActivity(i_carent);

            }
        });



        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        allCompanies.clear();
        allDetails.clear();
        Intent x = getIntent();
        selectedDestination = x.getStringExtra("selectedDestination");

        mTitleTextView.setText(selectedDestination);

        String[] loginParameters = {selectedDestination};
        sendRequest emailRequest = new sendRequest();
        emailRequest.delegate = selectXferCompanyActivity.this;
        emailRequest.execute(loginParameters);

        Log.i("IN_COMPANY", selectedDestination);
        Log.i("SELECTED_DEST", selectedDestination);
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
        for(int i = 0;i < output.length();i++){
            try {
                JSONObject x = output.getJSONObject(i);

                String name = x.getString("name");
                String tname = name.replaceAll("\\s", "");
                String logo = x.getString("logo");
                String priceOw = "";
                String priceRt = "";

                JSONArray carModels = x.getJSONArray("destination");

                for(int j = 0; j < carModels.length(); j++){
                    JSONObject kk = carModels.getJSONObject(j);
                    if(kk.getString("name").equals(selectedDestination)){
                        priceOw = kk.getString("oneWayRate");
                        priceRt = kk.getString("roundTripRate");
                    }
                }

                itemDetail xmm = new itemDetail(name, "Round trip : " + priceRt + " Baht, Oneway : " + priceOw + " Baht"  ,"/" + tname + "/" + logo);
                companyDetail xmp = new companyDetail(name,logo,priceOw,priceRt);
                allCompanies.add(i,xmm);
                allDetails.add(i,xmp);

                Log.i("COMPANY", x.getString("name"));
                Log.i("EMAIL", x.getString("logo"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        final detailItemAdapter  arrayAdapter = new detailItemAdapter(this, -1, allCompanies);
        ListView listView = (ListView) findViewById(R.id.xferCompanyListView);
        listView.setAdapter(arrayAdapter);

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
            client = new DefaultHttpClient(clientConnectionManager, params);
            HttpPost get = new HttpPost("https://128.199.97.22:1880/getTransferBoatCopanies");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            String carModel = "";

            if(params1.length == 1) {
                carModel = params1[0];

            }
            nameValuePairs.add(new BasicNameValuePair("destination.name", carModel));

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
                    jj = result;
                    return jj;
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
            //process message
            if(obj != null)
            {
                //do something
            }

            Log.i("JSON", obj.toString());

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

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 46, getResources().getDisplayMetrics());
        int targetWidth = px;
        int targetHeight = px;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);

        Path path = new Path();

        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);

        Bitmap sourceBitmap = scaleBitmapImage;

        canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
                sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
                targetHeight), null);

        return targetBitmap;
    }

}

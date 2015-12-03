package com.example.jed.carforrent;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by jed on 8/15/15 AD.
 */
public class BoatRent extends Activity implements AsyncResponse, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    JSONArray companies = new JSONArray();

    private ArrayList<boatModel> boatModels = new ArrayList<boatModel>();
    private ArrayList<companyModel> allCompanies = new ArrayList<companyModel>();
    private int fromWhere = 0;
    private int fromOrTo = 0;

    private String selectedCompany = "";
    private String selectedBoatModel = "";
    private String selectedEmail = "";
    private long selectedBoatPriceF = 0;
    private long selectedBoatPriceH = 0;


    private long selectedBoatPrice = 0;
    private String totalPrice = "";

    private Boolean isFullday = true;

    private final int FROM_TIME_DIALOG_ID = 0;
    private final int FROM_DATE_DIALOG_ID = 1;
    private final int TO_TIME_DIALOG_ID = 2;
    private final int TO_DATE_DIALOG_ID = 3;


    private String fromDate = "'";
    private String toDate = "";

    private String fromTime = "";
    private String toTime = "";

    int fromMg = 1;
    int fromDg = 1;
    int fromYg =2002;
    int toMg = 1;
    int toDg = 1;
    int toYg = 2002;

    public class companyModel{
        String name;
        String email;
        String image;

        public companyModel(String name, String email, String image) {
            this.name = name;
            this.email = email;
            this.image = image;
        }
    }

    public class boatModel {
        String model;
        String priceH;
        String priceF;
        String image;

        public boatModel(String model, String priceH, String priceF, String image) {
            this.model = model;
            this.priceH = priceH;
            this.priceF = priceF;
            this.image = image;
        }


    }

    private Button fromDateButton;
    private Button toDateButton;
    private Button fromTimeButton;
    private Button toTimeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boat_rent_activity);

        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("Boat Rent");

        final globalVar gv = ((globalVar) getApplicationContext());

        ImageButton imageButton_map = (ImageButton) mCustomView.findViewById(R.id.imageButtonMap);
        imageButton_map.setImageBitmap(getRoundedShape(gv.custommerImage));

        imageButton_map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i_carent = new Intent(getApplicationContext(), profileActivity.class);
                i_carent.putExtra("whereFrom", 1);
                startActivity(i_carent);

            }
        });


        Button boat_back = (Button) mCustomView.findViewById(R.id.actionbar_back);
        boat_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i_boatrent = new Intent(getApplicationContext(), selectBoatCompanyActivity.class);
                i_boatrent.putExtra("selectedBoat", selectedBoatModel);
                startActivity(i_boatrent);

            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);




/*

        Button companyButton = (Button)findViewById(R.id.bt_boat_company);
        companyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                allCompanies.clear();
                String [] loginParameters = {};
                sendRequest loginRequest = new sendRequest();
                loginRequest.delegate = BoatRent.this;
                loginRequest.execute(loginParameters);
            }
        });

        Button modelButton = (Button)findViewById(R.id.bt_boat_type);
        modelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fromWhere = 1;
                boatModels.clear();
                for (int i = 0; i < companies.length(); i++) {
                    JSONObject x = null;
                    try {
                        x = (JSONObject) companies.get(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        if (x.getString("name").equals(selectedCompany)) {
                            JSONArray y = x.getJSONArray("boatType");
                            selectedEmail = x.getString("email");

                            Log.i("Car models : ", y.toString());
                            for (int j = 0; j < y.length(); j++) {
                                JSONObject car = y.getJSONObject(j);
                                boatModel k = new boatModel(car.getString("Model"), car.getString("PriceH"), car.getString("PriceF"), "xx");
                                boatModels.add(j, k);
                            }

                            creatDialog(boatModels, BoatRent.this, fromWhere);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
*/
        final Button onwayButton = (Button)findViewById(R.id.bt_boat_halfday);
        final Button roundTripButton = (Button)findViewById(R.id.bt_boat_fullday);

        onwayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFullday = false;
                RelativeLayout returnLayout = (RelativeLayout) findViewById(R.id.half_day_view);
                returnLayout.setVisibility(View.GONE);

                onwayButton.setBackgroundResource(R.drawable.mybutton);
                roundTripButton.setBackgroundResource(R.drawable.buttoncurve);
                onwayButton.setTextColor(-1);
                roundTripButton.setTextColor(Color.parseColor("#ff153172"));

                TextView x = (TextView) findViewById(R.id.boat_total_price);
                x.setText(String.valueOf(selectedBoatPriceH ));
                totalPrice = String.valueOf(selectedBoatPriceH);
                //Intent i_boatrent = new Intent(getApplicationContext(), BoatRent.class);
                //startActivity(i_boatrent);
            }
        });



        roundTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFullday = true;
                RelativeLayout returnLayout = (RelativeLayout) findViewById(R.id.half_day_view);
                returnLayout.setVisibility(View.VISIBLE);

                onwayButton.setBackgroundResource(R.drawable.buttoncurve);
                roundTripButton.setBackgroundResource(R.drawable.mybutton);
                onwayButton.setTextColor(Color.parseColor("#ff153172"));
                roundTripButton.setTextColor(-1);

                long df = dayDiff(fromMg, fromDg, fromYg, toMg, toDg, toYg);
                TextView x2 = (TextView) findViewById(R.id.boat_total_price);
                x2.setText("0.0");

                if (df > 0 && df < 30) {
                    TextView x = (TextView) findViewById(R.id.boat_total_price);
                    x.setText(String.valueOf(df * selectedBoatPriceF));
                    totalPrice = String.valueOf(df * selectedBoatPriceF);
                } else {

                    if (fromOrTo == 1) {
                        new AlertDialog.Builder(BoatRent.this)
                                .setCancelable(false)
                                .setTitle("Wrong date")
                                .setMessage("Please check your date.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();

                    }
                }
            }

                // finish();

        });

        Button bookButton = (Button)findViewById(R.id.bt_boat_book);
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selectedCompany.isEmpty() && !selectedBoatModel.isEmpty() && !totalPrice.isEmpty()) {

                    String ee = composeEmail();

                    String subject = "Car booking from app";
                    String emm = selectedEmail;
                    Log.d("EMAIL=", emm);
                    String[] loginParameters = {emm, subject, ee};
                    sendEmail emailRequest = new sendEmail();
                    emailRequest.execute(loginParameters);

                    TextView pNote = (TextView) findViewById((R.id.editBoat_note));

                    final globalVar gv = ((globalVar) getApplicationContext());

                    String[] params1 = new String[10];

                    params1[0] = gv.custommerID;
                    params1[1] = gv.custommerName;
                    params1[2] = "N/A";
                    params1[3] = selectedCompany;
                    params1[4] = "boat";
                    params1[5] = selectedBoatModel;
                    params1[6] = "N/A";
                    params1[7] = "N/A";
                    params1[8] = pNote.getText().toString();
                    params1[9] = totalPrice;

                    updateTransaction upt = new updateTransaction();
                    upt.execute(params1);

                    Log.i("EMAIL", ee);

                    new AlertDialog.Builder(BoatRent.this)
                            .setCancelable(false)
                            .setTitle("Reservation complete")
                            .setMessage("Wait for confirmation.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                } else {
                    new AlertDialog.Builder(BoatRent.this)
                            .setCancelable(false)
                            .setTitle("Incomplete information")
                            .setMessage("Please check your information.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            }
        });
/*
        Button btt = (Button)findViewById(R.id.bt_boat_tranfer);
        btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i_boatrent = new Intent(getApplicationContext(), BoatTranfer.class);
                startActivity(i_boatrent);

            }
        });*/

        fromDateButton = (Button) findViewById(R.id.bt_boat_rentdate);
        toDateButton =(Button) findViewById(R.id.bt_boat_returndate);

        fromTimeButton = (Button) findViewById(R.id.bt_boat_renttime);
        toTimeButton = (Button) findViewById(R.id.bt_boat_returntime);

        //RelativeLayout tt = (RelativeLayout) findViewById(R.id.rentOrXfer);
        //tt.setVisibility(View.GONE);

        TextView tg = (TextView) findViewById(R.id.textBoat_guest);
        EditText dt = (EditText) findViewById(R.id.editBoat_guest);
        tg.setVisibility(View.GONE);
        dt.setVisibility(View.GONE);
    }


    @Override
    protected void onResume() {
        super.onResume();


        Intent x = getIntent();
        selectedBoatModel = x.getStringExtra("selectedBoat");
        selectedCompany = x.getStringExtra("selectedCompany");
        selectedBoatPriceH = Long.parseLong(x.getStringExtra("selectedPriceH"));
        selectedBoatPriceF = Long.parseLong(x.getStringExtra("selectedPriceF"));

        TextView x1 = (TextView) findViewById(R.id.textBoat_secect_company);
        x1.setText(selectedCompany);


        TextView x2 = (TextView) findViewById(R.id.textBoat_secect_boat);
        x2.setText(selectedBoatModel);

        String [] loginParameters = {};
        sendRequest loginRequest = new sendRequest();
        loginRequest.delegate = BoatRent.this;
        loginRequest.execute(loginParameters);

        TextView x3 = (TextView) findViewById(R.id.boat_total_price);
        x3.setText("-");

    }


    public void onBoatReturnDateClick(View v){
        fromOrTo = 1;
        showDialog(TO_DATE_DIALOG_ID);
    }

    public void onBoatReturnTimeClick(View v){
        fromOrTo = 1;
        showDialog(TO_TIME_DIALOG_ID);
    }

    public void onBoatRentDateClick(View v){
        fromOrTo = 0;
        showDialog(FROM_DATE_DIALOG_ID);
    }

    public void onBoatRentTimeClick(View v){
        fromOrTo = 0;
        showDialog(FROM_TIME_DIALOG_ID);
    }

    public void onBoatRentNoteClick(View v){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString().trim();
                TextView pNote = (TextView) findViewById((R.id.editBoat_note));

                pNote.setText(value);
                // Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
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
        companies = output;
        for(int i = 0;i < companies.length();i++){
            try {
                JSONObject x = companies.getJSONObject(i);
                companyModel xmm = new companyModel( x.getString("name"), x.getString("email"), x.getString("logo"));
                allCompanies.add(i,xmm);
                //companyNames.add(i, x.getString("name"));
                //emailS.put(x.getString("name"), x.getString("email"));

                Log.i("COMPANY", x.getString("name"));
                Log.i("EMAIL", x.getString("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        fromWhere = 0;
      //  creatDialog(allCompanies, BoatRent.this, fromWhere);

    }

    @Override
    protected Dialog onCreateDialog(int id) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        switch (id) {
            case FROM_TIME_DIALOG_ID:
                // fromOrTo = 0;
                return new TimePickerDialog(this,this, hour, minute, false);

            case FROM_DATE_DIALOG_ID:
                //  fromOrTo = 0;
                return new DatePickerDialog(this,this,year, month, day);
            case TO_TIME_DIALOG_ID:
                //  fromOrTo = 1;
                return new TimePickerDialog(this,this, hour, minute, false);
            case TO_DATE_DIALOG_ID:
                //  fromOrTo = 1;
                return new DatePickerDialog(this,this,year, month, day);
        }
        return null;
    }




    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        switch (fromOrTo){
            case 0:
                if(i1 < 10) {
                    fromTime = String.valueOf(i) + ":0" + String.valueOf(i1);
                } else {
                    fromTime = String.valueOf(i) + ":" + String.valueOf(i1);
                }
                fromTimeButton.setText(fromTime);
                break;
            case 1:
                if(i1 < 10) {
                    toTime = String.valueOf(i) + ":0" + String.valueOf(i1);
                } else {
                    toTime = String.valueOf(i) + ":" + String.valueOf(i1);
                }

                toTimeButton.setText(toTime);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        switch (fromOrTo){
            case 0:
                fromMg = month;
                fromDg = day;
                fromYg =year;


                fromDate = String.valueOf(day) + "-" + String.valueOf(month) + "-" +  String.valueOf(year);

                Log.i("From date", fromDate);
                fromDateButton.setText(fromDate);
                break;
            case 1:
                toMg = month;
                toDg = day;
                toYg = year;
                toDate = String.valueOf(day) + "-" + String.valueOf(month) + "-" +  String.valueOf(year);

                Log.i("To date", toDate);

                toDateButton.setText(toDate);
        }
        long df = dayDiff(fromMg, fromDg, fromYg, toMg, toDg, toYg);
        Log.i("Day diff = ", String.valueOf(df));

        if(isFullday) {
            if (df > 0 && df < 30) {
                TextView x = (TextView) findViewById(R.id.boat_total_price);
                x.setText(String.valueOf(df * selectedBoatPriceF));
                totalPrice = String.valueOf(df * selectedBoatPriceF);
            } else {
                if(fromOrTo == 1) {
                    new AlertDialog.Builder(this)
                            .setCancelable(false)
                            .setTitle("Wrong date")
                            .setMessage("Please check your date.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            }
        } else {
            TextView x = (TextView) findViewById(R.id.boat_total_price);
            x.setText(String.valueOf(selectedBoatPriceH));
            totalPrice = String.valueOf(selectedBoatPriceH);
        }
        // Do something with the date chosen by the user
    }

    public long dayDiff(int fromM, int fromD, int fromY, int toM, int toD, int toY) {
        Calendar thatDay = Calendar.getInstance();
        thatDay.set(Calendar.DAY_OF_MONTH, fromD);
        thatDay.set(Calendar.MONTH, fromM); // 0-11 so 1 less
        thatDay.set(Calendar.YEAR, fromY);

        Calendar thisDay = Calendar.getInstance();
        thisDay.set(Calendar.DAY_OF_MONTH, toD);
        thisDay.set(Calendar.MONTH, toM); // 0-11 so 1 less
        thisDay.set(Calendar.YEAR, toY);

        long diff = thisDay.getTimeInMillis() - thatDay.getTimeInMillis(); //result in millis
        long days = diff / (24 * 60 * 60 * 1000);
        return days;
    }


    public String composeEmail(){
        String x = "";
        TextView pNote = (TextView) findViewById((R.id.editBoat_note));
        x = "Boat type " + selectedBoatModel + "\n";
        x = x + "Rent date: " + fromDate + " Time : " + fromTime + "\n";
        if(isFullday) {
            x = x + "Return date: " + toDate + " Time : " + toTime + "\n";
        } else {
            x = x + "Remark : Half day" + "\n";
        }
        x = x + "Total price : " + totalPrice + " Baht" + "\n";
        if(pNote.length() > 0){
            x = x + "Note : " + pNote.getText();
        }

        return x;
    }


    public void creatDialog(ArrayList<?> data, final Context context, final int fromWhere) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        ArrayList<Item> m_parts = new ArrayList<Item>();

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.select_dialog_singlechoice);

        if(fromWhere == 0) {
            builderSingle.setTitle("Select Company");
            for (int i = 0; i < data.size(); i++) {

                companyModel cm = (companyModel) data.get(i);

                Item a = new Item(cm.name, cm.name.replaceAll("\\s","") + "/" + cm.image);

                m_parts.add(a);
            }
        } else if(fromWhere == 1){
            builderSingle.setTitle("Select Boat");
            for (int i = 0; i < data.size(); i++) {
                boatModel x = (boatModel) data.get(i);
                String a;
                if(isFullday){
                    a = x.model + " Price : " + x.priceF + "bath";
                } else {
                    a = x.model + " Price : " + x.priceH + "bath";
                }


                arrayAdapter.add(a);
            }
        }

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        if(fromWhere == 0) {
            final ItemAdapter  arrayAdapter2 = new ItemAdapter(context, android.R.layout.select_dialog_singlechoice, m_parts);

            builderSingle.setAdapter(arrayAdapter2,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Item strName = arrayAdapter2.getItem(which);

                                TextView x = (TextView) findViewById(R.id.textBoat_secect_company);
                                x.setText(strName.pname);
                                selectedCompany = strName.pname;



                        }
                    });
            builderSingle.show();

        } else if(fromWhere == 1) {
            builderSingle.setAdapter(arrayAdapter,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String strName = arrayAdapter.getItem(which);

                                boatModel k = boatModels.get(which);

                                TextView x = (TextView) findViewById(R.id.textBoat_secect_boat);
                                x.setText(strName);
                                selectedBoatModel = strName;
                                if (isFullday) {
                                    selectedBoatPrice = Long.parseLong(k.priceF);
                                } else {
                                    selectedBoatPrice = Long.parseLong(k.priceH);
                                }

                            }


                    });
            builderSingle.show();

        }

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
            HttpPost get = new HttpPost("https://128.199.97.22:1880/getBoatCompanies");



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

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
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
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
public class BoatTranfer extends Activity implements AsyncResponse, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{


    private final int FROM_TIME_DIALOG_ID = 0;
    private final int FROM_DATE_DIALOG_ID = 1;
    private final int TO_TIME_DIALOG_ID = 2;
    private final int TO_DATE_DIALOG_ID = 3;


    JSONArray companies = new JSONArray();

    JSONArray deses = new JSONArray();




    private ArrayList<companyModel> allCompanies = new ArrayList<companyModel>();

    private ArrayList<String>  destinationOfSelecledCompany = new ArrayList<String>();

    private ArrayList<String>  tDepart = new ArrayList<String>();

    private ArrayList<String>  tReturn = new ArrayList<String>();


    private int fromWhere = 0;
    private int fromOrTo = 0;

    private String selectedCompany = "";
    private String selectedDestination = "";
   // private String selectedEmail = "";


    private String selectedDepartTime = "";
    private String selectedReturnTime = "";

    private String priceOneWay = "";
    private String priceRoundTrip = "";

    private String fromDate = "'";
    private String toDate = "";

    private long selectedCarPrice = 0;
    private String totalPrice = "";
    private String selectedEmail = "";

    private EditText numGuest;

    int isOneWay = 0;

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

    public class carModel {
        String model;
        String priceH;
        String priceF;
        String image;

        public carModel(String model, String priceH, String priceF, String image) {
            this.model = model;
            this.priceH = priceH;
            this.priceF = priceF;
            this.image = image;
        }


    }

    int fromMg = 1;
    int fromDg = 1;
    int fromYg =2002;
    int toMg = 1;
    int toDg = 1;
    int toYg = 2002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boat_tranfer_activity);

        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("Transfer");

        numGuest = (EditText) findViewById(R.id.editText_guests);
        numGuest.addTextChangedListener(new TextWatcher() {

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                            }

                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count,
                                                                          int after) {
                                                // TODO Auto-generated method stub
                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {
                                                TextView uuu = (TextView) findViewById(R.id.totalPriceXfer);
                                                int myNum = 0;
                                                try {
                                                    myNum = Integer.parseInt(numGuest.getText().toString());
                                                } catch (NumberFormatException nfe) {
                                                    // Handle parse error.
                                                }
                                                int llc = 0;
                                                if (isOneWay == 0) {
                                                    try {
                                                        llc = Integer.parseInt(priceRoundTrip);
                                                    } catch (NumberFormatException nfe) {
                                                        // Handle parse error.
                                                    }

                                                } else {
                                                    try {
                                                        llc = Integer.parseInt(priceOneWay);
                                                    } catch (NumberFormatException nfe) {
                                                        // Handle parse error.
                                                    }

                                                }
                                                int ttp = myNum * llc;

                                                uuu.setText(String.valueOf(ttp));
                                                totalPrice = String.valueOf(ttp);

                                            }
                                        }

        );

        final globalVar gv = ((globalVar) getApplicationContext());


        ImageButton imageButton_map = (ImageButton) mCustomView.findViewById(R.id.imageButtonMap);
        imageButton_map.setImageBitmap(getRoundedShape(gv.custommerImage));

        imageButton_map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i_carent = new Intent(getApplicationContext(), profileActivity.class);
                i_carent.putExtra("whereFrom", 2);
                startActivity(i_carent);

            }
        });


        Button back = (Button) mCustomView.findViewById(R.id.actionbar_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i_carent = new Intent(getApplicationContext(), selectXferCompanyActivity.class);
                i_carent.putExtra("selectedDestination", selectedDestination);
                startActivity(i_carent);

            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
/*
        Button b_rent = (Button)findViewById(R.id.bt_rent);
        b_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i_boatrent = new Intent(getApplicationContext(), BoatRent.class);
                startActivity(i_boatrent);
            }
        });

        Button b_back = (Button)findViewById(R.id.bt_back);
        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i_boatrent = new Intent(getApplicationContext(), SelectForRent.class);
                startActivity(i_boatrent);
            }
        });*/

        final Button onwayButton = (Button)findViewById(R.id.bt_oneway);
        final Button roundTripButton = (Button)findViewById(R.id.bt_round_trip);

        onwayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isOneWay = 1;
                TextView uuu = (TextView) findViewById(R.id.totalPriceXfer);

                int myNum = 0;
                try {
                    myNum = Integer.parseInt(numGuest.getText().toString());
                } catch (NumberFormatException nfe) {
                    // Handle parse error.
                }
                RelativeLayout returnLayout = (RelativeLayout) findViewById(R.id.return_view);
                returnLayout.setVisibility(View.GONE);
                onwayButton.setBackgroundResource(R.drawable.mybutton);
                roundTripButton.setBackgroundResource(R.drawable.buttoncurve);
                onwayButton.setTextColor(-1);
                roundTripButton.setTextColor(Color.parseColor("#ff153172"));
                int llc = Integer.parseInt(priceOneWay.replaceAll("\\s+", ""));
                totalPrice = String.valueOf(myNum*llc);
                uuu.setText(String.valueOf(totalPrice));

                //Intent i_boatrent = new Intent(getApplicationContext(), BoatRent.class);
                //startActivity(i_boatrent);
            }
        });



        roundTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView uuu = (TextView) findViewById(R.id.totalPriceXfer);

                isOneWay = 0;
                int myNum = 0;
                try {
                    myNum = Integer.parseInt(numGuest.getText().toString().replaceAll("\\s+", ""));
                } catch (NumberFormatException nfe) {
                    // Handle parse error.
                }
                RelativeLayout returnLayout = (RelativeLayout) findViewById(R.id.return_view);
                returnLayout.setVisibility(View.VISIBLE);
                onwayButton.setBackgroundResource(R.drawable.buttoncurve);
                roundTripButton.setBackgroundResource(R.drawable.mybutton);
                onwayButton.setTextColor(Color.parseColor("#ff153172"));
                roundTripButton.setTextColor(-1);
                int llc = Integer.parseInt(priceRoundTrip);
                totalPrice = String.valueOf(myNum*llc);
                uuu.setText(String.valueOf(totalPrice));


               // finish();
            }
        });

        final Button bookButton = (Button)findViewById(R.id.bt_book);
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ng = numGuest.getText().toString();
                if (!selectedCompany.isEmpty() && !selectedDestination.isEmpty() && !ng.isEmpty()) {

                    String ee = composeEmail();
                    String subject = "Car booking from app";
                    String emm = selectedEmail;
                    Log.d("EMAIL=", emm);
                    String[] loginParameters = {emm, subject, ee};
                    sendEmail emailRequest = new sendEmail();
                    emailRequest.execute(loginParameters);

                    TextView pNote = (TextView) findViewById((R.id.editText_note));

                    final globalVar gv = ((globalVar) getApplicationContext());

                    String[] params1 = new String[10];

                    params1[0] = gv.custommerID;
                    params1[1] = gv.custommerName;
                    params1[2] = "N/A";
                    params1[3] = selectedCompany;
                    params1[4] = "Trasnsfer";
                    params1[5] = selectedDestination + " Ferry";
                    params1[6] = "Phuket";
                    params1[7] = selectedDestination;
                    params1[8] = pNote.getText().toString();
                    params1[9] = totalPrice;

                    updateTransaction upt = new updateTransaction();
                    upt.execute(params1);


                    Log.i("EMAIL", ee);

                    new AlertDialog.Builder(BoatTranfer.this)
                            .setCancelable(false)
                            .setTitle("Reservation complete")
                            .setMessage("Wait for confirmation.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i_boatTranfert2 = new Intent(getApplicationContext(), SelectForRent.class);
                                    startActivity(i_boatTranfert2);
                                }
                            }).show();

                    // finish();
                } else {
                    new AlertDialog.Builder(BoatTranfer.this)
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





    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent x = getIntent();
        selectedDestination = x.getStringExtra("selectedDestination");
        selectedCompany = x.getStringExtra("selectedCompany");
       // selectedCarPrice = Long.parseLong(x.getStringExtra("selectedPrice"));

        TextView x1 = (TextView) findViewById(R.id.transfer_text_view);
        x1.setText(selectedCompany);


        TextView x2 = (TextView) findViewById(R.id.destinatioon_text_view);
        x2.setText(selectedDestination);


        String [] loginParameters = {};
        sendRequest loginRequest = new sendRequest();
        loginRequest.delegate = BoatTranfer.this;
        loginRequest.execute(loginParameters);

        TextView uuu = (TextView) findViewById(R.id.totalPriceXfer);
        uuu.setText("-");

    }

    public void onTrsansferCompanyClick(View v){

        allCompanies.clear();
        destinationOfSelecledCompany.clear();
        tReturn.clear();
        tDepart.clear();
        String [] loginParameters = {};
        sendRequest loginRequest = new sendRequest();
        loginRequest.delegate = BoatTranfer.this;
        loginRequest.execute(loginParameters);
    }

    public void onDestinationClick(View v) {
        //destinationOfSelecledCompany.clear();
        fromWhere = 1;
        creatDialog(destinationOfSelecledCompany, BoatTranfer.this, fromWhere);


    }

    public void onXferDepartTimeClick(View v){
        fromWhere = 2;
        creatDialog(tDepart, BoatTranfer.this, fromWhere);

    }

    public void onXferReturnTimeClick(View v){
        fromWhere = 3;
        creatDialog(tReturn, BoatTranfer.this, fromWhere);

    }

    public void onXferReturnDateClick(View v){
        fromOrTo = 1;
        showDialog(TO_DATE_DIALOG_ID);
    }


    public void onXferRentDateClick(View v){
        fromOrTo = 0;
        showDialog(FROM_DATE_DIALOG_ID);
    }

    public void onXferNoteClick(View v){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString().trim();
                TextView pNote = (TextView) findViewById((R.id.editText_note));

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
                allCompanies.add(i, xmm);

                Log.i("COMPANY", x.getString("name"));
                Log.i("EMAIL", x.getString("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        fromWhere = 0;
       // creatDialog(allCompanies, BoatTranfer.this, fromWhere);
       // =======================================================================================
        for (int i = 0; i < companies.length(); i++) {
            JSONObject tm = null;
            try {
                tm = companies.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (tm.getString("name").equals(selectedCompany)) {
                    selectedEmail = tm.getString("email");
                    deses = tm.getJSONArray("destination");
                    for (int j = 0; j < deses.length(); j++) {
                        Log.i("DAMN", deses.getJSONObject(j).toString());
                        destinationOfSelecledCompany.add(j, deses.getJSONObject(j).getString("name"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < deses.length(); i++) {
            try {
                if (deses.getJSONObject(i).getString("name").equals(selectedDestination)) {
                    JSONArray dep = deses.getJSONObject(i).getJSONArray("tableDepart");
                    JSONArray ret = deses.getJSONObject(i).getJSONArray("tableReturn");
                    for (int ll = 0; ll < dep.length(); ll++) {
                        tDepart.add(ll, dep.getJSONObject(ll).getString("time"));
                    }
                    for (int ll = 0; ll < dep.length(); ll++) {
                        tReturn.add(ll, ret.getJSONObject(ll).getString("time"));
                    }

                    priceOneWay = deses.getJSONObject(i).getString("oneWayRate");
                    priceRoundTrip = deses.getJSONObject(i).getString("roundTripRate");

                    Log.i("PRICE", priceOneWay + " " + priceRoundTrip);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



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
                //arrayAdapter.add(cm.name);
            }
        } else if(fromWhere == 1){
            builderSingle.setTitle("Select Destination");
            for (int i = 0; i < data.size(); i++) {
                String a = "";
                a = destinationOfSelecledCompany.get(i);


                arrayAdapter.add(a);
            }
        } else if(fromWhere == 2) {
            builderSingle.setTitle("Choose depart time");
            for (int i = 0; i < data.size(); i++) {
                String a = "";
                a = tDepart.get(i);


                arrayAdapter.add(a);
            }
        } else if(fromWhere == 3) {
            builderSingle.setTitle("Choose retuen time");
            for (int i = 0; i < data.size(); i++) {
                String a = "";
                a = tReturn.get(i);


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

        if(fromWhere == 0){
            final ItemAdapter  arrayAdapter2 = new ItemAdapter(context, android.R.layout.select_dialog_singlechoice, m_parts);

            builderSingle.setAdapter(arrayAdapter2,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Item strName = arrayAdapter2.getItem(which);

                            TextView x = (TextView) findViewById(R.id.transfer_text_view);
                            x.setText(strName.pname);
                            selectedCompany = strName.pname;

                            for (int i = 0; i < companies.length(); i++) {
                                JSONObject tm = null;
                                try {
                                    tm = companies.getJSONObject(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    if (tm.getString("name").equals(selectedCompany)) {
                                        selectedEmail = tm.getString("email");
                                        deses = tm.getJSONArray("destination");
                                        for (int j = 0; j < deses.length(); j++) {
                                            Log.i("DAMN", deses.getJSONObject(j).toString());
                                            destinationOfSelecledCompany.add(j, deses.getJSONObject(j).getString("name"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        } else {


            builderSingle.setAdapter(arrayAdapter,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String strName = arrayAdapter.getItem(which);
                            if (fromWhere == 1) {

                                selectedDestination = strName;
                                TextView x = (TextView) findViewById(R.id.destinatioon_text_view);
                                x.setText(strName);


                                for (int i = 0; i < deses.length(); i++) {
                                    try {
                                        if (deses.getJSONObject(i).getString("name").equals(selectedDestination)) {
                                            JSONArray dep = deses.getJSONObject(i).getJSONArray("tableDepart");
                                            JSONArray ret = deses.getJSONObject(i).getJSONArray("tableReturn");
                                            for (int ll = 0; ll < dep.length(); ll++) {
                                                tDepart.add(ll, dep.getJSONObject(ll).getString("time"));
                                            }
                                            for (int ll = 0; ll < dep.length(); ll++) {
                                                tReturn.add(ll, ret.getJSONObject(ll).getString("time"));
                                            }

                                            priceOneWay = deses.getJSONObject(i).getString("oneWayRate").replaceAll("\\s+", "");
                                            priceRoundTrip = deses.getJSONObject(i).getString("roundTripRate").replaceAll("\\s+","");

                                            Log.i("PRICE", priceOneWay + " " + priceRoundTrip);

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                            } else if (fromWhere == 2) {
                                Button x = (Button) findViewById(R.id.bt_depart_time);
                                x.setText(strName);

                                selectedDepartTime = strName;
                            } else if (fromWhere == 3) {
                                Button x = (Button) findViewById(R.id.bt_return_time);
                                x.setText(strName);
                                selectedReturnTime = strName;
                            }

                        }
                    });
        }
        builderSingle.show();
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
    public void onDateSet(DatePicker view, int year, int month, int day) {
        switch (fromOrTo){
            case 0:

                fromMg = month;
                fromDg = day;
                fromYg =year;

                fromDate = String.valueOf(day) + "-" + String.valueOf(month+1) + "-" +  String.valueOf(year);

                Log.i("From date", fromDate);
                Button mm = (Button) findViewById(R.id.bt_depart_date);
                mm.setText(fromDate);

                //fromDateButton.setText(fromDate);
                break;
            case 1:

                toMg = month;
                toDg = day;
                toYg = year;

                toDate = String.valueOf(day) + "-" + String.valueOf(month+1) + "-" +  String.valueOf(year);

                Log.i("To date", toDate);
                Button nn = (Button) findViewById(R.id.bt_return_date);
                nn.setText(toDate);


                //toDateButton.setText(toDate);
        }

        long df = dayDiff(fromMg, fromDg, fromYg, toMg, toDg,  toYg);
        Log.i("Day diff = ", String.valueOf(df));

        if(df >= 0 && df < 30){

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
    }


    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

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
            HttpPost get = new HttpPost("https://128.199.97.22:1880/getTransferBoatCompanies");



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

    public String composeEmail(){
        String x = "";
        TextView pNote = (TextView) findViewById((R.id.editText_note));
        x = "Destination : " + selectedDestination + "\n";
        x = x + "Rent date: " + fromDate + " Time : " + selectedDepartTime + "\n";
        x = x + "Return date: " + toDate + " Time : " + selectedReturnTime + "\n";

        x = x + "Total price : " + totalPrice + " Bath" + "\n";
        if(pNote.length() > 0){
            x = x + "Note : " + pNote.getText();
        }

        return x;
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

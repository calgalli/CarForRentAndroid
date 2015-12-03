package com.example.jed.carforrent;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by jed on 8/15/15 AD.
 */
public class SelectForRent extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_rent_activity);

        // Set Custom Actionbar
        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("PHUKET RENT");

        ImageButton imageButton_map = (ImageButton) mCustomView.findViewById(R.id.imageButtonMap);

        imageButton_map.setVisibility(View.INVISIBLE);

        Button back = (Button) mCustomView.findViewById(R.id.actionbar_back);

        back.setVisibility(View.INVISIBLE);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

    }


    public void onBoatRentClick(View view) {

        Intent i_boatTranfert2 = new Intent(getApplicationContext(), selectBoatActivity.class);
        startActivity(i_boatTranfert2);
    }

    public void onBoatXferClick(View view) {

        Intent i_boatTranfert = new Intent(getApplicationContext(), selectDestinationActivity.class);
        startActivity(i_boatTranfert);
    }

    public void onCarRentClick(View view) {


        Intent i_carent = new Intent(getApplicationContext(), selectCarActivity.class);
        startActivity(i_carent);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

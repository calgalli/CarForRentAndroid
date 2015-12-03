package com.example.jed.carforrent;

import android.app.Application;
import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cake on 8/8/15 AD.
 */
public class globalVar extends Application {

    public Boolean allSelected = false;

    public Bitmap custommerImage;
    public String imageID;
    public String custommerID;
    public String custommerName;

    public String omiseToken = "pkey_test_50zicu2el50z0t59id6";

    public String cardName;
    public String cardCity;
    public String cardPostalCode;
    public String cardNumber;
    public String cardExpirationMonth;
    public String cardExpirationYear;
    public String cardSecurityCode;
    public String cardEmail;


    public String fare;
    public String isCash;


    String pathToImage = "/images/custommers/";
    String mainHost = "128.199.97.22";
    public String distanceKey = "AIzaSyCkkgvHEbB9Q0k4ICWzZBJNd_wV5GEYNzc";



    public  void setCustommerImage(Bitmap x){
        this.custommerImage = x;
    }


}

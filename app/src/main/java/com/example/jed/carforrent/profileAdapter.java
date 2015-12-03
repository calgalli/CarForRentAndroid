package com.example.jed.carforrent;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * Created by cake on 8/31/15 AD.
 */
public class profileAdapter extends ArrayAdapter<profileDetail> {

    // declaring our ArrayList of items
    private ArrayList<profileDetail> objects;


    Context context;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public profileAdapter(Context context, int textViewResourceId, ArrayList<profileDetail> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
        this.context = context;


    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.profile_cell, null);
        }

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
        profileDetail i = objects.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView profileTitle = (TextView) v.findViewById(R.id.profileTitle);
            TextView profileSubTitle = (TextView) v.findViewById(R.id.profileSubtitle);
            TextView profileStatus = (TextView) v.findViewById(R.id.profileStatus);


            // check to see if each individual textview is null.
            // if not, assign some text!
            if (profileTitle != null) {
                profileTitle.setText(i.title);
            }

            if (profileSubTitle != null) {
                profileSubTitle.setText(i.subtitle);
            }

            if (profileStatus != null) {
                if(i.status.equals("0")){
                    profileStatus.setText("Pending");
                    profileStatus.setBackgroundColor(Color.RED);
                } else {
                    profileStatus.setText("Confirmed");
                    profileStatus.setBackgroundColor(Color.GREEN);
                }

            }



        }

        // the view must be returned to our activity
        return v;

    }
}

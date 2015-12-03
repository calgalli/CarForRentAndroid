package com.example.jed.carforrent;

/**
 * Created by cake on 8/23/15 AD.
 */



import java.util.ArrayList;

        import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ItemAdapter extends ArrayAdapter<Item> {

    // declaring our ArrayList of items
    private ArrayList<Item> objects;

    ImageLoaderConfiguration config;

    Context context;
    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public ItemAdapter(Context context, int textViewResourceId, ArrayList<Item> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
        this.context = context;


    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent){

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.image_cell, null);
        }

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
        Item i = objects.get(position);
       // ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        config = new ImageLoaderConfiguration.Builder(context).build();
        if(!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().init(config);
        }

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView tt = (TextView) v.findViewById(R.id.textView1);
            ImageView ttd = (ImageView) v.findViewById(R.id.imageView1);

            // check to see if each individual textview is null.
            // if not, assign some text!
            if (tt != null){
                tt.setText(i.pname);
            }

            String imageUri = "http://128.199.97.22/images/rent/" + i.imageID;

           // Log.i("AAAAA", imageUri);


            //ImageLoader imageLoader = ImageLoader.getInstance().init(config); // Get singleton instance
            //ImageLoader.getInstance().init(config);
            ImageLoader.getInstance().displayImage(imageUri, ttd);

        }

        // the view must be returned to our activity
        return v;

    }

}

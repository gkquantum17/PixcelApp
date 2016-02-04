package com.example.gokul.pixcel;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Gokul on 1/28/2016.
 */
public final class Constants {

    private Constants(){
    }

    public static final String PACKAGE_NAME = "com.google.android.gms.location.Geofence";

    public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES_NAME";

    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

    /**
     * Used to set an expiration time for a geofence. After this amount of time Location Services
     * stops tracking the geofence.
     */
    public static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;

    /**
     * For this sample, geofences expire after twelve hours.
     */
    /*public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
          */
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 300000;
    //public static final float GEOFENCE_RADIUS_IN_METERS = 1609; // 1 mile, 1.6 km
    public static final float GEOFENCE_RADIUS_IN_METERS = 1607; // 1 mile, 1.6 km



    /**
     * Map for storing information about airports in the San Francisco bay area.
     */
    public static final HashMap<String, LatLng> BAY_AREA_LANDMARKS = new HashMap<String, LatLng>();

    static {

        /*ParseQuery<ParseObject> query = ParseQuery.getQuery("Images");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> images, ParseException e) {
                if (e == null) {
                    //if images exist in this data table, or is the images.size() is greater than 0, then store the individual elements in the images list in a ParseObject called image
                    if (images.size() > 0) {
                        for (ParseObject image : images) {
                            //delete the image data from the Parse data table
                            String objectId = image.getObjectId();
                            double latitutde = image.getParseGeoPoint("location").getLatitude();
                            double longitude = image.getParseGeoPoint("location").getLongitude();
                            BAY_AREA_LANDMARKS.put(objectId, new LatLng(latitutde, longitude));
                        }
                    } else {

                    }
                }else{
                }
            }
        });*/
        // San Francisco International Airport.

        //BAY_AREA_LANDMARKS.put(objectId, new LatLng(latitutde, longitude));
        BAY_AREA_LANDMARKS.put("SFO", new LatLng(37.621313, -122.378955));

        //Googleplex.
        BAY_AREA_LANDMARKS.put("GOOGLE", new LatLng(47.372028, -121.472966));

        //Test
        BAY_AREA_LANDMARKS.put("Udacity Studio", new LatLng(37.3999497, -122.1084776));
    }
}

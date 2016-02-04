package com.example.gokul.pixcel;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hp1 on 21-01-2015.
 */
public class Tab1 extends Fragment implements LocationListener {
protected List<ParseObject> listPosts;
    private GridLayoutManager gridLayoutManager;
    LocationManager locationManager;
    String provider;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab_1, container, false);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        final Location location = locationManager.getLastKnownLocation(provider);
        ParseGeoPoint parseLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

        //final ListView listView = (ListView) v.findViewById(R.id.listViewPosts);
        final RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Images");
            query.whereWithinMiles("location", parseLocation, 1);
            query.orderByDescending("createdAt");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (e == null) {
                        listPosts = list;

                        gridLayoutManager = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(gridLayoutManager);

                        PostListViewAdapter qadapter = new PostListViewAdapter(getActivity(), listPosts);
                        recyclerView.setAdapter(qadapter);
                    } else {

                    }
                }
            });

        }

        return v;
    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
package com.example.gokul.pixcel;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.Map;

/**

  * Created by hp1 on 21-01-2015.
  */
public class Tab2 extends Fragment implements LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {
    GoogleMap m_map;
    protected Location mLastLocation;
    public Double Latitude;
    public Double Longitude;
    MarkerOptions userLocation;
    public Double userLatitude;
    public Double userLongitude;
    boolean mapReady = false;
    private GoogleApiClient mGoogleApiClient;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2, container, false);
        FragmentManager fm = getChildFragmentManager();

        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        return v;
    }

    @Override
    public void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    public void onStop(){
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        userLatitude = mLastLocation.getLatitude();
        userLongitude = mLastLocation.getLongitude();
        userLocation = new MarkerOptions()
                .position(new LatLng(userLatitude, userLongitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Me");
        m_map.addMarker(userLocation);
        final CameraPosition LOCATION = CameraPosition.builder()
                .target(new LatLng(userLatitude, userLongitude))
                .zoom(15)
                .bearing(0)
                .tilt(45)
                .build();
        flyTo(LOCATION);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Images");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> images, ParseException e) {
                if (e == null) {
                    //if images exist in this data table, or is the images.size() is greater than 0, then store the individual elements in the images list in a ParseObject called image
                    if (images.size() > 0) {
                        for (ParseObject image : images) {
                            //delete the image data from the Parse data table
                            String name = image.getString("user");
                            double latitutde = image.getParseGeoPoint("location").getLatitude();
                            double longitude = image.getParseGeoPoint("location").getLongitude();
                            MarkerOptions picLocation = new MarkerOptions()
                                    .position(new LatLng(latitutde, longitude))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                                    .title(name);
                            m_map.addMarker(picLocation);
                        }
                    }
                } else {
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        m_map = googleMap;
    }
    private void flyTo(CameraPosition target){
        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));
    }
}
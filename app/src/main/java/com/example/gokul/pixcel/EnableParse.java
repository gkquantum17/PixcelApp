package com.example.gokul.pixcel;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by Gokul on 7/5/2015.
 */
public class EnableParse extends Application {
    public static final String APP_KEY = "IPqNx02rjmWkNIx7VlbtoPbRX2n21L3xuHDTlRKh";
    public static final String APP_CLIENT_ID = "P2WfP74JtpIkT8NrFTiUEo3uH8MTIKD26nacq1D3";

    @Override
    public void onCreate(){
        super.onCreate();
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, APP_KEY, APP_CLIENT_ID);
        ParseFacebookUtils.initialize(this);
        FacebookSdk.sdkInitialize(getApplicationContext());


    }
}

package com.example.gokul.pixcel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.facebook.Profile;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import static android.graphics.BitmapFactory.decodeFile;

/**
 * Created by Gokul on 1/18/2016.
 */
public class UploadImageAsyncTask extends AsyncTask<File, Void, ParseFile> {
    static int count = 0;
    WeakReference<ImageView> imageViewReference;
    final int TARGET_IMAGE_VIEW_WIDTH;
    final int TARGET_IMAGE_VIEW_HEIGHT;
    private File mImageFile;
    Location userLocation;
    CircularProgressButton btnUpload;
    String imageText;
    public UploadImageAsyncTask(ImageView imageView, int width, int height, CircularProgressButton btnUpload, Location location, String string){
        TARGET_IMAGE_VIEW_WIDTH = width;
        TARGET_IMAGE_VIEW_HEIGHT = height;
        imageViewReference = new WeakReference<ImageView>(imageView);
        this.btnUpload = btnUpload;
        this.userLocation = location;
        this.imageText = string;
    }
    @Override
    protected ParseFile doInBackground(File... files) {

        mImageFile = files[0];
        Bitmap bitmap = decodeBitmapFromFile(mImageFile);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        byte[] byteArray = stream.toByteArray();

        ParseFile file = new ParseFile("image.png", byteArray);

        return file;
    }

    @Override
    protected void onPostExecute(ParseFile file){

        if (userLocation != null){

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Images");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> images, ParseException e) {
                    if (e == null) {
                        //if images exist in this data table, or is the images.size() is greater than 0, then store the individual elements in the images list in a ParseObject called image
                        count = images.size();
                    }else{
                    }
                }
            });
            Profile profile = Profile.getCurrentProfile();
            String userId = profile.getId();
            ParseGeoPoint geoLocation = new ParseGeoPoint (userLocation.getLatitude(), userLocation.getLongitude());
            ParseObject Images = new ParseObject("Images");
            Images.put("image", file);
            Images.put("postText", imageText);
            Images.put("location", geoLocation);
            Images.put("user", ParseUser.getCurrentUser().getUsername());
            Images.put("fbUserId", userId);
            Images.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null){

                    }else{

                    }
                }
            });
        }


    }
    private int calculateInSampleSize(BitmapFactory.Options bmOptions){
        final int photoWidth = bmOptions.outWidth;
        final int photoHeight = bmOptions.outHeight;
        int scaleFactor = 1;

        if (photoWidth > TARGET_IMAGE_VIEW_HEIGHT || photoHeight > TARGET_IMAGE_VIEW_HEIGHT){
            final int halfPhotoWidth = photoWidth/2;
            final int halfPhotoHeight = photoHeight/2;
            while (halfPhotoWidth/scaleFactor > TARGET_IMAGE_VIEW_WIDTH
                    || halfPhotoHeight/scaleFactor > TARGET_IMAGE_VIEW_HEIGHT){
                scaleFactor +=2;
            }
        }
        return scaleFactor;

    }

    private Bitmap decodeBitmapFromFile(File imageFile){

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        decodeFile(imageFile.getAbsolutePath(), bmOptions);
        bmOptions.inSampleSize = calculateInSampleSize(bmOptions);
        bmOptions.inJustDecodeBounds = false;
        return decodeFile(imageFile.getAbsolutePath(), bmOptions);
    }

    public File getImageFile(){
        return mImageFile;
    }
}

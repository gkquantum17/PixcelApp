package com.example.gokul.pixcel;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.ref.WeakReference;

import static android.graphics.BitmapFactory.Options;
import static android.graphics.BitmapFactory.decodeFile;

/**
 * Created by Gokul on 1/12/2016.
 */
public class SingleBitmapWorkTask extends AsyncTask<File, Void, Bitmap> {

    WeakReference<ImageView> imageViewReference;
    final int TARGET_IMAGE_VIEW_WIDTH;
    final int TARGET_IMAGE_VIEW_HEIGHT;
    private File mImageFile;
    boolean usageControl;

    public SingleBitmapWorkTask(ImageView imageView, int width, int height, boolean usageControl){
        TARGET_IMAGE_VIEW_WIDTH = width;
        TARGET_IMAGE_VIEW_HEIGHT = height;
        imageViewReference = new WeakReference<ImageView>(imageView);
        this.usageControl = usageControl;
    }
    @Override
    protected Bitmap doInBackground(File... files) {
        //return decodeFile(files[0].getAbsolutePath());

        mImageFile = files[0];
        Bitmap bitmap = decodeBitmapFromFile(mImageFile);

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap){
        /*if (bitmap != null && imageViewReference != null){
            ImageView viewImage = imageViewReference.get();

            if (viewImage != null){
                viewImage.setImageBitmap(bitmap);
            }

        }*/


            if (bitmap != null && imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);

                }
            }


    }
    private int calculateInSampleSize(Options bmOptions){
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
        Options bmOptions = new Options();
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

package com.example.gokul.pixcel;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.WeakReference;

import static android.graphics.BitmapFactory.Options;
import static android.graphics.BitmapFactory.decodeFile;

/**
 * Created by Gokul on 1/12/2016.
 */
public class BitmapWorkTask extends AsyncTask<File, Void, Bitmap> {

    WeakReference<ImageView> imageViewReference;
    final static int TARGET_IMAGE_VIEW_WIDTH = 200;
    final static int TARGET_IMAGE_VIEW_HEIGHT = 200;
    private File mImageFile;

    public BitmapWorkTask(ImageView imageView){
        imageViewReference = new WeakReference<ImageView>(imageView);
    }
    @Override
    protected Bitmap doInBackground(File... files) {
        //return decodeFile(files[0].getAbsolutePath());
        mImageFile = files[0];
        return decodeBitmapFromFile(files[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap){
        if (bitmap != null && imageViewReference != null){
            ImageView viewImage = imageViewReference.get();

            if (viewImage != null){
                viewImage.setImageBitmap(bitmap);
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

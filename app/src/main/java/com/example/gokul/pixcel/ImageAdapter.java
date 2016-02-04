package com.example.gokul.pixcel;

/**
 * Created by Gokul on 1/12/2016.
 */

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by nigelhenshaw on 25/06/2015.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Bitmap placeHolderBitmap;
    private File[] imagesFileArray;
    private File imagesFile;
    private static RecyclerViewClickPositionInterface mPositionInterface;

    public static class AsyncDrawable extends BitmapDrawable {
        final WeakReference<BitmapWorkTask> taskReference;
        public AsyncDrawable(Resources resources,
                             Bitmap bitmap,
                             BitmapWorkTask bitmapWorkTask){
            super(resources, bitmap);
            taskReference = new WeakReference(bitmapWorkTask);
        }
        public BitmapWorkTask getBitmapWorkTask(){
            return taskReference.get();

        }
    }
   public ImageAdapter(File[] folderFileArray, RecyclerViewClickPositionInterface positionInterface) {
       mPositionInterface = positionInterface;
       imagesFileArray = folderFileArray;
    }
    /*public ImageAdapter(File folderFile) {
        imagesFile = folderFile;
    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_images_relative_layout, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //File imageFile = imagesFile.listFiles()[position];
        File imageFile = imagesFileArray[position];
        //1.unoptimized image loading
        //Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        //holder.getImageView().setImageBitmap(imageBitmap);

        //2.somewhat optimized image loading
        //Picasso.with(holder.getImageView().getContext()).load(imageFile).into(holder.getImageView());

        BitmapWorkTask workerTask = new BitmapWorkTask(holder.getImageView());
        workerTask.execute(imageFile);
        /*if (checkBitmapWorkTask(imagesFile, holder.getImageView())){
            BitmapWorkTask bitmapWorkTask = new BitmapWorkTask(holder.getImageView());
            AsyncDrawable asyncDrawable = new AsyncDrawable(holder.getImageView().getResources(),
                    placeHolderBitmap, bitmapWorkTask);
            holder.getImageView().setImageDrawable(asyncDrawable);
            bitmapWorkTask.execute(imageFile);
        }*/


    }

    @Override
    public int getItemCount() {
        //return imagesFile.listFiles().length;
        return imagesFileArray.length;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.imageGalleryView);
        }

        public ImageView getImageView() {
            return imageView;
        }

        @Override
        public void onClick(View view) {
            mPositionInterface.getRecyclerViewAdapterPosition(this.getPosition());
        }
    }
   /* public static boolean checkBitmapWorkTask(File imageFile, ImageView imageView){
        BitmapWorkTask bitmapWorkTask = getBitmapWorkTask(imageView);
        if (bitmapWorkTask != null){
            final File workerFile = bitmapWorkTask.getImageFile();
            if (workerFile != null){
                if (workerFile != imageFile){
                    bitmapWorkTask.cancel(true);
                }else{
                    // bitmap work task file is the same as image view is expecting
                    return false;
                }
            }
        }
        return true;
    }
    public static BitmapWorkTask getBitmapWorkTask(ImageView imageView){
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof AsyncDrawable){
            AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
            return asyncDrawable.getBitmapWorkTask();
        }
        return null;
    }*/

}

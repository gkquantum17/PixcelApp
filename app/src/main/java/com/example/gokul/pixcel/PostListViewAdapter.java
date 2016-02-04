package com.example.gokul.pixcel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static android.graphics.BitmapFactory.decodeFile;

/**
 * Created by Gokul on 12/2/2015.
 */
public class PostListViewAdapter extends RecyclerView.Adapter<PostListViewAdapter.ViewHolder>{
    private FragmentActivity adapterTab1Activity;
    private List<ParseObject> mPosts;
    String userId = null;

    public PostListViewAdapter(FragmentActivity tab1Activity, List<ParseObject> posts) {
        this.adapterTab1Activity = tab1Activity;
        this.mPosts = posts;


    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(adapterTab1Activity).inflate(R.layout.image_row, viewGroup, false);


        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final ParseObject statusObject = mPosts.get(position);

        //Toast.makeText(adapterTab1Activity, userId.toString(), Toast.LENGTH_LONG).show();

        if (statusObject.getString("fbUserId") != null){
            final String userId = statusObject.getString("fbUserId");

            GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(), "/" + userId + "/picture",null, HttpMethod.GET, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {
                    JSONObject jsonObject = response.getJSONObject();
                    try {

                        JSONObject data = jsonObject.getJSONObject("data");
                        String url = data.getString("url");
                        Picasso.with(adapterTab1Activity).load(url).placeholder(R.drawable.placeholder_user).into(viewHolder.imgPostProfile);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Bundle parameters =new Bundle();
            parameters.putString("type","large");
            parameters.putBoolean("redirect",false);
            request.setParameters(parameters);
            request.executeAsync();
        }


        final String objectId = statusObject.getObjectId();
        if (statusObject.getList("usersLikedList") != null){
            int likeCount = statusObject.getList("usersLikedList").size();
            String likes;
            if (likeCount > 1 || likeCount == 0){
                likes = "likes";
            }else{
                likes = "like";
            }
            viewHolder.textLikeCount.setText(Integer.toString(likeCount) + " " + likes);
            if (statusObject.getList("usersLikedList").contains(ParseUser.getCurrentUser().getUsername())) {
                viewHolder.btnLike.setBackgroundResource(R.drawable.like_filled);
            } else if (!statusObject.getList("usersLikedList").contains(ParseUser.getCurrentUser().getUsername())) {
                viewHolder.btnLike.setBackgroundResource(R.drawable.heart_like_2);
            }
        }

        if (statusObject.getString("postText") != null){
            viewHolder.textPostCaption.setText(statusObject.getString("postText"));
        }

        if (statusObject.getList("comments") != null){
            int commentCount = statusObject.getList("comments").size();
            String comment;
            if (commentCount > 1 || commentCount == 0){
                comment = "comments";
            }else{
                comment = "comment";
            }
            viewHolder.textCommentCount.setText(Integer.toString(commentCount) + " " + comment);
            if (statusObject.getList("commentingUsers").contains(ParseUser.getCurrentUser().getUsername())){
                viewHolder.btnComment.setBackgroundResource(R.drawable.comment_bubble_filled);
            }else if (!statusObject.getList("commentingUsers").contains(ParseUser.getCurrentUser().getUsername())){
                viewHolder.btnComment.setBackgroundResource(R.drawable.comment_w);
            }
        }
        if (statusObject.getParseFile("image") != null) {

            ParseFile image = statusObject.getParseFile("image");
            Uri imageUri = Uri.parse(image.getUrl());
            Picasso.with(adapterTab1Activity).load(imageUri.toString()).resize(1075, 1075).centerCrop().placeholder(R.drawable.image_placeholder2).into(viewHolder.imgPostItem);
        }else{
        }
        if (statusObject.getString("user") != null){
            String userName = statusObject.getString("user");
            viewHolder.textPostName.setText(userName);
        }else{

        }
        if (statusObject.getDate("createdAt") != null){
            String date = statusObject.getDate("createdAt").toString();
            viewHolder.textPostDate.setText(date);
        }else{

        }
        viewHolder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailsIntent = new Intent(adapterTab1Activity, PostDetailsActivity.class);
                detailsIntent.putExtra("objectId", objectId);
                adapterTab1Activity.startActivity(detailsIntent);
            }
        });
        viewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Images");
                query.getInBackground(statusObject.getObjectId(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(final ParseObject parseObject, ParseException e) {
                        if (e == null) {

                            if (statusObject.getList("usersLikedList") != null){
                                int likeCount = statusObject.getList("usersLikedList").size();
                                if (statusObject.getList("usersLikedList").contains(ParseUser.getCurrentUser().getUsername())) {
                                    likeCount--;
                                    String likes;
                                    if (likeCount > 1 || likeCount == 0){
                                        likes = "likes";
                                    }else{
                                        likes = "like";
                                    }
                                    viewHolder.textLikeCount.setText(Integer.toString(likeCount) + " " + likes);
                                    viewHolder.btnLike.setBackgroundResource(R.drawable.heart_like_2);
                                    parseObject.removeAll("usersLikedList", Arrays.asList(ParseUser.getCurrentUser().getUsername()));
                                } else if (!statusObject.getList("usersLikedList").contains(ParseUser.getCurrentUser().getUsername())) {
                                    likeCount++;
                                    String likes;
                                    if (likeCount > 1){
                                        likes = "likes";
                                    }else{
                                        likes = "like";
                                    }
                                    viewHolder.textLikeCount.setText(Integer.toString(likeCount) + " " + likes);
                                    viewHolder.btnLike.setBackgroundResource(R.drawable.like_filled);
                                    parseObject.addAll("usersLikedList", Arrays.asList(ParseUser.getCurrentUser().getUsername()));
                                }
                            }else {
                                int likeCount = 0;
                                likeCount++;
                                String likes;
                                if (likeCount > 1 || likeCount == 0){
                                    likes = "likes";
                                }else{
                                    likes = "like";
                                }
                                viewHolder.textLikeCount.setText(Integer.toString(likeCount) + " " + likes);
                                parseObject.addAll("usersLikedList", Arrays.asList(ParseUser.getCurrentUser().getUsername()));
                                viewHolder.btnLike.setBackgroundResource(R.drawable.like_filled);
                            }
                            parseObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        //Toast.makeText(adapterTab1Activity, "success", Toast.LENGTH_LONG).show();
                                    } else {
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(adapterTab1Activity);
                                        dialog.setTitle("Sorry");
                                        dialog.setMessage(e.getMessage());
                                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog.show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(adapterTab1Activity, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
      }
    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imgPostItem;
        TextView textPostName;
        TextView textPostDate;
        LinearLayout llComment;
        CircularImageView imgPostProfile;
        LinearLayout llLike;
        ImageButton btnLike;
        ImageButton btnComment;
        TextView textCommentCount;
        TextView textLikeCount;
        TextView textPostCaption;
        public ViewHolder(View itemView) {
            super(itemView);
            imgPostItem = (ImageView) itemView.findViewById(R.id.imgPostItem);
            textPostName =  (TextView) itemView.findViewById(R.id.textPostName);
            textPostDate = (TextView) itemView.findViewById(R.id.textPostDate);
            llLike = (LinearLayout) itemView.findViewById(R.id.llLike);
            btnLike = (ImageButton) itemView.findViewById(R.id.btnLike);
            textCommentCount = (TextView) itemView.findViewById(R.id.textCommentCount);
            textLikeCount = (TextView) itemView.findViewById(R.id.textLikeCount);
            btnComment = (ImageButton) itemView.findViewById(R.id.btnComment);
            imgPostProfile = (CircularImageView) itemView.findViewById(R.id.imgPostProfile);
            textPostCaption = (TextView) itemView.findViewById(R.id.textPostCaption);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){

                default:
                    break;
            }
        }
    }
}

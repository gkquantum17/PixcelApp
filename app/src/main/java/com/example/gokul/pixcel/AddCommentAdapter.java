package com.example.gokul.pixcel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.parse.FindCallback;
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

import java.util.Arrays;
import java.util.List;

/**
 * Created by Gokul on 8/18/2015.
 */
public class AddCommentAdapter extends RecyclerView.Adapter<AddCommentAdapter.ViewHolder>{
    private PostDetailsActivity adapterPDActivity;
    private List<String> mComments;
    private List<String> mCommentingUsers;
    private List<String> mCommentingFbIds;

    public AddCommentAdapter(PostDetailsActivity pdActivity, List<String> posts, List<String> users, List<String> fbIds) {
        this.adapterPDActivity = pdActivity;
        this.mComments = posts;
        this.mCommentingUsers = users;
        this.mCommentingFbIds = fbIds;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(adapterPDActivity).inflate(R.layout.comment_list_row, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        String userName = this.mCommentingUsers.get(position);

        viewHolder.textComment.setText(this.mComments.get(position));
        viewHolder.textCommentName.setText(this.mCommentingUsers.get(position));
        GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(), "/" + this.mCommentingFbIds.get(position) + "/picture",null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                JSONObject jsonObject = response.getJSONObject();
                try {

                    JSONObject data = jsonObject.getJSONObject("data");
                    String url = data.getString("url");
                    Picasso.with(adapterPDActivity).load(url).placeholder(R.drawable.placeholder_user).into(viewHolder.imgProfile);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters =new Bundle();
        parameters.putString("type","large");
        parameters.putBoolean("redirect", false);
        request.setParameters(parameters);
        request.executeAsync();
    }
    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView textComment;
        TextView textCommentName;
        CircularImageView imgProfile;
        public ViewHolder(View itemView) {
            super(itemView);
            textComment = (TextView) itemView.findViewById(R.id.textComment);
            textCommentName = (TextView) itemView.findViewById(R.id.textCommentName);
            imgProfile = (CircularImageView) itemView.findViewById(R.id.imgProfileComment);
        }
    }
}

package com.example.gokul.pixcel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


public class PostDetailsActivity extends ActionBarActivity {
    ImageView imgPost;
    EditText editComment;
    ImageButton btnComment;
    RecyclerView recyclerComments;
    TextView textNoComments;
    CircularImageView imgProfile;
    TextView textPanelHeadline;
    Toolbar mToolbar;

    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        Bundle records = getIntent().getExtras();
        final String objectId = records.getString("objectId");
        imgPost = (ImageView) this.findViewById(R.id.imgPostDetails);
        editComment = (EditText) this.findViewById(R.id.editComment);
        btnComment = (ImageButton) this.findViewById(R.id.btnSendComment);
        recyclerComments = (RecyclerView) this.findViewById(R.id.recyclerViewComments);
        textNoComments = (TextView) this.findViewById(R.id.textNoComments);
        imgProfile = (CircularImageView) this.findViewById(R.id.imgProfileDetails);
        textPanelHeadline = (TextView) this.findViewById(R.id.textPanelHeadline);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar2);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        ParseQuery<ParseObject> commentListQuery = new ParseQuery<ParseObject>("Images");
        commentListQuery.getInBackground(objectId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    if (parseObject.getList("comments") != null) {
                        if (parseObject.getList("comments").size() != 0) {
                            textNoComments.setVisibility(View.INVISIBLE);
                            List<String> parseCommentsList = parseObject.getList("comments");
                            List<String> parseCommentingUsersList = parseObject.getList("commentingUsers");
                            List<String> parseCommentingFbIds = parseObject.getList("commentingFbIds");
                            AddCommentAdapter commentAdapter = new AddCommentAdapter(PostDetailsActivity.this, parseCommentsList, parseCommentingUsersList, parseCommentingFbIds);
                            gridLayoutManager = new GridLayoutManager(PostDetailsActivity.this, 1, LinearLayoutManager.VERTICAL, false);
                            recyclerComments.setLayoutManager(gridLayoutManager);
                            recyclerComments.setAdapter(commentAdapter);
                        }
                    }
                    if (parseObject.getString("user") != null){
                        textPanelHeadline.setText(parseObject.getString("user").toString());
                    }
                } else {
                    Toast.makeText(PostDetailsActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Images");

                query.getInBackground(objectId, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if (e == null) {
                            Profile profile = Profile.getCurrentProfile();
                            String userId = profile.getId();
                            //parseObject.addAllUnique("CommentsArray", Arrays.asList("a", "b"));
                            parseObject.addAll("comments", Arrays.asList(editComment.getText().toString()));
                            parseObject.addAll("commentingUsers", Arrays.asList(ParseUser.getCurrentUser().getUsername()));
                            parseObject.addAll("commentingFbIds", Arrays.asList(userId));
                            textNoComments.setVisibility(View.INVISIBLE);
                            List<String> parseCommentsList = parseObject.getList("comments");
                            List<String> parseCommentingUsersList = parseObject.getList("commentingUsers");
                            List<String> parseCommentingFbIds = parseObject.getList("commentingFbIds");
                            AddCommentAdapter commentAdapter = new AddCommentAdapter(PostDetailsActivity.this, parseCommentsList, parseCommentingUsersList, parseCommentingFbIds);
                            parseObject.put("totalComments", parseCommentsList.size());
                            gridLayoutManager = new GridLayoutManager(PostDetailsActivity.this, 1, LinearLayoutManager.VERTICAL, false);
                            recyclerComments.setLayoutManager(gridLayoutManager);
                            recyclerComments.setAdapter(commentAdapter);
                            recyclerComments.scrollToPosition(parseCommentsList.size() - 1);
                            editComment.setText(null);

                            //Toast.makeText(ThoughtPostDetailsActivity.this, a.get(1).toString(), Toast.LENGTH_LONG).show();*/

                            parseObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        //Toast.makeText(ThoughtPostDetailsActivity.this, "success", Toast.LENGTH_LONG).show();
                                    } else {
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(PostDetailsActivity.this);
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
                            Toast.makeText(PostDetailsActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Images");
        query.whereEqualTo("objectId", objectId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {

                } else {
                    if (object.getString("fbUserId") != null){
                        final String userId = object.getString("fbUserId");

                        GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(), "/" + userId + "/picture",null, HttpMethod.GET, new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                JSONObject jsonObject = response.getJSONObject();
                                try {

                                    JSONObject data = jsonObject.getJSONObject("data");
                                    String url = data.getString("url");
                                    Picasso.with(getApplicationContext()).load(url).into(imgProfile);
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

                    ParseFile file = object.getParseFile("image");
                    Uri imageUri = Uri.parse(file.getUrl());
                    Picasso.with(PostDetailsActivity.this).load(imageUri.toString()).resize(1500, 1500).centerCrop().placeholder(R.drawable.image_placeholder2).into(imgPost);

                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

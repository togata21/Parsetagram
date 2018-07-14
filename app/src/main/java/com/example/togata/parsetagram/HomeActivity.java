package com.example.togata.parsetagram;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.togata.parsetagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private EditText descriptionInput;
    private Button createButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        descriptionInput = (EditText) findViewById(R.id.etDescription);
        createButton = (Button) findViewById(R.id.bCreate);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String description = descriptionInput.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();
                //dispatchTakePictureIntent();
                persistImage(imageBitmap, "image");
                final File file = new File(HomeActivity.this.getFilesDir()+"/image.jpg");
                //final File file = new File(Environment.getExternalStorageDirectory()+"/storage/emulated/0/DCIM/Camra/IMG_20180710_135020.jpg");
                final ParseFile pfile = new ParseFile(file);
                createPost(description, pfile, user);
            }
        });
        imageView = (ImageView) findViewById(R.id.ivImage);

        loadTopPosts();

    }

    private void createPost(String description, ParseFile image, ParseUser user){
        Post post = new Post();
        post.setDescription(description);
        post.setImage(image);
        post.setUser(user);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                  Log.d("HomeActivity", "Create post success!");
                } else{
                    Toast.makeText(HomeActivity.this, "fail", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }


    public void loadTopPosts() {
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("HomeActivity", "Posts[" + i + "] = " + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername());
                    }
                } else {
                    e.printStackTrace();
                    ;
                }
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    private void persistImage(Bitmap bitmap, String name) {
        File filesDir = this.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("HomeActivity", "Error writing bitmap", e);
        }
    }

    private void logOut(View v){

        ParseUser.logOut();
        SharedPreferences preferences = getSharedPreferences("com.example.togete.parsetagram", Context.MODE_PRIVATE);
        preferences.edit().remove("username").commit();
        preferences.edit().remove("password").commit();
        ParseUser currentUser = ParseUser.getCurrentUser();
        Intent returnIntent = new Intent(this, LoginActivity.class);
        startActivity(returnIntent);

        /*Intent returnIntent = new Intent(this, LoginActivity.class);
        setResult(2, returnIntent);
        startActivity(returnIntent);
        finish();*/
    }

    public void timeline(View v){
        Intent timeline = new Intent(this, FeedActivity.class);
        startActivity(timeline);
    }

}

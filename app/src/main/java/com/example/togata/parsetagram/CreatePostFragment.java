package com.example.togata.parsetagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.togata.parsetagram.model.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by togata on 7/11/18.
 */

public class CreatePostFragment extends Fragment {

    View v;
    private EditText descriptionInput;
    private Button createButton;
    private Button takePicture;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;
    ImageView imageView;

    public CreatePostFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances) {
        v = inflater.inflate(R.layout.activity_home, container, false);
        descriptionInput = (EditText) v.findViewById(R.id.etDescription);
        createButton = (Button) v.findViewById(R.id.bCreate);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String description = descriptionInput.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();
                persistImage(imageBitmap, "image");
                final File file = new File(v.getContext().getFilesDir()+"/image.jpg");
                final ParseFile pfile = new ParseFile(file);
                createPost(description, pfile, user);
                Toast.makeText(v.getContext(), "Created!", Toast.LENGTH_SHORT).show();
                descriptionInput.setText("");
                imageView.setImageResource(R.drawable.ic_launcher_background);
            }
        });
        imageView = (ImageView) v.findViewById(R.id.ivImage);

        takePicture = (Button) v.findViewById(R.id.bPicture);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        return v;
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
                    Toast.makeText(v.getContext(), "fail", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    private void persistImage(Bitmap bitmap, String name) {
        File filesDir = v.getContext().getFilesDir();
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

}

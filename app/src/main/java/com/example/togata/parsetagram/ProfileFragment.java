package com.example.togata.parsetagram;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by togata on 7/12/18.
 */

public class ProfileFragment extends Fragment {

    FeedAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    RecyclerView recycerView;
    Button signout;
    GridView grid;
    Button changeHandle;
    Button changeProPic;
    TextView handle;
    View v;
    Bitmap imageBitmap;

    public ProfileFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances) {

        v = inflater.inflate(R.layout.activity_profile, container, false);

        handle = (TextView) v.findViewById(R.id.tvHandleProfile);
        handle.setText(ParseUser.getCurrentUser().get("handle").toString());

        signout = (Button) v.findViewById(R.id.bLogOut);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                SharedPreferences preferences = v.getContext().getSharedPreferences("com.example.togete.parsetagram", Context.MODE_PRIVATE);
                preferences.edit().remove("username").commit();
                preferences.edit().remove("password").commit();
                ParseUser currentUser = ParseUser.getCurrentUser();
                Intent returnIntent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(returnIntent);
            }
        });
        grid = (GridView) v.findViewById(R.id.gvPicturesProfile);
        grid.setAdapter(new GridAdapter(getActivity()));

        changeHandle = (Button) v.findViewById(R.id.bAddHandle);
        changeHandle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                final EditText edittext = new EditText(v.getContext());
                alert.setMessage("Enter Your Message");
                alert.setTitle("Enter Your Title");

                alert.setView(edittext);

                alert.setPositiveButton("Yes Option", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String YouEditTextValue = edittext.getText().toString();
                    }
                });

                alert.setNegativeButton("Change", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
            }
        });

        changeProPic = (Button) v.findViewById(R.id.bAddProfilePicture);
        changeProPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            persistImage(imageBitmap, "image");
            final File file = new File(v.getContext().getFilesDir()+"/image.jpg");
            final ParseFile pfile = new ParseFile(file);
            ImageView imageView = (ImageView) v.findViewById(R.id.ivProfileProfile23);
            Glide.with(v.getContext())
                    .load(imageBitmap)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);
            ParseUser user = ParseUser.getCurrentUser();
            user.put("profilePicture", pfile);
            user.saveInBackground();
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
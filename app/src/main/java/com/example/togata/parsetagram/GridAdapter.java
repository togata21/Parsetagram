package com.example.togata.parsetagram;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.togata.parsetagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by togata on 7/13/18.
 */

public class GridAdapter extends BaseAdapter {

    private Context context;

    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.ufi_heart, R.drawable.ufi_heart,
            R.drawable.ufi_heart, R.drawable.ufi_heart,
            R.drawable.ufi_heart, R.drawable.ufi_heart,
            R.drawable.ufi_heart,R.drawable.ufi_heart,R.drawable.ufi_heart

    };

    // Constructor
    public GridAdapter(Context c){
        context = c;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageView imageView = new ImageView(context);
        final ArrayList<Post> posts = new ArrayList<Post>();
        Post.Query q = new Post.Query();
        q.getTop().withUser();

        q.findInBackground(new FindCallback<Post>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++){
                        posts.add(objects.get(i));
                    }
                    for (int i = 0; i < objects.size()/2; i++){
                        Post temp = objects.get(objects.size()-i-1);
                        posts.set(objects.size()-i-1, objects.get(i));
                        posts.set(i, temp);
                    }
                    int count = 0;
                    int index = 0;
                    ArrayList<String> names = new ArrayList<>();
                    for (int j = 0; j < objects.size(); j++){
                        names.add(objects.get(j).getUser().getUsername().toString());
                    }
                    while (count <= position && index < objects.size()){
                        String temp = objects.get(index).getUser().getUsername().toString();
                        String currentUser = ParseUser.getCurrentUser().getUsername().toString();
                        if ((temp).equals(currentUser)){
                            count += 1;
                        }
                        index += 1;
                    }
                    index--;
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeFile((posts.get(index).getImage().getFile().toPath().toString()));
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    imageView.setImageBitmap(bitmap);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
                    imageView.setPadding(2, 2, 2, 2);

                } else {
                    e.printStackTrace();
                }
            }
        });

        //imageView.setImageResource(mThumbIds[position]);
        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView.setLayoutParams(new GridView.LayoutParams(70, 70));
        return imageView;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}

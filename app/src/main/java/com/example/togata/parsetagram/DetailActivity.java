package com.example.togata.parsetagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.togata.parsetagram.model.Post;

import org.parceler.Parcels;

import java.util.Date;

/**
 * Created by togata on 7/11/18.
 */

public class DetailActivity extends AppCompatActivity{

    TextView captionDetail;
    TextView handleDetail;
    TextView locationDetail;
    TextView date;
    ImageView imageDetail;
    ImageView profileDetail;

    public DetailActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Post post = (Post) Parcels.unwrap(getIntent().getParcelableExtra("post"));

        captionDetail = (TextView) findViewById(R.id.tvDescriptionDetail);
        handleDetail = (TextView) findViewById(R.id.tvHandleDetail);
        locationDetail = (TextView) findViewById(R.id.tvLocationDetail);
        imageDetail = (ImageView) findViewById(R.id.ivImagePostDetail);
        profileDetail = (ImageView) findViewById(R.id.ivProfilePictureDetail);
        date = (TextView) findViewById(R.id.tvDateDetail);

        handleDetail.setText(post.getUser().getUsername()+"");
        captionDetail.setText(post.getDescription()+"");
        Glide.with(this)
                .load(post.getImage().getUrl())
                .into(imageDetail);

        date.setText(getRelativeTimeAgo(post.getDate()));
    }

    public void back(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    public String getRelativeTimeAgo(Date date) {
        String relativeDate = "";
        String newDate = "";
        long dateMillis;
        dateMillis = date.getTime();
        relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        int index = relativeDate.indexOf(' ');
        newDate = relativeDate.substring(0,index)+relativeDate.substring(index+1, index+2);

        return newDate;
    }
}

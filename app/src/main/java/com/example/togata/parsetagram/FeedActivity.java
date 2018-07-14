package com.example.togata.parsetagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class FeedActivity extends AppCompatActivity {

    FeedAdapter adapter;
    RecyclerView recycerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        recycerView = (RecyclerView) findViewById(R.id.rvTimeline);

        adapter = new FeedAdapter();
        recycerView.setLayoutManager(new LinearLayoutManager(this));
        recycerView.setAdapter(adapter);
        adapter.refresh();
    }

}

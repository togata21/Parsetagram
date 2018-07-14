package com.example.togata.parsetagram;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by togata on 7/11/18.
 */

public class FeedFragment extends Fragment {

    FeedAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    RecyclerView recycerView;
    View v;

    public FeedFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances) {

        v = inflater.inflate(R.layout.activity_feed, container, false);
        recycerView = (RecyclerView) v.findViewById(R.id.rvTimeline);
        adapter = new FeedAdapter();
        recycerView.setLayoutManager(new LinearLayoutManager((MainActivity)getActivity()));
        recycerView.setAdapter(adapter);
        adapter.refresh();
        adapter.feedFragment = this;

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.refresh();
                swipeContainer.setRefreshing(false);
            }
        });


        swipeContainer.setColorSchemeResources(android.R.color.holo_purple,
                android.R.color.holo_blue_dark,
                android.R.color.holo_blue_dark);
        recycerView.bringToFront();

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        adapter.refresh();
    }
}

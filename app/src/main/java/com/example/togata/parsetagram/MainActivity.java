package com.example.togata.parsetagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by togata on 7/11/18.
 */

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private ViewPageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Instagram");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new ViewPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new FeedFragment(), "Feed");
        adapter.addFragment(new CreatePostFragment(), "Create Post");
        adapter.addFragment(new ProfileFragment(), "Profile");

        viewPager.setAdapter(adapter);
        //tabLayout.setupWithViewPager(viewPager);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Menu menu = bottomNavigationView.getMenu();
                        MenuItem post = menu.findItem(R.id.nav_post);
                        MenuItem profile = menu.findItem(R.id.nav_profile);
                        MenuItem home = menu.findItem(R.id.nav_home);
                        switch (item.getItemId()) {
                            case R.id.nav_home:
                                viewPager.setCurrentItem(0);
                                item.setIcon(R.drawable.instagram_home_filled_24);

                                post.setIcon(R.drawable.instagram_new_post_outline_24);
                                profile.setIcon(R.drawable.instagram_user_outline_24);

                                break;
                            case R.id.nav_post:
                                viewPager.setCurrentItem(1);
                                item.setIcon(R.drawable.instagram_new_post_filled_24);

                                profile.setIcon(R.drawable.instagram_user_outline_24);
                                home.setIcon(R.drawable.instagram_home_outline_24);

                                break;
                            case R.id.nav_profile:
                                viewPager.setCurrentItem(2);
                                item.setIcon(R.drawable.instagram_user_filled_24);

                                post.setIcon(R.drawable.instagram_new_post_outline_24);
                                home.setIcon(R.drawable.instagram_home_outline_24);

                                break;
                        }
                        return false;
                    }
                });
    }

    public void changeFragment(int index){
        viewPager.setCurrentItem(index);
    }
}

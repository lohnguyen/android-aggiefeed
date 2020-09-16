package com.lohnguyen.aggiefeed.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lohnguyen.aggiefeed.R;
import com.lohnguyen.aggiefeed.fragments.BusFragment;
import com.lohnguyen.aggiefeed.fragments.FeedPagerFragment;
import com.lohnguyen.aggiefeed.viewmodels.FeedListViewModel;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final FeedPagerFragment feedPagerFragment = new FeedPagerFragment();;
    private final BusFragment busFragment = new BusFragment();
    FragmentManager fragmentManger = getSupportFragmentManager();
    Fragment activeFragment = feedPagerFragment;

    FeedListViewModel feedListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(this);

            setUpFragments();
        }

        feedListViewModel = new ViewModelProvider(this).get(FeedListViewModel.class);
    }

    private void setUpFragments() {
        fragmentManger.beginTransaction()
                .add(R.id.fragment_container, feedPagerFragment)
                .commit();

        fragmentManger.beginTransaction()
                .add(R.id.fragment_container, busFragment)
                .hide(busFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            feedListViewModel.fetchAll();
            return true;
        } else if (item.getItemId() == R.id.menu_add) {
            feedListViewModel.insertTest();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.bottom_action_feed:
                if (activeFragment != feedPagerFragment) {
                    fragmentManger.beginTransaction().hide(activeFragment).show(feedPagerFragment).commit();
                    activeFragment = feedPagerFragment;
                }
                break;

            case R.id.bottom_action_bus:
                if (activeFragment != busFragment) {
                    fragmentManger.beginTransaction().hide(activeFragment).show(busFragment).commit();
                    activeFragment = busFragment;
                }
                break;
        }
        return true;
    }
}
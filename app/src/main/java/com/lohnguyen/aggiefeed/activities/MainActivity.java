package com.lohnguyen.aggiefeed.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.lohnguyen.aggiefeed.R;
import com.lohnguyen.aggiefeed.fragments.FeedPagerFragment;
import com.lohnguyen.aggiefeed.viewmodels.FeedListViewModel;

public class MainActivity extends AppCompatActivity {

    FeedListViewModel feedListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
//            FeedPagerFragment feedPagerFragment = new FeedPagerFragment();
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.feed_pager_container, feedPagerFragment)
//                    .commit();

            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            assert navHostFragment != null;
            NavController navController = navHostFragment.getNavController();
        }

        feedListViewModel = new ViewModelProvider(this).get(FeedListViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
package com.lohnguyen.aggiefeed.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lohnguyen.aggiefeed.R;
import com.lohnguyen.aggiefeed.viewmodels.FeedListViewModel;

public class MainActivity extends AppCompatActivity {

    FeedListViewModel feedListViewModel;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            assert navHostFragment != null;
            navController = navHostFragment.getNavController();
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }

        feedListViewModel = new ViewModelProvider(this).get(FeedListViewModel.class);
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

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        // update the main content by replacing fragments
//        int menuItemID = menuItem.getItemId();
//        // handles clicks for the bottom navigation bar
//        switch (menuItemID) {
//            case R.id.bottom_action_bus:
//                navController.navigate(
//                        NavGraphDirections
//                                .actionGlobalBusTabFragment()
//                );
//                break;
//
//            case R.id.bottom_action_feed:
//                navController.navigate(
//                        NavGraphDirections
//                                .actionGlobalFeed()
//                );
//                break;
//        }
//        return true;
//    }
}
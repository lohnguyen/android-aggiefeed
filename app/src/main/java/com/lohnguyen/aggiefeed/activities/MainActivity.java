package com.lohnguyen.aggiefeed.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lohnguyen.aggiefeed.entities.AFActivity;
import com.lohnguyen.aggiefeed.R;
import com.lohnguyen.aggiefeed.adapters.AFActivityAdapter;
import com.lohnguyen.aggiefeed.viewmodels.MainViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_TITLE = "com.lohnguyen.aggiefeed.TITLE";
    public static final String EXTRA_DISPLAY_NAME = "com.lohnguyen.aggiefeed.DISPLAY_NAME";
    public static final String EXTRA_OBJECT_TYPE = "com.lohnguyen.aggiefeed.OBJECT_TYPE";
    public static final String EXTRA_PUBLISHED = "com.lohnguyen.aggiefeed.PUBLISHED";
    public static final String EXTRA_LOCATION = "com.lohnguyen.aggiefeed.LOCATION";
    public static final String EXTRA_START_DATE = "com.lohnguyen.aggiefeed.START_DATE";
    public static final String EXTRA_END_DATE = "com.lohnguyen.aggiefeed.END_DATE";

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getAll().observe(this, new Observer<List<AFActivity>>() {
            @Override
            public void onChanged(@Nullable final List<AFActivity> allActivities) {
                updateUI(allActivities);
            }
        });
        Log.d(LOG_TAG, "onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            mainViewModel.fetchAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI(final List<AFActivity> activities) {
        final AFActivityAdapter activityAdapter = new AFActivityAdapter(this, activities);
        ListView listView = findViewById(R.id.list_view_activities);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                AFActivity activity = activityAdapter.getItem(position);
                Intent intent = new Intent(view.getContext(), DetailActivity.class);

                assert activity != null;
                intent.putExtra(EXTRA_TITLE, activity.getTitle());
                intent.putExtra(EXTRA_DISPLAY_NAME, activity.getDisplayName());
                intent.putExtra(EXTRA_OBJECT_TYPE, activity.getObjectType());
                intent.putExtra(EXTRA_PUBLISHED, activity.getPublished());
                intent.putExtra(EXTRA_LOCATION, activity.getLocation());
                intent.putExtra(EXTRA_START_DATE, activity.getStartDate());
                intent.putExtra(EXTRA_END_DATE, activity.getEndDate());

                startActivity(intent);
            }
        });
        listView.setAdapter(activityAdapter);
    }
}
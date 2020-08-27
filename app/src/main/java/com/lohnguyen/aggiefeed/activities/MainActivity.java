package com.lohnguyen.aggiefeed.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.lohnguyen.aggiefeed.entities.FeedItem;
import com.lohnguyen.aggiefeed.R;
import com.lohnguyen.aggiefeed.adapters.FeedItemAdapter;
import com.lohnguyen.aggiefeed.viewmodels.MainViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FeedItemAdapter.OnFeedItemListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_TITLE = "com.lohnguyen.aggiefeed.TITLE";
    public static final String EXTRA_DISPLAY_NAME = "com.lohnguyen.aggiefeed.DISPLAY_NAME";
    public static final String EXTRA_OBJECT_TYPE = "com.lohnguyen.aggiefeed.OBJECT_TYPE";
    public static final String EXTRA_PUBLISHED = "com.lohnguyen.aggiefeed.PUBLISHED";
    public static final String EXTRA_LOCATION = "com.lohnguyen.aggiefeed.LOCATION";
    public static final String EXTRA_START_DATE = "com.lohnguyen.aggiefeed.START_DATE";
    public static final String EXTRA_END_DATE = "com.lohnguyen.aggiefeed.END_DATE";

    private MainViewModel mainViewModel;
    private List<FeedItem> allFeedItems;

    private RecyclerView recyclerView;
    private FeedItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerview_feeditems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getAll().observe(this, new Observer<List<FeedItem>>() {
            @Override
            public void onChanged(@Nullable final List<FeedItem> feedItems) {
                allFeedItems = feedItems;
                adapter = new FeedItemAdapter(allFeedItems, MainActivity.this);
                recyclerView.setAdapter(adapter);
            }
        });
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
        } else if (item.getItemId() == R.id.menu_add) {
            FeedItem activity = new FeedItem("Test", "Test", "Test",
                    "Test", "Test", "Test", "Test", "Test");
            mainViewModel.insert(activity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFeedItemClick(int position) {
        FeedItem feedItem = allFeedItems.get(position);
        Intent intent = new Intent(this, DetailActivity.class);

        assert feedItem != null;
        intent.putExtra(EXTRA_TITLE, feedItem.getTitle());
        intent.putExtra(EXTRA_DISPLAY_NAME, feedItem.getDisplayName());
        intent.putExtra(EXTRA_OBJECT_TYPE, feedItem.getObjectType());
        intent.putExtra(EXTRA_PUBLISHED, feedItem.getPublished());
        intent.putExtra(EXTRA_LOCATION, feedItem.getLocation());
        intent.putExtra(EXTRA_START_DATE, feedItem.getStartDate());
        intent.putExtra(EXTRA_END_DATE, feedItem.getEndDate());

        startActivity(intent);
    }
}
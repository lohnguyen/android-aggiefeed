package com.lohnguyen.aggiefeed.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lohnguyen.aggiefeed.R;
import com.lohnguyen.aggiefeed.activities.DetailActivity;
import com.lohnguyen.aggiefeed.adapters.FeedListAdapter;
import com.lohnguyen.aggiefeed.entities.FeedItem;
import com.lohnguyen.aggiefeed.viewmodels.FeedListViewModel;

import java.util.List;

public class FeedListFragment extends Fragment implements FeedListAdapter.OnFeedItemListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String EXTRA_TITLE = "com.lohnguyen.aggiefeed.TITLE";
    public static final String EXTRA_DISPLAY_NAME = "com.lohnguyen.aggiefeed.DISPLAY_NAME";
    public static final String EXTRA_OBJECT_TYPE = "com.lohnguyen.aggiefeed.OBJECT_TYPE";
    public static final String EXTRA_PUBLISHED = "com.lohnguyen.aggiefeed.PUBLISHED";
    public static final String EXTRA_LOCATION = "com.lohnguyen.aggiefeed.LOCATION";
    public static final String EXTRA_START_DATE = "com.lohnguyen.aggiefeed.START_DATE";
    public static final String EXTRA_END_DATE = "com.lohnguyen.aggiefeed.END_DATE";

    private FeedListViewModel feedListViewModel;
    private List<FeedItem> allFeedItems;

    private Boolean isHappeningToday = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Boolean refreshing = false;
    private RecyclerView recyclerView;
    private FeedListAdapter adapter = new FeedListAdapter(FeedListFragment.this);

    public FeedListFragment() {
    }

    public static FeedListFragment newInstance() {
        FeedListFragment fragment = new FeedListFragment();
        Bundle bundle = new Bundle();

        bundle.putBoolean("isHappeningToday", false);
        fragment.setArguments(bundle);

        return fragment;
    }

    public static FeedListFragment newInstance(Boolean isHappeningToday) {
        FeedListFragment fragment = new FeedListFragment();
        Bundle bundle = new Bundle();

        bundle.putBoolean("isHappeningToday", isHappeningToday);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            isHappeningToday = getArguments().getBoolean("isHappeningToday");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.swiperefresh_feeditems);
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = view.findViewById(R.id.recyclerview_feeditems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        feedListViewModel = new ViewModelProvider(this).get(FeedListViewModel.class);

        if (!isHappeningToday) {
            feedListViewModel.getAll().observe(getViewLifecycleOwner(), feedItems -> {
                allFeedItems = feedItems;
                adapter.setSearchFeedItems(allFeedItems);
                recyclerView.setAdapter(adapter);

                //  Set refresh icon to false
                if (refreshing) {
                    swipeRefreshLayout.setRefreshing(false);
                    refreshing = false;
                }

                Log.e("feedList", "feed changed " + feedItems.size());
            });
        } else {
            feedListViewModel.getHappeningTodayFeedItems().observe(getViewLifecycleOwner(), feedItems -> {
                allFeedItems = feedItems;
                adapter.setSearchFeedItems(allFeedItems);
                recyclerView.setAdapter(adapter);

                //  Set refresh icon to false
                if (refreshing) {
                    swipeRefreshLayout.setRefreshing(false);
                    refreshing = false;
                }

                Log.e("feedList", "happening today changed " + feedItems.size());
            });
        }
    }

    @Override
    public void onRefresh() {
        feedListViewModel.fetchAll();
        refreshing = true;
    }

    @Override
    public void onFeedItemClick(int position) {
        FeedItem feedItem = allFeedItems.get(position);
        Intent intent = new Intent(getContext(), DetailActivity.class);

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

    @Override
    public void onDestroy() {
        Log.e("feedList", "onDestroy");

        super.onDestroy();
    }
}

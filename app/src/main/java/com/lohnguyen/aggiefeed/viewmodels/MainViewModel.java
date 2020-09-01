package com.lohnguyen.aggiefeed.viewmodels;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lohnguyen.aggiefeed.entities.FeedItem;
import com.lohnguyen.aggiefeed.repositories.MainRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private MainRepository mainRepo;
    private LiveData<List<FeedItem>> allFeedItems;

    public MainViewModel(Application application) {
        super(application);
        mainRepo = new MainRepository(application);
        allFeedItems = mainRepo.getAll();
    }

    public LiveData<List<FeedItem>> getAll() {
        return allFeedItems;
    }

    public void fetchAll() {
        mainRepo.fetchAll(getApplication());
    }

    public void refresh(SwipeRefreshLayout swipeRefreshLayout) {
        fetchAll();
        swipeRefreshLayout.setRefreshing(false);
    }

    public void insert(FeedItem feedItem) {
        mainRepo.insert(feedItem);
        Toast.makeText(getApplication(), "Successfully add activity.", Toast.LENGTH_SHORT).show();
    }
}

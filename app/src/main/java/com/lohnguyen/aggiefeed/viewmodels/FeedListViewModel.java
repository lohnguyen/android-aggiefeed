package com.lohnguyen.aggiefeed.viewmodels;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lohnguyen.aggiefeed.entities.FeedItem;
import com.lohnguyen.aggiefeed.repositories.FeedListRepository;

import java.util.List;

public class FeedListViewModel extends AndroidViewModel {

    private FeedListRepository repo;

    public FeedListViewModel(Application application) {
        super(application);
        repo = new FeedListRepository(application);
        repo.fetchAll(application); // fetch from server when app is first launched
    }

    public LiveData<List<FeedItem>> getAll() {
        return repo.getAll();
    }

    public LiveData<List<FeedItem>> getHappeningTodayFeedItems() {
        return repo.getHappeningTodayFeedItems("Dateline");
    }

    public void fetchAll() {
        repo.fetchAll(getApplication());
    }

    public void insert(FeedItem feedItem) {
        repo.insert(feedItem);
        Toast.makeText(getApplication(), "Successfully add activity.", Toast.LENGTH_SHORT).show();
    }

    public void insertTest() {
        FeedItem activity = new FeedItem("Test", "Test", "Test",
                "Test", "Test", "Test", "Test", "Test");
        insert(activity);
    }
}

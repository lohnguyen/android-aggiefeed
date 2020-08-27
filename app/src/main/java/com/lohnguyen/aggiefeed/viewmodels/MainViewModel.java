package com.lohnguyen.aggiefeed.viewmodels;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lohnguyen.aggiefeed.entities.AFActivity;
import com.lohnguyen.aggiefeed.repositories.MainRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private MainRepository mainRepo;
    private LiveData<List<AFActivity>> allActivities;

    public MainViewModel(Application application) {
        super(application);
        mainRepo = new MainRepository(application);
        allActivities = mainRepo.getAll();
    }

    public LiveData<List<AFActivity>> getAll() {
        return allActivities;
    }

    public void fetchAll() {
        mainRepo.fetchAll(getApplication());
    }

    public void insert(AFActivity activity) {
        mainRepo.insert(activity);
        Toast.makeText(getApplication(), "Successfully add activity.", Toast.LENGTH_SHORT).show();
    }
}

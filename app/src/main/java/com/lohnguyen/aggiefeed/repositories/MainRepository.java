package com.lohnguyen.aggiefeed.repositories;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.android.volley.toolbox.JsonArrayRequest;
import com.lohnguyen.aggiefeed.room.AppRoomDatabase;
import com.lohnguyen.aggiefeed.volley.AppVolleySingleton;
import com.lohnguyen.aggiefeed.daos.AFActivityDao;
import com.lohnguyen.aggiefeed.entities.AFActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainRepository {

    private static final String AF_URL = "https://aggiefeed.ucdavis.edu/api/v1/activity/public?s=0?l=25";

    private AFActivityDao activityDao;
    private LiveData<List<AFActivity>> allActivities;

    public MainRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getInstance(application);
        activityDao = db.AFActivityDao();
        allActivities = activityDao.getAll();
        fetchAll(application);
    }

    public LiveData<List<AFActivity>> getAll() {
        return allActivities;
    }

    public void fetchAll(final Application application) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AF_URL, response -> {
            List<AFActivity> activities = new ArrayList<>();
            generateActivities(response, activities);
            deleteAll();
            insert(activities);
            Toast.makeText(application, "Successfully fetch activities.", Toast.LENGTH_LONG).show();
        }, error -> Toast.makeText(application, "Unable to fetch activities.", Toast.LENGTH_LONG).show());
        AppVolleySingleton.getInstance(application).addToRequestQueue(jsonArrayRequest);
    }

    private void generateActivities(JSONArray response, List<AFActivity> activities) {
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject activityJSON = response.getJSONObject(i);
                JSONObject object = activityJSON.getJSONObject("object");
                JSONObject event = object.getJSONObject("ucdEdusModel").getJSONObject("event");
                AFActivity activity = new AFActivity(activityJSON.getString("title"),
                        activityJSON.getJSONObject("actor").getString("displayName"),
                        object.getString("objectType"),
                        activityJSON.getString("published"),
                        event.getString("location"),
                        event.getString("startDate"),
                        event.getString("endDate"),
                        activityJSON.toString());
                activities.add(activity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void insert(List<AFActivity> activities) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            activityDao.insert(activities);
        });
    }

    public void deleteAll() {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            activityDao.deleteAll();
        });
    }
}

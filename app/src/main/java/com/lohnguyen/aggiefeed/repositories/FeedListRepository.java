package com.lohnguyen.aggiefeed.repositories;

import android.app.Application;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.android.volley.toolbox.JsonArrayRequest;
import com.lohnguyen.aggiefeed.daos.FeedItemDao;
import com.lohnguyen.aggiefeed.room.AppRoomDatabase;
import com.lohnguyen.aggiefeed.volley.AppVolleySingleton;
import com.lohnguyen.aggiefeed.entities.FeedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeedListRepository {

    private static final String AF_URL = "https://aggiefeed.ucdavis.edu/api/v1/activity/public?s=0?l=25";

    private FeedItemDao feedItemDao;

    public FeedListRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getInstance(application);
        feedItemDao = db.AFActivityDao();
    }

    public LiveData<List<FeedItem>> getAll() {
        return feedItemDao.getAll();
    }

    public LiveData<List<FeedItem>> getHappeningTodayFeedItems(String displayName) {
        return feedItemDao.getHappeningTodayFeedItems(displayName);
    }

    public void fetchAll(final Application application) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AF_URL, response -> {
            List<FeedItem> feedItems = new ArrayList<>();
            generateActivities(response, feedItems);
            deleteAndInsert(feedItems);
            Toast.makeText(application, "Successfully fetch activities.", Toast.LENGTH_SHORT).show();
        }, error -> Toast.makeText(application, "Unable to fetch activities.", Toast.LENGTH_SHORT).show());
        AppVolleySingleton.getInstance(application).addToRequestQueue(jsonArrayRequest);
    }

    /*
     * reference: parse HTML encoding
     * https://stackoverflow.com/questions/2918920/decode-html-entities-in-android
     */
    private String decodeHTML(String htmlStr) {
        return Html.fromHtml(htmlStr, Html.FROM_HTML_MODE_LEGACY).toString();
    }

    private void generateActivities(JSONArray response, List<FeedItem> feedItems) {
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject activityJSON = response.getJSONObject(i);
                JSONObject object = activityJSON.getJSONObject("object");
                String location = null, startDate = null, endDate = null;

                if (object.getString("objectType").equals("event")) {
                    JSONObject event = object.getJSONObject("ucdEdusModel").getJSONObject("event");
                    location = event.getString("location");
                    startDate = event.getString("startDate");
                    endDate = event.getString("endDate");
                }

                feedItems.add(new FeedItem(decodeHTML(activityJSON.getString("title")),
                        activityJSON.getJSONObject("actor").getString("displayName"),
                        object.getString("objectType"),
                        activityJSON.getString("published"),
                        location,
                        startDate,
                        endDate,
                        activityJSON.toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace(System.err);
        }
    }

    public void insert(FeedItem feedItem) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            feedItemDao.insert(feedItem);
        });
    }

    public void deleteAndInsert(List<FeedItem> feedItems) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            feedItemDao.deleteAllAndInsert(feedItems);
        });
    }
}

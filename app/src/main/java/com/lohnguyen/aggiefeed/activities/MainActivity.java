package com.lohnguyen.aggiefeed.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lohnguyen.aggiefeed.models.AFActivity;
import com.lohnguyen.aggiefeed.R;
import com.lohnguyen.aggiefeed.adapters.AFActivityAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_TITLE = "com.lohnguyen.aggiefeed.TITLE";
    public static final String EXTRA_DISPLAY_NAME = "com.lohnguyen.aggiefeed.DISPLAY_NAME";
    public static final String EXTRA_OBJECT_TYPE = "com.lohnguyen.aggiefeed.OBJECT_TYPE";
    public static final String EXTRA_PUBLISHED = "com.lohnguyen.aggiefeed.PUBLISHED";
    public static final String EXTRA_LOCATION = "com.lohnguyen.aggiefeed.LOCATION";
    public static final String EXTRA_START_DATE = "com.lohnguyen.aggiefeed.START_DATE";
    public static final String EXTRA_END_DATE = "com.lohnguyen.aggiefeed.END_DATE";
    private static final String AF_URL = "https://aggiefeed.ucdavis.edu/api/v1/activity/public?s=0?l=25";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AFActivityService activityService = new AFActivityService();
        activityService.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_more) {
            Log.d(LOG_TAG, "more clicked.");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI(final ArrayList<AFActivity> activities) {
        final AFActivityAdapter activityAdapter = new AFActivityAdapter(this, activities);
        ListView listView = findViewById(R.id.list_view_activities);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                AFActivity activity = activityAdapter.getItem(position);
                Intent intent = new Intent(view.getContext(), DetailActivity.class);

                intent.putExtra(EXTRA_TITLE, activity.title);
                intent.putExtra(EXTRA_DISPLAY_NAME, activity.displayName);
                intent.putExtra(EXTRA_OBJECT_TYPE, activity.objectType);
                intent.putExtra(EXTRA_PUBLISHED, activity.published);
                intent.putExtra(EXTRA_LOCATION, activity.location);
                intent.putExtra(EXTRA_START_DATE, activity.startDate);
                intent.putExtra(EXTRA_END_DATE, activity.endDate);

                startActivity(intent);
            }
        });
        listView.setAdapter(activityAdapter);
    }

    private class AFActivityService extends AsyncTask<URL, Void, ArrayList<AFActivity>> {
        @Override
        protected ArrayList<AFActivity> doInBackground(URL... urls) {
            URL url = createURL(AF_URL);
            String response;
            ArrayList<AFActivity> activities = new ArrayList<>();

            try {
                response = makeGET(url);
                generateActivities(activities, response);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return activities;
        }

        @Override
        protected void onPostExecute(ArrayList<AFActivity> activities) {
            updateUI(activities);
        }

        private URL createURL(String string) {
            URL url = null;
            try {
                url = new URL(string);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error with creating URL", e);
            }
            return url;
        }

        private String makeGET(URL url) {
            String response = "";
            HttpURLConnection urlConnection = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                response = readFromStream(in);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error with making GET request", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return response;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private void generateActivities(ArrayList<AFActivity> activities, String response) throws JSONException {
            JSONArray json = new JSONArray(response);

            for (int i = 0; i < json.length(); i++) {
                JSONObject activityJSON = json.getJSONObject(i);
                JSONObject object = activityJSON.getJSONObject("object");
                JSONObject event = object.getJSONObject("ucdEdusModel").getJSONObject("event");
                AFActivity activity = new AFActivity(activityJSON.getString("title"),
                        activityJSON.getJSONObject("actor").getString("displayName"),
                        object.getString("objectType"),
                        activityJSON.getString("published"),
                        event.getString("location"),
                        event.getString("startDate"),
                        event.getString("endDate"));

                activities.add(activity);
            }
        }
    }
}
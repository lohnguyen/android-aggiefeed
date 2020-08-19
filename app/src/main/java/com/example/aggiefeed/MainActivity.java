package com.example.aggiefeed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String AF_URL = "https://aggiefeed.ucdavis.edu/api/v1/activity/public?s=0?l=25";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AFActivityManager activityManager = new AFActivityManager();
        activityManager.execute();
    }

    private class AFActivityManager extends AsyncTask<URL, Void, ArrayList<AFActivity>> {

        @Override
        protected ArrayList<AFActivity> doInBackground(URL... urls) {
            URL url = createURL(AF_URL);
            String response = "";
            ArrayList<AFActivity> activities = new ArrayList<AFActivity>();

            try {
                response = makeGET(url);
                generateActivities(activities, response);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return activities;
        }

//        @Override
//        protected void onPostExecute(AFActivity... activities) {
//            updateUI(activities);
//        }

        private URL createURL(String string) {
            URL url = null;
            try {
                url = new URL(string);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error with creating URL", e);
                return null;
            }
            return url;
        }

        private String makeGET(URL url) throws IOException {
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
                Log.d(LOG_TAG, activityJSON.getString("title"));
            }
        }
    }
}
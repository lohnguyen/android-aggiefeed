package com.lohnguyen.aggiefeed.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.lohnguyen.aggiefeed.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    private String title;
    private String displayName;
    private String objectType;
    private String published;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        TextView titleView = findViewById(R.id.title);
        TextView displayNameView = findViewById(R.id.display_name);
        TextView objectTypeView = findViewById(R.id.object_type);
        TextView publishedView = findViewById(R.id.published);

        setAll(intent);

        titleView.setText(this.title);
        displayNameView.setText(this.displayName);
        objectTypeView.setText(this.objectType);
        publishedView.setText(this.published);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_calendar) {
            long begin = getUnixTime(this.published);
            long end = begin + 5 * 60 * 60 * 1000;
            addEvent(this.title, this.displayName, begin, end);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private long getUnixTime(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            Date date = format.parse(time);
            assert date != null;
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void setAll(Intent intent) {
        this.title = intent.getStringExtra(MainActivity.EXTRA_TITLE);
        this.displayName = intent.getStringExtra(MainActivity.EXTRA_DISPLAY_NAME);
        this.objectType = intent.getStringExtra(MainActivity.EXTRA_OBJECT_TYPE);
        this.published = intent.getStringExtra(MainActivity.EXTRA_PUBLISHED);
    }

    public void addEvent(String title, String location, long begin, long end) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(Events.CONTENT_URI)
                .putExtra(Events.TITLE, title)
                .putExtra(Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
package com.lohnguyen.aggiefeed.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lohnguyen.aggiefeed.R;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        TextView titleView = findViewById(R.id.title);
        TextView displayNameView = findViewById(R.id.display_name);
        TextView objectTypeView = findViewById(R.id.object_type);
        TextView publishedView = findViewById(R.id.published);

        titleView.setText(intent.getStringExtra(MainActivity.EXTRA_TITLE));
        displayNameView.setText(intent.getStringExtra(MainActivity.EXTRA_DISPLAY_NAME));
        objectTypeView.setText(intent.getStringExtra(MainActivity.EXTRA_OBJECT_TYPE));
        publishedView.setText(intent.getStringExtra(MainActivity.EXTRA_PUBLISHED));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_back) {
            goToMain();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToMain() {
        this.finish();
    }
}
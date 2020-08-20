package com.example.aggiefeed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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

    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
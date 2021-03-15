package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutUsActivity extends AppCompatActivity {
    ImageView backAbout;
    TextView myAppTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        backAbout= findViewById(R.id.backAbout);
        myAppTitle = findViewById(R.id.appTitle);
        myAppTitle.setPaintFlags(myAppTitle.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        backAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
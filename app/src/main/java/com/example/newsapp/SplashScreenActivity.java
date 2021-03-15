package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.firestore.Query;

import java.util.Locale;

public class SplashScreenActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private static final String MYPREFS = "MYPREFS";
    private static final String MYPRIVATE = "MYPRIVATE";
    private BroadcastReceiver mLangReceiver = null;
    Locale locale;
    String myTitle;
    String myContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(MYPREFS, MODE_PRIVATE);
        int setMode = sharedPreferences.getInt(MYPRIVATE, 0);
        if (setMode == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else if (setMode == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        locale = Locale.getDefault();
        setupLangReceiver();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (getIntent().hasExtra("pushnotification")) {
                    myTitle = getIntent().getStringExtra("title");
                    myContent = getIntent().getStringExtra("content");
                    i.putExtra("pushnotification", "yes");
                    i.putExtra("title", myTitle);
                    i.putExtra("content", myContent);
                }
                startActivity(i);
                finish();
            }
        }, 1500);
    }

    protected BroadcastReceiver setupLangReceiver() {

        if (mLangReceiver == null) {

            mLangReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    recreate();
                }

            };
            registerReceiver(mLangReceiver, new IntentFilter("Language.changed"));
        }

        return mLangReceiver;
    }

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(localeHelper.onAttach(newBase));
        locale = Locale.getDefault();
        if (locale.getLanguage().equals("en")) {
            updateViews("en");
        } else if (locale.getLanguage().equals("hi")) {
            updateViews("hi");
        }
    }

    private void updateViews(String languageCode) {
        Context context = localeHelper.setLocale(this, languageCode);
        Resources resources = context.getResources();


    }
}
package com.example.newsapp;

import android.app.Application;
import android.content.Context;


public class MainApplication extends Application {
    private Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(localeHelper.onAttach(base, "en"));
    }
}

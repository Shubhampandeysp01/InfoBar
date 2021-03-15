package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class settingsActivity extends AppCompatActivity {
    ImageView backSettings;
    TextView english, hindi, notify, feedback, submitNews, shareThis, rateThis, contactHere, aboutUs, defaultMode, light, night;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String MYPREFS = "MYPREFS";
    private static final String MYPRIVATE = "MYPRIVATE";
    int myMode;
    public static final String AUTO = "auto";
    public static final String AUTOBOOLEAN = "autoboolean";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(MYPREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        myMode = sharedPreferences.getInt(MYPRIVATE, 0);
        if (myMode == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else if (myMode == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        defaultMode = findViewById(R.id.defaultMode);
        night = findViewById(R.id.nightMode);
        light = findViewById(R.id.lightMode);
        english = findViewById(R.id.english);
        hindi = findViewById(R.id.hindi);
        shareThis = findViewById(R.id.shareThis);
        rateThis = findViewById(R.id.rateThis);
        contactHere = findViewById(R.id.contactHere);
        aboutUs = findViewById(R.id.aboutUs);
        backSettings = findViewById(R.id.backSettings);
        notify = findViewById(R.id.notify);
        feedback = findViewById(R.id.feedback);
        submitNews = findViewById(R.id.submitNews);
        if (myMode == 0) {
            defaultMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_process, 0, R.drawable.ic_checkme, 0);
            light.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lightmode, 0, 0, 0);
            night.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_moon, 0, 0, 0);
        } else if (myMode == 1) {
            defaultMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_process, 0, 0, 0);
            light.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lightmode, 0, R.drawable.ic_checkme, 0);
            night.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_moon, 0, 0, 0);
        } else {
            defaultMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_process, 0, 0, 0);
            light.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lightmode, 0, 0, 0);
            night.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_moon, 0, R.drawable.ic_checkme, 0);
        }
        Locale locale = Locale.getDefault();
        if (locale.getLanguage().equals("en")) {
            english.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_checkme, 0);
            hindi.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = "MY Hindi NOTIFICATION";
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);
            notificationManager.deleteNotificationChannel(channelId);
        }
        if (locale.getLanguage().equals("hi")) {
            hindi.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_checkme, 0);
            english.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        backSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName())
                        .putExtra(Settings.EXTRA_CHANNEL_ID, "channel_id");
                startActivity(settingsIntent);
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:"));
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"tj644686@gmail.com"});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(settingsActivity.this, "Please install email", Toast.LENGTH_SHORT).show();
                }
            }
        });
        submitNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSdtuiHOmTIg-YU9mqEhNwpOpg-FU0AEKwrZwYKDMjCr-KFiVg/viewform"));
                startActivity(intent);
            }
        });
        contactHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settingsActivity.this, ContactHereActivity.class);
                startActivity(intent);
            }
        });
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settingsActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(localeHelper.onAttach(base));
    }

    public void englishChange(View view) {
        FirebaseMessaging.getInstance().subscribeToTopic("English").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(settingsActivity.this, "English Subscribed", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor12 = getSharedPreferences(AUTO, MODE_PRIVATE).edit();
                editor12.putBoolean(AUTOBOOLEAN, false);
                editor12.apply();
                english.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_checkme, 0);
                hindi.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                updateViews("en");
                sendBroadcast(new Intent("Language.changed"));
                FirebaseMessaging.getInstance().unsubscribeFromTopic("Hindi").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(settingsActivity.this, "Hindi Unsubscribed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public void hindiChange(View view) {

        FirebaseMessaging.getInstance().subscribeToTopic("Hindi").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(settingsActivity.this, "Hindi Subscribed", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor12 = getSharedPreferences(AUTO, MODE_PRIVATE).edit();
                editor12.putBoolean(AUTOBOOLEAN, false);
                editor12.apply();
                hindi.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_checkme, 0);
                english.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                updateViews("hi");
                sendBroadcast(new Intent("Language.changed"));
                FirebaseMessaging.getInstance().unsubscribeFromTopic("English").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(settingsActivity.this, "English Unsubscribed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    private void updateViews(String languageCode) {
        Context context = localeHelper.setLocale(this, languageCode);
        Resources resources = context.getResources();
        notify.setText(resources.getString(R.string.notify));
        shareThis.setText(resources.getString(R.string.shareThis));
        rateThis.setText(resources.getString(R.string.rateThis));
        feedback.setText(resources.getString(R.string.feedback));
        submitNews.setText(resources.getString(R.string.submitNews));
        contactHere.setText(resources.getString(R.string.contactUs));
        aboutUs.setText(resources.getString(R.string.aboutUs));
        defaultMode.setText(resources.getString(R.string.defaultMode));
        light.setText(resources.getString(R.string.lightMode));
        night.setText(resources.getString(R.string.darkMode));

    }

    public void defaultMode(View view) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        defaultMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_process, 0, R.drawable.ic_checkme, 0);
        light.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lightmode, 0, 0, 0);
        night.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_moon, 0, 0, 0);
        editor.putInt(MYPRIVATE, 0);
        editor.apply();
        recreate();
    }

    public void lightMode(View view) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        light.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lightmode, 0, R.drawable.ic_checkme, 0);
        defaultMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_process, 0, 0, 0);
        night.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_moon, 0, 0, 0);
        editor.putInt(MYPRIVATE, 1);
        editor.apply();
        recreate();
    }

    public void nightMode(View view) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        night.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_moon, 0, R.drawable.ic_checkme, 0);
        defaultMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_process, 0, 0, 0);
        light.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lightmode, 0, 0, 0);
        editor.putInt(MYPRIVATE, 2);
        editor.apply();
        recreate();
    }


}
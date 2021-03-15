package com.example.newsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.List;
import java.util.Locale;

public class SearchNewsActivity extends AppCompatActivity {
    FrameLayout politicsImage, cricketImage, entertainmentImage, scienceImage, businessImage, worldImage, youtubeImage, coronaImage, officialImage, miscImage;
    ImageView imageBack;
    TextView overview, textStocks, settingsSearch, homeSearch;
    private BroadcastReceiver mLangReceiver = null;
    Locale locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_news);
        overview =  findViewById(R.id.overView);
        textStocks = findViewById(R.id.textStocks);
        settingsSearch = findViewById(R.id.settingsSearch);
        homeSearch = findViewById(R.id.homeSearch);
        politicsImage = findViewById(R.id.politicsImage);
        cricketImage = findViewById(R.id.cricketImage);
        entertainmentImage = findViewById(R.id.entertainmentImage);
        scienceImage = findViewById(R.id.scienceImage);
        businessImage = findViewById(R.id.businessImage);
        worldImage = findViewById(R.id.worldImage);
        youtubeImage = findViewById(R.id.youtubeImage);
        coronaImage = findViewById(R.id.coronaImage);
        imageBack = findViewById(R.id.backSearchNews);
        officialImage = findViewById(R.id.officialImage);
        miscImage = findViewById(R.id.miscImage);
        locale = Locale.getDefault();
        setupLangReceiver();
        textStocks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StocksActivity.class);
                startActivity(intent);
            }
        });
        homeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        settingsSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), settingsActivity.class);
                startActivity(intent);
            }
        });
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        politicsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToResultActivity("zpolitics");
            }
        });
        cricketImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToResultActivity("zcricket");
            }
        });
        entertainmentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToResultActivity("zentertainment");
            }
        });
        scienceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToResultActivity("ztechnology");
            }
        });
        businessImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToResultActivity("zbusiness");
            }
        });
        worldImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToResultActivity("zworld");
            }
        });
        youtubeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToResultActivity("zyoutube");
            }
        });
        coronaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToResultActivity("zcorona");
            }
        });
        officialImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToResultActivity("zofficial");
            }
        });
        miscImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToResultActivity("zmisc");
            }
        });


    }
    public void sendToResultActivity(String s){
        Intent intent = new Intent(getApplicationContext(), SearchResults.class);
        intent.putExtra("toolbartitle", s);
        startActivity(intent);
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
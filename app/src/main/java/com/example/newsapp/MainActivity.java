package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.FirestoreArray;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    ImageView goToTop;
    String mostRecentUtteranceID;
    TextToSpeech tts;
    androidx.appcompat.widget.AppCompatButton speakButton, searchButtonBottom;
    int positionOfPage;
    int checkPlayButton = 0;
    Boolean first = true;
    HashMap<String, String> params = new HashMap<>();
    Drawable pause;
    Drawable play;
    Locale locale;
    SharedPreferences sharedPreferences, myShared, sharedPreferencesEnglish;
    private static final String MYPREFS = "MYPREFS";
    private static final String MYPRIVATE = "MYPRIVATE";
    private BroadcastReceiver mLangReceiver = null;
    Query query;
    RelativeLayout relativeLayout;
    public static final String SETTINGS_TITLE = "settings";
    public static final String AUTO = "auto";
    public static final String AUTOBOOLEAN = "autoboolean";
    public static final String SETTINGS_NOTIFICATION = "notification_state";
    public static final String SETTINGS_ALREADY_SUBSCRIBED = "already_subscribed";
    Boolean auto, englishLang;


    @RequiresApi(api = Build.VERSION_CODES.O)
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
        adjustFontScale(getResources().getConfiguration());
        setContentView(R.layout.activity_main);
        myShared = getSharedPreferences(AUTO, MODE_PRIVATE);
        auto = myShared.getBoolean(AUTOBOOLEAN, true);
        if (auto) {
            sharedPreferencesEnglish = getSharedPreferences(SETTINGS_TITLE, MODE_PRIVATE);
            englishLang = sharedPreferencesEnglish.getBoolean(SETTINGS_NOTIFICATION, true);
            if (englishLang) {
                boolean alreadyEng = sharedPreferencesEnglish.getBoolean(SETTINGS_ALREADY_SUBSCRIBED, false);
                if (!alreadyEng) {
                    if (locale.getLanguage().toString().equals("en")) {
                        FirebaseMessaging.getInstance().subscribeToTopic("English").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "English Subscribed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (locale.getLanguage().toString().equals("hi")){
                        FirebaseMessaging.getInstance().subscribeToTopic("Hindi").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Hindi Subscribed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    SharedPreferences.Editor editor1 = getSharedPreferences(SETTINGS_TITLE, MODE_PRIVATE).edit();
                    editor1.putBoolean(SETTINGS_ALREADY_SUBSCRIBED, true);
                    editor1.apply();
                } else {
                    Toast.makeText(this, "Already Subscribed", Toast.LENGTH_SHORT).show();
                }
            }
        }
        relativeLayout = findViewById(R.id.parentLayout);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        tabLayout = findViewById(R.id.tabLayout);
        goToTop = findViewById(R.id.goTop);
        speakButton = findViewById(R.id.speakButton);
        searchButtonBottom = findViewById(R.id.searchButton);
        pause = ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_baseline_pause_24);
        pause.setBounds(0, 0, 60, 60);
        play = ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_baseline_play_arrow_24);
        play.setBounds(0, 0, 60, 60);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        firebaseFirestore = FirebaseFirestore.getInstance();
        viewPager2 = findViewById(R.id.recyclerView);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager2.setPageTransformer(new MyPageTransformer());
        locale = Locale.getDefault();
        setupLangReceiver();

        query = firebaseFirestore.collection(getString(R.string.newsLanguage)).orderBy("ttime", Query.Direction.DESCENDING);
        if (getIntent().hasExtra("pushnotification")) {
            String myTitle = getIntent().getStringExtra("title");
            String myContent = getIntent().getStringExtra("content");
            query = firebaseFirestore.collection(getString(R.string.newsLanguage)).whereEqualTo("title", myTitle);
        }
        FirestoreRecyclerOptions<list_data> options = new FirestoreRecyclerOptions.Builder<list_data>().setQuery(query, list_data.class).build();
        adapter = new FirestoreRecyclerAdapter<list_data, listViewHolder>(options) {

            @NonNull
            @Override
            public listViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data, parent, false);
                return new listViewHolder(view);
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onBindViewHolder(@NonNull listViewHolder holder, int position, @NonNull com.example.newsapp.list_data model) {
                holder.tvTitle.setText(model.getTitle());
                holder.tvContent.setText(model.getContent());
                Date date = model.getTtime().toDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy h:mm a");
                holder.tvTime.setText(dateFormat.format(date));

                String imageUrl = model.getImageUrl();
                Glide.with(getApplicationContext()).load(imageUrl).optionalFitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate().into(holder.imageView);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                        intent.putExtra("articleUrl", model.getArticleurl());
                        intent.putExtra("fakeurl", model.getShowurl());
                        startActivity(intent);
                    }
                });
                holder.readFull.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.cardView.callOnClick();
                    }
                });
                holder.flipPage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1, true);
                    }
                });
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (toolbar.isShown()) {
                            ViewGroup parent = findViewById(R.id.parentLayout);
                            Transition transition = new Slide(Gravity.TOP);
                            transition.setDuration(300);
                            transition.addTarget(toolbar);
                            toolbar.setVisibility(View.GONE);
                            TransitionManager.beginDelayedTransition(parent, transition);
                        } else {
                            ViewGroup parentElse = findViewById(R.id.parentLayout);
                            Transition transition = new Slide(Gravity.TOP);
                            transition.setDuration(300);
                            transition.addTarget(toolbar);
                            toolbar.setVisibility(View.VISIBLE);
                            TransitionManager.beginDelayedTransition(parentElse, transition);

                        }
                    }
                });

            }
        };
        viewPager2.setAdapter(adapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (checkPlayButton == 1) {
                    tts.stop();
                    convertTextToSpeech();
                } else {

                }
                ViewGroup parent = findViewById(R.id.parentLayout);
                Transition transition = new Slide(Gravity.TOP);
                transition.setDuration(300);
                transition.addTarget(toolbar);
                toolbar.setVisibility(View.GONE);
                TransitionManager.beginDelayedTransition(parent, transition);
                if (first && positionOffset == 0 && positionOffsetPixels == 0) {
                    onPageSelected(0);
                    first = false;
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                positionOfPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (getIntent().hasExtra("pushnotification")) {
                    if (!viewPager2.canScrollVertically(1)) {
                        Query myQuery = firebaseFirestore.collection(getString(R.string.newsLanguage)).orderBy("ttime", Query.Direction.DESCENDING);
                        FirestoreRecyclerOptions<list_data> myOtions = new FirestoreRecyclerOptions.Builder<list_data>().setQuery(myQuery, list_data.class).build();
                        adapter.updateOptions(myOtions);
                    }
                }
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    Query allQuery = firebaseFirestore.collection(getString(R.string.newsLanguage)).orderBy("ttime", Query.Direction.DESCENDING);
                    FirestoreRecyclerOptions<list_data> alloptions = new FirestoreRecyclerOptions.Builder<list_data>().setQuery(allQuery, list_data.class).build();
                    adapter.updateOptions(alloptions);

                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    Query trendingQuery = firebaseFirestore.collection(getString(R.string.newsLanguage)).orderBy("ttime", Query.Direction.DESCENDING).whereEqualTo("ztrending", true);
                    FirestoreRecyclerOptions<list_data> trendingOptions = new FirestoreRecyclerOptions.Builder<list_data>().setQuery(trendingQuery, list_data.class).build();
                    adapter.updateOptions(trendingOptions);
                } else if (tabLayout.getSelectedTabPosition() == 2) {
                    Query politicsQuery = firebaseFirestore.collection(getString(R.string.newsLanguage)).orderBy("ttime", Query.Direction.DESCENDING).whereEqualTo("zpolitics", true);
                    FirestoreRecyclerOptions<list_data> politicsOptions = new FirestoreRecyclerOptions.Builder<list_data>().setQuery(politicsQuery, list_data.class).build();
                    adapter.updateOptions(politicsOptions);
                } else if (tabLayout.getSelectedTabPosition() == 3) {
                    Query cricketQuery = firebaseFirestore.collection(getString(R.string.newsLanguage)).orderBy("ttime", Query.Direction.DESCENDING).whereEqualTo("zcricket", true);
                    FirestoreRecyclerOptions<list_data> cricketoptions = new FirestoreRecyclerOptions.Builder<list_data>().setQuery(cricketQuery, list_data.class).build();
                    adapter.updateOptions(cricketoptions);
                } else if (tabLayout.getSelectedTabPosition() == 4) {
                    Query entertainmentQuery = firebaseFirestore.collection(getString(R.string.newsLanguage)).orderBy("ttime", Query.Direction.DESCENDING).whereEqualTo("zentertainment", true);
                    FirestoreRecyclerOptions<list_data> entertainmentOptions = new FirestoreRecyclerOptions.Builder<list_data>().setQuery(entertainmentQuery, list_data.class).build();
                    adapter.updateOptions(entertainmentOptions);
                } else if (tabLayout.getSelectedTabPosition() == 5) {
                    Query technologyQuery = firebaseFirestore.collection(getString(R.string.newsLanguage)).orderBy("ttime", Query.Direction.DESCENDING).whereEqualTo("ztechnology", true);
                    FirestoreRecyclerOptions<list_data> technologyOptions = new FirestoreRecyclerOptions.Builder<list_data>().setQuery(technologyQuery, list_data.class).build();
                    adapter.updateOptions(technologyOptions);
                } else if (tabLayout.getSelectedTabPosition() == 6) {
                    Query businessQuery = firebaseFirestore.collection(getString(R.string.newsLanguage)).orderBy("ttime", Query.Direction.DESCENDING).whereEqualTo("zbusiness", true);
                    FirestoreRecyclerOptions<list_data> businessOptions = new FirestoreRecyclerOptions.Builder<list_data>().setQuery(businessQuery, list_data.class).build();
                    adapter.updateOptions(businessOptions);
                } else if (tabLayout.getSelectedTabPosition() == 7) {
                    Query worldQuery = firebaseFirestore.collection(getString(R.string.newsLanguage)).orderBy("ttime", Query.Direction.DESCENDING).whereEqualTo("zworld", true);
                    FirestoreRecyclerOptions<list_data> worldOptions = new FirestoreRecyclerOptions.Builder<list_data>().setQuery(worldQuery, list_data.class).build();
                    adapter.updateOptions(worldOptions);
                } else if (tabLayout.getSelectedTabPosition() == 8) {
                    Query youtubeQuery = firebaseFirestore.collection(getString(R.string.newsLanguage)).orderBy("ttime", Query.Direction.DESCENDING).whereEqualTo("zyoutube", true);
                    FirestoreRecyclerOptions<list_data> youtubeOptions = new FirestoreRecyclerOptions.Builder<list_data>().setQuery(youtubeQuery, list_data.class).build();
                    adapter.updateOptions(youtubeOptions);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mostRecentUtteranceID = (new Random().nextInt() % 9999999) + "";
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, mostRecentUtteranceID);
        tts = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

                    } else {
                        ttsInitialized();
                    }
                } else {

                }
            }
        });
        if (checkPlayButton == 0) {
            tts.stop();
        }


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

    @Override
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

    private void adjustFontScale(Configuration configuration) {
        configuration.fontScale = (float) 0.7;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);
    }

    private void ttsInitialized() {
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                if (checkPlayButton == 0){
                    tts.stop();
                }
            }

            @Override
            public void onDone(String utteranceId) {
                if (!utteranceId.equals(mostRecentUtteranceID)) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkPlayButton = 0;
                        speakButton.setCompoundDrawables(play, null, null, null);
                        speakButton.setText("Play");
                        tts.stop();
                        viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1, true);


                    }
                });

            }

            @Override
            public void onError(String utteranceId) {
                new Thread() {
                    public void run() {
                        MainActivity.this.runOnUiThread(new runnable() {
                            public void run() {
                                checkPlayButton = 0;
                                speakButton.setCompoundDrawables(play, null, null, null);
                                speakButton.setText("Play");
                                tts.stop();

                                Toast.makeText(MainActivity.this, "Starting, Click again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }.start();

            }
        });
    }

    private void convertTextToSpeech() {
        try {
            String mySpeech = getTextForSpeech();
            checkPlayButton = 1;

            if (mySpeech == null || "".equals(mySpeech)) {
                mySpeech = "content Not Available";
                tts.speak(mySpeech, TextToSpeech.QUEUE_FLUSH, params);
            } else {
                tts.speak(mySpeech, TextToSpeech.QUEUE_FLUSH, params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getTextForSpeech() {

        View view = ((RecyclerView) viewPager2.getChildAt(0)).findViewHolderForAdapterPosition(positionOfPage).itemView;
        TextView textView = (TextView) view.findViewById(R.id.tvContent);
        String dataToSpeak = textView.getText().toString();
        return dataToSpeak;

    }

    private class listViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout linearLayout;
        private CardView cardView;
        private TextView tvTitle, tvContent, tvTime, readFull, flipPage;
        private ImageView imageView;

        public listViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            imageView = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.cardView);
            linearLayout = itemView.findViewById(R.id.linearLayoutForTouch);
            tvTime = itemView.findViewById(R.id.timeArticle);
            readFull = itemView.findViewById(R.id.readFull);
            flipPage = itemView.findViewById(R.id.flipPage);
        }
    }

    public void goTopPage(View view) {
        Intent intent = new Intent(getApplicationContext(), SearchNewsActivity.class);
        startActivity(intent);
    }

    public void speakClick(View view) {
        if (checkPlayButton == 1) {
            tts.stop();
            checkPlayButton = 0;
            speakButton.setCompoundDrawables(play, null, null, null);
            speakButton.setText("Play");
        } else {
            speakButton.setCompoundDrawables(pause, null, null, null);
            speakButton.setText("Pause");
            convertTextToSpeech();
        }

    }

    public void shareSs(View view) {
        Dexter.withContext(this)
                .withPermissions(
                        WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {

            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()){
                    View view2 = ((RecyclerView) viewPager2.getChildAt(0)).findViewHolderForAdapterPosition(positionOfPage).itemView;
                    TextView textView = (TextView) view2.findViewById(R.id.tvTitle);
                    String titleForSs = textView.getText().toString();
                    String wholeText = titleForSs + System.lineSeparator() + "www.google.com";
                    view2.setDrawingCacheEnabled(true);
                    view2.setDrawingCacheBackgroundColor(0xfffafafa);
                    Bitmap bitmap = Bitmap.createBitmap(view2.getDrawingCache());
                    view2.setDrawingCacheEnabled(false);
                    String filePath = Environment.getExternalStorageDirectory() + "/Download/" + Calendar.getInstance().getTime().toString() + ".jpg";
                    File fileScreenshot = new File(filePath);
                    FileOutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = new FileOutputStream(fileScreenshot);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Uri uri = Uri.fromFile(fileScreenshot);
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("image/jpeg");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "News Article");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, wholeText);
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()){
                    showSettingsDialog();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError dexterError) {
                Toast.makeText(MainActivity.this, "Error with Permissions", Toast.LENGTH_SHORT).show();
            }
        }).onSameThread().check();


    }
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        locale = Locale.getDefault();
        if (locale.getLanguage().equals("en")) {
            updateViews("en");
        } else if (locale.getLanguage().equals("hi")) {
            updateViews("hi");
        }
        if (tts != null) {
            checkPlayButton = 0;
            speakButton.setCompoundDrawables(play, null, null, null);
            speakButton.setText("Play");
            tts.stop();
        }

    }

    @Override
    protected void onPause() {
        if (tts != null) {
            checkPlayButton = 0;
            speakButton.setCompoundDrawables(play, null, null, null);
            speakButton.setText("Play");
            tts.stop();
        }
        super.onPause();
        locale = Locale.getDefault();
        if (locale.getLanguage().equals("en")) {
            updateViews("en");
        } else if (locale.getLanguage().equals("hi")) {
            updateViews("hi");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        locale = Locale.getDefault();
        if (locale.getLanguage().equals("en")) {
            updateViews("en");
        } else if (locale.getLanguage().equals("hi")) {
            updateViews("hi");
        }
        if (tts != null) {
            checkPlayButton = 0;
            speakButton.setCompoundDrawables(play, null, null, null);
            speakButton.setText("Play");
            tts.stop();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        locale = Locale.getDefault();
        if (locale.getLanguage().equals("en")) {
            updateViews("en");
        } else if (locale.getLanguage().equals("hi")) {
            updateViews("hi");
        }
        if (tts != null) {
            checkPlayButton = 0;
            speakButton.setCompoundDrawables(play, null, null, null);
            speakButton.setText("Play");
            tts.stop();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        locale = Locale.getDefault();
        if (locale.getLanguage().equals("en")) {
            updateViews("en");
        } else if (locale.getLanguage().equals("hi")) {
            updateViews("hi");
        }
        if (tts != null) {
            checkPlayButton = 0;
            speakButton.setCompoundDrawables(play, null, null, null);
            speakButton.setText("Play");
            tts.stop();
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            checkPlayButton = 0;
            speakButton.setCompoundDrawables(play, null, null, null);
            speakButton.setText("Play");
            tts.stop();
        }
        super.onDestroy();
    }


    private abstract class runnable implements Runnable {

    }
}
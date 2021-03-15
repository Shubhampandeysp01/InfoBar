package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchResults extends AppCompatActivity {
    ImageView backSearch;
    TextView toolbarTitleResult;
    ViewPager2 viewPager2SearchResult;
    private BroadcastReceiver mLangReceiver = null;
    Query query;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    ConstraintLayout constraintLayout;
    Toolbar toolbarResult;
    int positionOfPage;
    Boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        setupLangReceiver();
        constraintLayout = findViewById(R.id.consSearchResult);
        toolbarResult = findViewById(R.id.toolbarSearchResult);
        toolbarResult.setVisibility(View.VISIBLE);
        backSearch = findViewById(R.id.backSearchResult);
        toolbarTitleResult = findViewById(R.id.toolbarTitleResult);
        viewPager2SearchResult = findViewById(R.id.viewpagerSearchResult);
        firebaseFirestore = FirebaseFirestore.getInstance();
        viewPager2SearchResult.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager2SearchResult.setPageTransformer(new MyPageTransformer());
        viewPager2SearchResult.setOverScrollMode(View.OVER_SCROLL_NEVER);
        backSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchNewsActivity.class);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        String toolbarTitleText = intent.getStringExtra("toolbartitle");
        String removeLetter = toolbarTitleText.substring(1);
        String firstLetter = removeLetter.substring(0, 1);
        String remainingLetters = removeLetter.substring(1, removeLetter.length());
        firstLetter = firstLetter.toUpperCase();
        if (removeLetter.equals("politics")){
            toolbarTitleResult.setText(R.string.politics);
        } else if (removeLetter.equals("cricket")){
            toolbarTitleResult.setText(R.string.cricket);
        } else if (removeLetter.equals("entertainment")){
            toolbarTitleResult.setText(R.string.entertainment);
        } else if (removeLetter.equals("technology")){
            toolbarTitleResult.setText(R.string.science);
        } else if (removeLetter.equals("business")){
            toolbarTitleResult.setText(R.string.business);
        } else if (removeLetter.equals("world")){
            toolbarTitleResult.setText(R.string.world);
        } else if (removeLetter.equals("youtube")){
            toolbarTitleResult.setText(R.string.youtube);
        } else if (removeLetter.equals("corona")){
            toolbarTitleResult.setText(R.string.corona);
        }else if (removeLetter.equals("official")){
            toolbarTitleResult.setText(R.string.Official);
        } else if (removeLetter.equals("misc")){
            toolbarTitleResult.setText(R.string.Misc);
        } else {
            toolbarTitleResult.setText(R.string.allStocks);
        }
        query = firebaseFirestore.collection(getString(R.string.newsLanguage))
                .orderBy("ttime", Query.Direction.DESCENDING).whereEqualTo(toolbarTitleText, true);
        FirestoreRecyclerOptions<list_data> options = new FirestoreRecyclerOptions.Builder<list_data>().setQuery(query, list_data.class).build();
        adapter = new FirestoreRecyclerAdapter<list_data, listViewHolder>(options) {
            @NonNull
            @Override
            public listViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data, parent, false);
                return new SearchResults.listViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull listViewHolder holder, int position, @NonNull list_data model) {
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
                        viewPager2SearchResult.setCurrentItem(viewPager2SearchResult.getCurrentItem() + 1, true);
                    }
                });
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (toolbarResult.isShown()) {
                            ViewGroup parent = findViewById(R.id.consSearchResult);
                            Transition transition = new Slide(Gravity.TOP);
                            transition.setDuration(300);
                            transition.addTarget(toolbarResult);
                            toolbarResult.setVisibility(View.GONE);
                            TransitionManager.beginDelayedTransition(parent, transition);
                        } else {
                            ViewGroup parentElse = findViewById(R.id.consSearchResult);
                            Transition transition = new Slide(Gravity.TOP);
                            transition.setDuration(300);
                            transition.addTarget(toolbarResult);
                            toolbarResult.setVisibility(View.VISIBLE);
                            TransitionManager.beginDelayedTransition(parentElse, transition);

                        }
                    }
                });
            }
        };
        viewPager2SearchResult.setAdapter(adapter);
        viewPager2SearchResult.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                ViewGroup parent = findViewById(R.id.consSearchResult);
                Transition transition = new Slide(Gravity.TOP);
                transition.setDuration(300);
                transition.addTarget(toolbarResult);
                toolbarResult.setVisibility(View.GONE);
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
            }
        });
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

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
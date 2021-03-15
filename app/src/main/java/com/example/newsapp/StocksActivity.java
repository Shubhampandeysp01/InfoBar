package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DecimalFormat;

public class StocksActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView back;
    private FirestoreRecyclerAdapter firestoreAdapter;
    Query stockQuery;
    Boolean marketprice, marketcapital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);
        back = findViewById(R.id.backStocks);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerStocks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        stockQuery = FirebaseFirestore.getInstance().collection("stocks");
        FirestoreRecyclerOptions<stock_list> recyclerOptions = new FirestoreRecyclerOptions.Builder<stock_list>().setQuery(stockQuery, stock_list.class).build();
        marketprice = true;
        marketcapital = true;
        firestoreAdapter = new FirestoreRecyclerAdapter<stock_list, myViewHolder>(recyclerOptions) {

            @NonNull
            @Override
            public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stocklist, parent, false);
                return new myViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull stock_list model) {
                holder.name.setText(model.getName());
                holder.mcapital.setText(model.getMcapital());
                holder.todayprice.setText(model.getTodayprice());
                float todayPrice = Float.parseFloat(model.getTodayprice());
                float yestPrice = Float.parseFloat(model.getYestprice());
                float profitLoss = todayPrice - yestPrice;
                DecimalFormat decimalFormat = new DecimalFormat();
                decimalFormat.setMaximumFractionDigits(2);
                if (profitLoss >= 0){
                    holder.profitLoss.setText(String.format("+%s", String.valueOf(decimalFormat.format(profitLoss))));
                    holder.profitLoss.setTextColor(Color.GREEN);
                } else {
                    holder.profitLoss.setText(String.valueOf(decimalFormat.format(profitLoss)));
                    holder.profitLoss.setTextColor(Color.RED);
                }
                float percent = (profitLoss*100)/yestPrice;
                if (percent >= 0){
                    holder.percentStock.setText(String.format("+%s%%", String.valueOf(decimalFormat.format(percent))));
                    holder.percentStock.setTextColor(Color.GREEN);
                } else {
                    holder.percentStock.setText(String.format("%s%%", String.valueOf(decimalFormat.format(percent))));
                    holder.percentStock.setTextColor(Color.RED);
                }


            }
        };
        recyclerView.setAdapter(firestoreAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    public void marketCapClick(View view) {
        if (marketcapital == true) {
            Query marketCapQuery = FirebaseFirestore.getInstance().collection("stocks").orderBy("mcapital", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<stock_list> recyclerOptions = new FirestoreRecyclerOptions.Builder<stock_list>().setQuery(marketCapQuery, stock_list.class).build();
            firestoreAdapter.updateOptions(recyclerOptions);
            marketcapital = false;
        } else {
            Query marktCapQuery = FirebaseFirestore.getInstance().collection("stocks").orderBy("mcapital", Query.Direction.ASCENDING);
            FirestoreRecyclerOptions<stock_list> recyclerOptions = new FirestoreRecyclerOptions.Builder<stock_list>().setQuery(marktCapQuery, stock_list.class).build();
            firestoreAdapter.updateOptions(recyclerOptions);
            marketcapital = true;
        }
    }

    public void marketPriceClick(View view) {
        if (marketprice == true){
        Query marktCapQuery = FirebaseFirestore.getInstance().collection("stocks").orderBy("todayprice", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<stock_list> recyclerOptions = new FirestoreRecyclerOptions.Builder<stock_list>().setQuery(marktCapQuery, stock_list.class).build();
        firestoreAdapter.updateOptions(recyclerOptions);
        marketprice = false;
        }  else {
            Query marktCapQuery = FirebaseFirestore.getInstance().collection("stocks").orderBy("todayprice", Query.Direction.ASCENDING);
            FirestoreRecyclerOptions<stock_list> recyclerOptions = new FirestoreRecyclerOptions.Builder<stock_list>().setQuery(marktCapQuery, stock_list.class).build();
            firestoreAdapter.updateOptions(recyclerOptions);
            marketprice = true;
        }
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView  mcapital, name, todayprice, profitLoss, percentStock;
        ConstraintLayout constraintLayout;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            mcapital = itemView.findViewById(R.id.marketCapital);
            name = itemView.findViewById(R.id.nameStock);
            todayprice = itemView.findViewById(R.id.marketPrice);
            percentStock = itemView.findViewById(R.id.percentStock);
            profitLoss = itemView.findViewById(R.id.profitLoss);
            constraintLayout = itemView.findViewById(R.id.secondConst);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firestoreAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firestoreAdapter.stopListening();
    }
}
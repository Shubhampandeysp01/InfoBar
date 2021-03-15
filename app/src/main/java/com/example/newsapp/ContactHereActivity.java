package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactHereActivity extends AppCompatActivity {
    ImageView backContact;
    TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_here);
        backContact = findViewById(R.id.backContact);
        appName = findViewById(R.id.appNameTextView);
        backContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        appName.setPaintFlags(appName.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
    }

    public void gmailClick(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"tj644686@gmail.com"});
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ContactHereActivity.this, "Please install email", Toast.LENGTH_SHORT).show();
        }
    }

    public void whatsappClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://wa.me/918294699376?text=Hi+Team"));
        startActivity(intent);
    }

    public void instaClick(View view) {
        Uri uri = Uri.parse("http://instagram.com/_u/infobar_ib");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/infobar_ib")));
        }
    }
}
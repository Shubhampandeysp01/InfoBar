package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WebActivity extends AppCompatActivity {
    WebView webView;
    TextView urlTextView;
    ImageView cancelButton, copyButton, shareButton;
    private ClipboardManager clipboardManager;
    private ClipData clipData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = findViewById(R.id.webView);
        urlTextView = findViewById(R.id.urlText);
        cancelButton = findViewById(R.id.cancelButton);
        copyButton = findViewById(R.id.copyButton);
        shareButton = findViewById(R.id.shareButton);
        Intent intent = getIntent();
        String articleUrl = intent.getStringExtra("articleUrl");
        String showUrl = intent.getStringExtra("fakeurl");
        urlTextView.setText(showUrl);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(articleUrl);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipData = ClipData.newRawUri("articleUrl", Uri.parse(articleUrl));
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(WebActivity.this, "Copied url", Toast.LENGTH_SHORT).show();
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing article URL");
                shareIntent.putExtra(Intent.EXTRA_TEXT, articleUrl);
                startActivity(Intent.createChooser(shareIntent, "Share article URL with...."));
            }
        });
    }
}
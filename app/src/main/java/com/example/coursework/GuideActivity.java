package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        WebView wv = findViewById(R.id.webview);
        wv.setWebViewClient(new WebViewClient());
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("file:///android_asset/index.html");

        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
        nav.getMenu().findItem(R.id.guide).setChecked(true);
        nav.setOnItemSelectedListener(item -> {
            Intent intent;
            int itemId = item.getItemId();
            if (itemId == R.id.activity_main) {
                intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.journal) {
                intent = new Intent(GuideActivity.this, JournalActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.sleep) {
                intent = new Intent(GuideActivity.this, SleepActivity.class);
                startActivity(intent);
            } else {
                return false;
            }
            return true;
        });
    }
}
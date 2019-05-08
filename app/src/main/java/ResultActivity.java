package com.example.myapplication;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ResultActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
            String query = getIntent().getStringExtra(SearchManager.QUERY);
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String query = intent.getStringExtra(SearchManager.QUERY);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
    }

}
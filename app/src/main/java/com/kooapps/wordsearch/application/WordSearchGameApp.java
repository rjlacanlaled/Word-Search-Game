package com.kooapps.wordsearch.application;

import android.app.Application;

import com.kooapps.wordsearch.handler.AdHandler;
import com.kooapps.wordsearch.handler.WordSearchGameHandler;

public class WordSearchGameApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        WordSearchGameHandler.getInstance().init(getApplicationContext());
        AdHandler.getInstance().init(getApplicationContext());
    }
}

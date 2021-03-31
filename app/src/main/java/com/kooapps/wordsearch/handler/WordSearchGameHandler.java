package com.kooapps.wordsearch.handler;

import android.content.Context;

import com.kooapps.wordsearch.model.core.main.WordSearchGame;
import com.kooapps.wordsearch.model.repository.WordsDatabase;

public class WordSearchGameHandler {

    private static final WordSearchGameHandler sHandler = new WordSearchGameHandler();
    private static boolean sInitialized;

    private WordSearchGame mWordSearchGame;

    public static WordSearchGameHandler getInstance() {
        return sHandler;
    }

    public WordSearchGame getWordSearchGame() {
        return mWordSearchGame;
    }

    public void init(Context context) {
        if (sInitialized) {
            return;
        }
        mWordSearchGame = new WordSearchGame(new WordsDatabase(context));
        sInitialized = true;
    }

}

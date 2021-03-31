package com.kooapps.wordsearch.model.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kooapps.wordsearch.model.utils.helper.AlphabetUtils;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordsDatabase {
    private static final String NAME_DATABASE = "words.db";
    private SQLiteAssetHelper mDataBaseHelper;

    //tables
    private static final List<String> sTables = Arrays.asList("afw",
            "behqz",
            "clj",
            "dtov",
            "mruk",
            "pin",
            "sgyx");

    private static final String TABLE_COLUMN_WORD = "word";

    public WordsDatabase(@NonNull Context context) {
        mDataBaseHelper = new SQLiteAssetHelper(context,NAME_DATABASE,null,1);
    }

    public boolean isValidWord(@NonNull String word) {
        String query = getQueryForWord(word);
        if(query == null) {
            return false;
        }

        Cursor cursor = getReadableDatabase().rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                return true;
            }
        }
        cursor.close();

        return false;
    }

    private
    @NonNull List<String> getAllValidWords(@NonNull List<String> words) {
        List<String> validWords = new ArrayList();
        String query = getQueryForWords(words);
        if(query == null) {
            return validWords;
        }

        Cursor cursor =  getReadableDatabase().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(TABLE_COLUMN_WORD)).toLowerCase();

                if(!validWords.contains(name)) {
                    validWords.add(name);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();

        return validWords;
    }

    private @Nullable
    static String getQueryForWord(@NonNull String word) {
        String tableName = getTable(word);
        if(tableName == null) {
            return null;
        }

        return "select * from "+tableName+" where word like '"+word+"'";
    }

    //all words must start with same character
    private @Nullable static String getQueryForWords(@NonNull List<String> words) {
        int numberOfWords = words.size();
        if(numberOfWords == 0) {
            return null;
        }

        String tableName = getTable(words.get(0));

        if(tableName == null) {
            return null;
        }

        String query = "select * from "+tableName+" where";

        int ctr = 0;
        for (String word:words) {
            query += " word like '"+word+"'";
            ctr++;
            if(ctr < numberOfWords) {
                query += " OR";
            }
        }

        return query;
    }

    private @Nullable static String getTable(@NonNull String word) {
        String firstLetter = word.substring(0,1);
        for (String table:sTables) {
            if(table.contains(firstLetter)) {
                return table;
            }
        }

        return null;
    }

    private SQLiteDatabase getReadableDatabase() {
        return mDataBaseHelper.getReadableDatabase();
    }

    public void close() {
        getReadableDatabase().close();
    }

    private String getQueryWordsStartingWithLetter(char startingLetter) {
        return "SELECT * FROM " + getTable(String.valueOf(startingLetter)) +
                " WHERE word LIKE " + "'" + startingLetter + "%'";
    }

    private String getMaxIDFromTableQuery(String table) {
        return "SELECT MAX(ID) FROM" + table;
    }

    private String getRandomWordQuery(String table) {
        return "SELECT * FROM " + table + " ORDER BY RANDOM() LIMIT 1";
    }

    //region WORDS_DATABASE_FETCHER

    public List<String> getWordsStartingWithSpecificLetter(char startingLetter) {
        List<String> allWordsStartingWithSpecificLetter = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery(getQueryWordsStartingWithLetter(startingLetter),
                null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(TABLE_COLUMN_WORD)).toLowerCase();

                if(!allWordsStartingWithSpecificLetter.contains(name)) {
                    allWordsStartingWithSpecificLetter.add(name);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        getReadableDatabase().close();
        return allWordsStartingWithSpecificLetter;
    }

    public String getRandomTable() {
        return getTable(String.valueOf(AlphabetUtils.generateRandomLowerCaseLetter()));
    }

    public String getQueryForWordWithLimit(String table, int maxWordLength) {

        return "SELECT * FROM " + table + " WHERE LENGTH(word) <= " + maxWordLength + " ORDER BY RANDOM() LIMIT 1";
    }

    public List<String> getRandomWordsFromDatabase(int count) {
        List<String> randomWords = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Cursor cursor = getReadableDatabase().rawQuery(getRandomWordQuery(getRandomTable()), null);

            if (cursor.moveToFirst()) {
                randomWords.add(cursor.getString(cursor.getColumnIndex(TABLE_COLUMN_WORD)).toLowerCase());
            }

            cursor.close();
        }
        return randomWords;
    }

    public List<String> generateWordWithMaxLength(int maxWordLength, int count) {
        List<String> wordList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Cursor cursor = getReadableDatabase().rawQuery(getQueryForWordWithLimit(getRandomTable(), maxWordLength), null);

            if (cursor.moveToFirst()) {
                wordList.add(cursor.getString(cursor.getColumnIndex(TABLE_COLUMN_WORD)).toLowerCase());
            }

            cursor.close();
        }

        return wordList;
    }

    //endregion WORDS_DATABASE_FETCHER
}

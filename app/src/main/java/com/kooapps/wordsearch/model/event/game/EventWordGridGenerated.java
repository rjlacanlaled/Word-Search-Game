package com.kooapps.wordsearch.model.event.game;

import androidx.annotation.NonNull;

import com.kooapps.wordsearch.R;
import com.kooapps.wordsearch.model.event.main.Event;

public class EventWordGridGenerated extends Event {

    @NonNull
    @Override
    public int getId() {
        return R.string.event_word_grid_generated;
    }
}

package com.kooapps.wordsearch.model.core.main;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.kooapps.wordsearch.model.core.helper.Cell;

import java.util.List;

public class WordMap extends BaseObservable {

    //region PROPERTIES

    // OBJECTS
    private String mWord;
    private List<Cell> mWordLocation;

    // STATES
    private boolean mFound;
    private boolean mFoundAnimated;

    //endregion PROPERTIES

    public WordMap(String word, List<Cell> wordLocation, boolean found) {
        mWord = word;
        mWordLocation = wordLocation;
        mFound = found;
    }

    //region GETTERS

    public String getWord() {
        return mWord;
    }

    public List<Cell> getWordLocation() {
        return mWordLocation;
    }

    @Bindable
    public boolean isFound() {
        return mFound;
    }

    public boolean isFoundAnimated() {
        return mFoundAnimated;
    }
    //endregion GETTERS

    //region SETTERS

    public void setWord(String mWord) {
        this.mWord = mWord;
    }

    public void setWordLocation(List<Cell> mWordLocation) {
        this.mWordLocation = mWordLocation;
    }

    public void setFound(boolean mFound) {
        this.mFound = mFound;
        notifyPropertyChanged(BR.found);
    }

    public void setFoundAnimated(boolean foundAnimated) {
        mFoundAnimated = foundAnimated;
    }

    //endregion SETTERS
}


















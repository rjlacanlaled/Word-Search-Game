package com.kooapps.wordsearch.model.core.main;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.kooapps.wordsearch.BR;
import com.kooapps.wordsearch.model.core.helper.Cell;

public class WordGridCell extends BaseObservable {

    private Cell mCell;
    private Character mCharacter;
    private boolean mSelected;
    private boolean mFound;
    private boolean mHighlighted;

    public WordGridCell(Cell cell, Character character) {
        mCell = cell;
        mCharacter = character;
    }

    public Cell getCell() {
        return mCell;
    }

    public void setCell(Cell mCell) {
        this.mCell = mCell;
    }

    @Bindable
    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean mSelected) {
        this.mSelected = mSelected;
        notifyPropertyChanged(BR.selected);
    }

    public Character getCharacter() {
        return mCharacter;
    }

    public void setCharacter(Character mCharacter) {
        this.mCharacter = mCharacter;
    }

    @Bindable
    public boolean isFound() {
        return mFound;
    }

    public void setFound(boolean mFound) {
        this.mFound = mFound;
        notifyPropertyChanged(BR.found);
    }

    @Bindable
    public boolean isHighlighted() {
        return mHighlighted;
    }

    public void setHighlighted(boolean mHighlighted) {
        this.mHighlighted = mHighlighted;
        notifyPropertyChanged(BR.highlighted);
    }
}

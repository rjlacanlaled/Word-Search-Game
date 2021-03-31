package com.kooapps.wordsearch.model.core.helper;

public class Grid {

    private int mRow;
    private int mColumn;

    public Grid(int row, int column) {
        mRow = Math.max(row, 1);
        mColumn = Math.max(column, 1) ;
    }

    public int getRow() { return mRow; }

    public int getColumn() { return mColumn; }

    public void setRow(int row) { mRow = Math.max(row, 1); }

    public void setColumn(int column) {mColumn = Math.max(column, 1); }

    public int getSize() {
        return mRow * mColumn;
    }

}

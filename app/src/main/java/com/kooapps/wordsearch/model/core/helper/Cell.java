package com.kooapps.wordsearch.model.core.helper;

public class Cell {

    private int mRow;
    private int mColumn;

    public Cell(int row, int column) {
        mRow = row;
        mColumn = column;
    }

    public int getRow() {
        return mRow;
    }

    public int getColumn() {
        return mColumn;
    }

    public static Cell getCellLocation(int position, Grid grid) {
        return new Cell(position/grid.getColumn(), position%grid.getColumn());
    }

    public static int getCellPosition(Cell cell, Grid grid) {
        return cell.mRow * grid.getColumn() + cell.mColumn;
    }
}

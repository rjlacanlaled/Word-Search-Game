package com.kooapps.wordsearch.model.core.main;

import android.util.Log;

import com.kooapps.wordsearch.model.core.helper.Cell;
import com.kooapps.wordsearch.model.core.helper.Grid;
import com.kooapps.wordsearch.model.core.helper.Range;
import com.kooapps.wordsearch.model.core.helper.StringLengthListSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WordGrid {

    //region CONSTANTS

    private static final Character CELL_PLACE_HOLDER = '*';
    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    //endregion CONSTANTS

    //region PROPERTIES

    // OBJECTS
    private Grid mGrid;
    private List<List<WordGridCell>> mGeneratedWordGrid;
    private List<String> mWordList;
    private List<WordMap> mWordMapList;
    private List<String> mWordsToFindList;

    //endregion PROPERTIES

    public WordGrid(Grid grid, List<String> wordList) {
        mGrid = grid;
        mWordList = wordList;
        mWordMapList = new ArrayList<>();
        mWordsToFindList = new ArrayList<>();
        Collections.sort(mWordList, new StringLengthListSort());
        initializeWordGrid();
        placeWordToQualifiedSlotsInGrid();
        fillVacantCellsWithRandomCharacters();
    }


    //region GETTERS

    public Grid getGrid() {
        return mGrid;
    }

    public List<String> getWordList() {
        return mWordList;
    }

    public List<List<WordGridCell>> getGeneratedWordGrid() {
        return mGeneratedWordGrid;
    }

    public List<WordMap> getWordMapList() {
        return mWordMapList;
    }

    public List<String> getWordsToFind() { return mWordsToFindList; }

    //endregion GETTERS

    //region SETTERS

    public void setGrid(Grid grid) {
        mGrid = grid;
    }

    public void setWordList(List<String> wordList) {
        mWordList = wordList;
    }

    //endregion SETTERS

    //region HELPERS

    private void initializeWordGrid() {
        mGeneratedWordGrid = new ArrayList<>();
        for (int i = 0; i < mGrid.getRow(); i++) {
            List<WordGridCell> wordGridRow = new ArrayList<>();
            for (int j = 0; j < mGrid.getColumn(); j++) {
                wordGridRow.add(new WordGridCell(new Cell(i, j), CELL_PLACE_HOLDER));
            }
            mGeneratedWordGrid.add(wordGridRow);
        }
    }

    private List<List<Cell>> getQualifiedHorizontalSlots(String word) {

        List<List<Cell>> qualifiedCellList = new ArrayList<>();
        Range horizontalRange = new Range(0, mGrid.getColumn() - word.length());

        if (horizontalRange.getMax() >= horizontalRange.getMin()) {
            for (int i = 0; i < mGrid.getRow(); i++) {
                for (int j = 0; j <= horizontalRange.getMax(); j++) {
                    List<Cell> cellForWord = new ArrayList<>();
                    for (int k = j; k < j + word.length(); k++) {
                        cellForWord.add(new Cell(i, k));
                    }
                    if (isCellListQualified(cellForWord, word)) {
                        qualifiedCellList.add(cellForWord);
                    }
                }
            }
        }

        return qualifiedCellList;
    }


    private List<List<Cell>> getQualifiedVerticalSlots(String word) {

        List<List<Cell>> qualifiedCellList = new ArrayList<>();
        Range verticalRange = new Range(0, mGrid.getRow() - word.length());

        if (verticalRange.getMax() >= verticalRange.getMin()) {
            for (int i = 0; i < mGrid.getColumn(); i++) {
                for (int j = 0; j <= verticalRange.getMax(); j++) {
                    List<Cell> cellForWord = new ArrayList<>();
                    for (int k = j; k < j + word.length(); k++) {
                        cellForWord.add(new Cell(k, i));
                    }
                    if (isCellListQualified(cellForWord, word)) {
                        qualifiedCellList.add(cellForWord);
                    }
                }
            }
        }

        return qualifiedCellList;
    }

    private boolean isCellListQualified(List<Cell> cellList, String word) {

        boolean hasPlace = true;

        for (int i = 0; i < cellList.size(); i++) {
            Character letter = word.charAt(i);
            int row = cellList.get(i).getRow();
            int column = cellList.get(i).getColumn();
            Character currentGridCharacter = mGeneratedWordGrid.get(row).get(column).getCharacter();
            if (!currentGridCharacter.equals(CELL_PLACE_HOLDER) && !currentGridCharacter.equals(letter)) {
                hasPlace = false;
                break;
            }
        }

        return hasPlace;
    }

    private void placeWordToCells(List<Cell> cellToFill, String word) {
        if (cellToFill.size() >= word.length()) {
            for (int i = 0; i < word.length(); i++) {
                int row = cellToFill.get(i).getRow();
                int column = cellToFill.get(i).getColumn();
                mGeneratedWordGrid.get(row).get(column).setCharacter(word.charAt(i));
            }
        }
    }

    private void placeWordToQualifiedSlotsInGrid() {
        for (String word : mWordList) {
            List<List<Cell>> horizontalSlots = getQualifiedHorizontalSlots(word);
            List<List<Cell>> verticalSlots = getQualifiedVerticalSlots(word);

            List<Cell> qualifiedCells = null;

            if (horizontalSlots.size() > 0 && verticalSlots.size() > 0) {
                boolean isHorizontal = new Random().nextBoolean();
                if (isHorizontal) {
                    qualifiedCells = horizontalSlots.get(new Random().nextInt(horizontalSlots.size()));
                } else {
                    qualifiedCells = verticalSlots.get(new Random().nextInt(verticalSlots.size()));
                }
            } else {
                if (horizontalSlots.size() > 0) {
                    qualifiedCells = horizontalSlots.get(new Random().nextInt(horizontalSlots.size()));
                } else if (verticalSlots.size() > 0) {
                    qualifiedCells = verticalSlots.get(new Random().nextInt(verticalSlots.size()));
                }
            }

            if (qualifiedCells != null) {
                placeWordToCells(qualifiedCells, word);
                mWordMapList.add(new WordMap(word, qualifiedCells, false));
                mWordsToFindList.add(word);
            }
        }
    }


    private void fillVacantCellsWithRandomCharacters() {
        for (int i = 0; i < mGrid.getRow(); i++) {
            for (int j = 0; j < mGrid.getColumn(); j++) {
                if (mGeneratedWordGrid.get(i).get(j).getCharacter().equals(CELL_PLACE_HOLDER)) {
                    mGeneratedWordGrid.get(i).get(j).setCharacter(ALPHABET[new Random().nextInt(ALPHABET.length)]);
                }
            }
        }
    }

    //endregion HELPERS

}



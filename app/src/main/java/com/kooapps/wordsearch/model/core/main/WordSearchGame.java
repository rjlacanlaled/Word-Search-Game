package com.kooapps.wordsearch.model.core.main;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.kooapps.wordsearch.BR;
import com.kooapps.wordsearch.model.core.helper.Cell;
import com.kooapps.wordsearch.model.core.helper.Grid;
import com.kooapps.wordsearch.model.event.game.NoTimeRemainingListener;
import com.kooapps.wordsearch.model.repository.WordsDatabase;

import java.util.ArrayList;
import java.util.List;

public class WordSearchGame extends BaseObservable {

    //region CONSTANTS

    private static final int DEFAULT_ROW = 4;
    private static final int DEFAULT_COLUMN = 4;
    private static final int DEFAULT_WORD_COUNT = 3;
    private static final int DEFAULT_COINS = 50;
    private static final int DEFAULT_HINT_COST = 25;
    private static final int DEFAULT_AD_REWARD = 25;
    private static final int DEFAULT_PROGRESS_GOAL = 10;
    private static final int DEFAULT_PROGRESS_INCREMENT = 2;
    private static final int MAX_LEVEL = 8;
    private static final int MAX_WORD_COUNT = 8;
    public static final int FOUND_WORD_BONUS = 2;
    private static final long DEFAULT_TIME_LIMIT_PER_WORD = 20000;
    //endregion CONSTANTS

    //region PROPERTIES

    private WordGrid mWordGrid;
    private List<String> mWordList;
    private WordsDatabase mWordDatabase;
    private Grid mGrid;
    private List<WordGridCell> mSelectedCells;
    private NoTimeRemainingListener mNoTimeRemainingListener;
    private CountDownTimer mGameTimer;

    private int mMaxWordCount;
    private int mCoin;
    private int mHintCost;
    private int mLevel;
    private int mProgress;
    private int mWordsFound;

    private long mTimeLimit;
    private long mTimeRemaining;

    // STATES
    private boolean mRunning;
    private boolean mActive;
    private boolean mHintVisible;
    private boolean mTimerRunning;

    //endregion PROPERTIES

    //region CONSTRUCTORS

    public WordSearchGame(WordsDatabase wordsDatabase) {
        mLevel = 1;
        mWordDatabase = wordsDatabase;
        mMaxWordCount = DEFAULT_WORD_COUNT;
        setCoin(DEFAULT_COINS);
        mHintCost = DEFAULT_HINT_COST;
        mGrid = new Grid(DEFAULT_ROW, DEFAULT_COLUMN);
        mSelectedCells = new ArrayList<>();
    }

    //endregion CONSTRUCTORS

    //region GETTERS

    public WordGrid getWordGrid() {
        return mWordGrid;
    }

    public WordsDatabase getWordDatabase() {
        return mWordDatabase;
    }

    public Grid getGrid() {
        return mGrid;
    }

    @Bindable
    public boolean isRunning() {
        return mRunning;
    }

    public List<WordGridCell> getSelectedCells() {
        return mSelectedCells;
    }

    @Bindable
    public int getCoin() {
        return mCoin;
    }

    @Bindable
    public boolean isActive() {
        return mActive;
    }

    @Bindable
    public long getTimeRemaining() {
        return mTimeRemaining;
    }

    public int getProgress() {
        return mProgress;
    }

    public int getLevel() {
        return mLevel;
    }

    public int getWordsFound() {
        return mWordsFound;
    }

    @Bindable
    public boolean isHintVisible() {
        return mHintVisible;
    }

    @Bindable
    public boolean isTimerRunning() {
        return mTimerRunning;
    }

    //endregion GETTERS

    //region SETTERS

    public void setWordGrid(WordGrid mWordGrid) {
        this.mWordGrid = mWordGrid;
    }

    public void setWordDatabase(WordsDatabase mWordDatabase) {
        this.mWordDatabase = mWordDatabase;
    }

    public void setGrid(Grid mGrid) {
        this.mGrid = mGrid;
    }

    public void setRunning(boolean mRunning) {
        this.mRunning = mRunning;
        notifyPropertyChanged(BR.running);
    }

    public void setCoin(int coin) {
        mCoin = coin;
        notifyPropertyChanged(BR.coin);
    }

    public void setActive(boolean mActive) {
        this.mActive = mActive;
        notifyPropertyChanged(BR.active);
    }

    public void setTimeLimit(long timeLimit) {
        mTimeLimit = timeLimit;
    }

    public void setTimeRemaining(long timeRemaining) {
        mTimeRemaining = timeRemaining;
        notifyPropertyChanged(BR.timeRemaining);
    }

    public void setNoTimeRemainingListener(NoTimeRemainingListener listener) {
        mNoTimeRemainingListener = listener;
    }

    public void setLevel(int level) {
        mLevel = Math.min(level, MAX_LEVEL);
    }

    public void setMaxWordCount(int maxWordCount) {
        mMaxWordCount = Math.min(maxWordCount, MAX_WORD_COUNT);
    }

    public void setProgress(int progress) {
        mProgress = progress;
    }

    public void setWordsFound(int wordsFound) {
        mWordsFound = wordsFound;
    }

    public void setHintVisible(boolean hintVisible) {
        mHintVisible = hintVisible;
        notifyPropertyChanged(BR.hintVisible);
    }

    public void setTimerRunning(boolean timerRunning) {
        mTimerRunning = timerRunning;
    }

    //endregion SETTERS

    //region DATA_HELPERS

    public void generateWordGrid() {
        mWordGrid = new WordGrid(mGrid, mWordList);
    }

    public void fetchWordList() {
        mWordList = generateWordList();
    }

    //endregion DATA_HELPERS

    //region DATA_FETCHER

    public List<String> generateWordList() {
        return mWordDatabase.generateWordWithMaxLength(Math.max(mGrid.getRow(), mGrid.getColumn()), mMaxWordCount);
    }

    //endregion DATA_FETCHER

    //region GAME_HELPERS

    public void clearSelectedCellList() {
        mSelectedCells.clear();
    }

    public boolean isCorrect(String word) {
        return mWordGrid.getWordsToFind().contains(word);
    }

    public void tagWordMapAsFound(WordMap wordMap) {
        if (wordMap != null) {
            wordMap.setFound(true);
            for (int i = 0; i < wordMap.getWordLocation().size(); i++) {
                Cell cell = wordMap.getWordLocation().get(i);
                mWordGrid.getGeneratedWordGrid().get(cell.getRow()).get(cell.getColumn()).setFound(true);
                mWordGrid.getWordsToFind().remove(wordMap.getWord());
            }
        }
    }

    public WordMap findWordMap(String word) {
        WordMap wordMap = null;
        for (WordMap map : mWordGrid.getWordMapList()) {
            if (map.getWord().equals(word)) {
                wordMap = map;
            }
        }

        return wordMap;
    }

    public void deselectCells() {
        if (mSelectedCells.size() > 0) {
            for (int i = 0; i < mSelectedCells.size(); i++) {
                mSelectedCells.get(i).setSelected(false);
            }
        }
    }

    public boolean hasEnoughCoins() {
        return mCoin >= mHintCost;
    }

    public boolean canLevelUp() {
        return mProgress >= DEFAULT_PROGRESS_GOAL;
    }

    public Cell getWordMapFirstCell(WordMap wordMap) {
        return wordMap.getWordLocation().get(0);
    }

    public WordMap getTopWordMapToFind() {
        return findWordMap(mWordGrid.getWordsToFind().get(0));
    }

    public void highlightWordGridCell(WordGridCell wordGridCell) {
        wordGridCell.setHighlighted(true);
    }

    public void unHighlightWordGridCell(WordGridCell wordGridCell) {
        wordGridCell.setHighlighted(false);
    }

    public void addReward() {
        setCoin(mCoin + DEFAULT_AD_REWARD);
    }

    public void incrementWordsFoundCount() {
        mWordsFound++;
    }

    public int getWordBonus() {
        return mWordsFound * FOUND_WORD_BONUS;
    }

    //endregion GAME_HELPERS

    //region GAME_MAIN

    public void startGame() {
        resetData();
        resumeGameTimer();
        setRunning(true);
    }

    public void stopGame() {
        cancelGameTimer();
        setRunning(false);
    }

    public boolean isLevelCleared() {
        return mWordGrid.getWordsToFind().size() < 1;
    }

    public void increaseProgress() {
        mProgress += DEFAULT_PROGRESS_INCREMENT;
    }

    public void resetData() {
        setWordsFound(0);
        updateGrid();
        updateMaxWord();
        fetchWordList();
        generateWordGrid();
        setTimeLimit(DEFAULT_TIME_LIMIT_PER_WORD * mWordGrid.getWordsToFind().size());
        setTimeRemaining(mTimeLimit);
    }

    public void updateGrid() {
        mGrid.setRow(DEFAULT_ROW + mLevel);
        mGrid.setColumn(DEFAULT_ROW + mLevel);
    }

    public void updateMaxWord() {
        setMaxWordCount(DEFAULT_WORD_COUNT + mLevel);
    }

    public void levelUp() {
        setLevel(mLevel + 1);
    }

    public void resetProgress() {
        setProgress(0);
    }

    public void addCoinBonus() {
        setCoin(mCoin + getWordBonus());
    }

    public void buyHint() {
        if (hasEnoughCoins()) {
            setCoin(mCoin - DEFAULT_HINT_COST);
            WordMap wordMap = getTopWordMapToFind();
            Cell cell = getWordMapFirstCell(wordMap);
            WordGridCell wordGridCell = mWordGrid.getGeneratedWordGrid().get(cell.getRow()).get(cell.getColumn());
            highlightWordGridCell(wordGridCell);
            Handler handler = new Handler(Looper.myLooper());
            handler.postDelayed(() -> unHighlightWordGridCell(wordGridCell), 3000);
        }
    }

    //endregion GAME_MAIN

    //region TIMER_HELPERS

    public void startGameTimer() {
        if (mGameTimer != null) {
            mGameTimer.start();
        }
    }

    public void cancelGameTimer() {
        if (mGameTimer != null) {
            mGameTimer.cancel();
            mGameTimer = null;
        }
    }

    public void resumeGameTimer() {
        initGameTimer();
        startGameTimer();
    }

    public void initGameTimer() {
        mGameTimer = new CountDownTimer(mTimeRemaining, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                setTimerRunning(true);
                setTimeRemaining(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                setTimeRemaining(0);
                stopGame();
                if (mNoTimeRemainingListener != null) {
                    mNoTimeRemainingListener.onTimeLimitReached();
                }
            }
        };
    }

    //endregion TIMER_HELPERS


    //region INPUT_PROCESSING

    public void captureSelectedCell(int position) {
        Cell textViewCell = Cell.getCellLocation(position, getGrid());
        WordGridCell wordGridCell = getWordGrid().getGeneratedWordGrid().get(textViewCell.getRow()).get(textViewCell.getColumn());
        if (!getSelectedCells().contains(wordGridCell)) {
            getSelectedCells().add(wordGridCell);
            wordGridCell.setSelected(true);
        }
    }

    public void processSelectedCells() {
        if (getSelectedCells().size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (WordGridCell cell : getSelectedCells()) {
                builder.append(cell.getCharacter());
            }
            if (isCorrect(builder.toString())) {
                WordMap wordMap = findWordMap(builder.toString());
                if (!wordMap.isFound()){
                    tagWordMapAsFound(wordMap);
                    incrementWordsFoundCount();
                    if (isLevelCleared()) {
                        increaseProgress();
                        if (canLevelUp()) {
                            levelUp();
                            resetProgress();
                        }
                    }
                }
            }
            deselectCells();
            clearSelectedCellList();
        }
    }

    //endregion INPUT_PROCESSING

}


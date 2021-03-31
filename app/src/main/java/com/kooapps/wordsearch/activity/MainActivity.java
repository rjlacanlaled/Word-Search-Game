package com.kooapps.wordsearch.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.kooapps.wordsearch.R;
import com.kooapps.wordsearch.ads.main.admob.RewardedAdmobLoader;
import com.kooapps.wordsearch.ads.main.admob.RewardedAdmobManager;
import com.kooapps.wordsearch.databinding.ActivityMainBinding;
import com.kooapps.wordsearch.fragment.AdLoadingDialogFragment;
import com.kooapps.wordsearch.handler.AdHandler;
import com.kooapps.wordsearch.handler.WordSearchGameHandler;
import com.kooapps.wordsearch.model.core.helper.Cell;
import com.kooapps.wordsearch.model.core.main.WordGridCell;
import com.kooapps.wordsearch.model.core.main.WordMap;
import com.kooapps.wordsearch.model.core.main.WordSearchGame;
import com.kooapps.wordsearch.model.event.ads.EventRewardedAdDismissed;
import com.kooapps.wordsearch.model.event.ads.EventRewardedAdFailed;
import com.kooapps.wordsearch.model.event.ads.EventRewardedAdLoaded;
import com.kooapps.wordsearch.model.event.game.NoTimeRemainingListener;
import com.kooapps.wordsearch.model.event.gesture.PanGestureListener;
import com.kooapps.wordsearch.model.event.gesture.WordSearchGamePanGestureListener;
import com.kooapps.wordsearch.model.event.main.EagerEventDispatcher;
import com.kooapps.wordsearch.model.event.main.EventListener;
import com.kooapps.wordsearch.view.adapter.WordListAdapter;
import com.kooapps.wordsearch.view.adapter.WordSearchAdapter;
import com.kooapps.wordsearch.view.layoutmanager.WordSearchGameLayoutManager;

public class MainActivity extends AppCompatActivity implements NoTimeRemainingListener {

    // DATA BINDING
    private ActivityMainBinding mBinding;

    // GAME
    private WordSearchGame mWordSearchGame;

    // ADAPTERS
    private WordSearchAdapter mWordSearchAdapter;
    private WordListAdapter mWordListAdapter;

    // GESTURES
    private PanGestureListener mPanGestureListener;
    private GestureDetector mGestureDetector;

    // RESULT
    private static final int RESULT = 35323;

    // EVENTS

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_MOVE:
                    mGestureDetector.onTouchEvent(event);

                    ConstraintLayout cellLayout = (ConstraintLayout) mBinding.wordGridRecyclerView.findChildViewUnder(event.getX(), event.getY());
                    if (cellLayout != null) {
                        int position = mBinding.wordGridRecyclerView.getChildLayoutPosition(cellLayout);
                        if (position >= 0) {
                            mWordSearchGame.captureSelectedCell(position);
                            return true;
                        }
                    }  else {
                        processSelectedCells();
                    }
                    return false;
                case MotionEvent.ACTION_UP:
                    processSelectedCells();
                    return true;
                default:

                    return false;
            }
        }
    };

    EventListener<EventRewardedAdDismissed> mEventRewardedAdDismissedEventListener = event -> {
        dismissAdLoadingDialog();
        if (AdHandler.getInstance().getCurrentRewardedAdmobLoader().isRewardEarned()) {
            mWordSearchGame.addReward();
        }
        AdHandler.getInstance().clearRewardedAdmobLoader();
    };

    EventListener<EventRewardedAdFailed> mEventRewardedAdFailedEventListener = event -> {
        dismissAdLoadingDialog();
        AdHandler.getInstance().clearRewardedAdmobLoader();
        if (mWordSearchGame.isRunning()) {
            mWordSearchGame.resumeGameTimer();
        }
    };

    EventListener<EventRewardedAdLoaded> mEventRewardedAdLoadedEventListener = new EventListener<EventRewardedAdLoaded>() {
        @Override
        public void onEvent(@NonNull EventRewardedAdLoaded event) {
            if (mWordSearchGame.isActive()) {
                dismissAdLoadingDialog();
                showRewardedAd();
            }
        }
    };

    //region ACTIVITY_LIFE_CYCLE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        RewardedAdmobLoader adLoader = AdHandler.getInstance().getCurrentRewardedAdmobLoader();
        if (adLoader == null ) {
            if (mWordSearchGame.isRunning()) {
                mWordSearchGame.resumeGameTimer();
            }
        } else {
            RewardedAdmobManager.executePendingAd(adLoader, this);
        }

        mWordSearchGame.setActive(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWordSearchGame.isRunning()) {
            mWordSearchGame.cancelGameTimer();
        }
        mWordSearchGame.setActive(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mWordSearchGame.setRunning(false);
            mBinding.wordGridRecyclerView.post(this::startGame);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyListeners();
    }

    //endregion ACTIVITY_LIFE_CYCLE

    //region INITIALIZATION

    public void init() {
        initData();
        initBinding();
        initListeners();
        initUIHelpers();
        if (mWordSearchGame.getWordGrid() != null) {
            mBinding.wordGridRecyclerView.post(this::initWordSearchAdapter);
            mBinding.wordListRecyclerView.post(this::initWordListAdapter);
        }
    }

    public void initData() {
        mWordSearchGame = WordSearchGameHandler.getInstance().getWordSearchGame();
    }

    public void initBinding() {
        mBinding.setWordSearchGame(mWordSearchGame);
    }

    public void initListeners() {
        initGestureListeners();
        initGameListeners();
        initAdListeners();
    }

    public void initGameListeners() {
        mWordSearchGame.setNoTimeRemainingListener(this);
    }

    public void initGestureListeners() {
        mBinding.wordGridRecyclerView.setOnTouchListener(mOnTouchListener);
    }

    public void initAdListeners() {
        EagerEventDispatcher.addListener(R.string.event_rewarded_ad_dismissed, mEventRewardedAdDismissedEventListener);
        EagerEventDispatcher.addListener(R.string.event_rewarded_ad_failed, mEventRewardedAdFailedEventListener);
        EagerEventDispatcher.addListener(R.string.event_rewarded_ad_loaded, mEventRewardedAdLoadedEventListener);
    }

    public void initUIHelpers() {
        mPanGestureListener = new WordSearchGamePanGestureListener();
        mGestureDetector = new GestureDetector(getApplicationContext(), mPanGestureListener);
    }

    public void initWordSearchAdapter() {
       mWordSearchAdapter = new WordSearchAdapter(mWordSearchGame, mBinding.wordGridRecyclerView.getMeasuredHeight());
       mBinding.wordGridRecyclerView.setAdapter(mWordSearchAdapter);
       WordSearchGameLayoutManager manager = new WordSearchGameLayoutManager(getApplicationContext(), mWordSearchGame.getGrid().getColumn());
       manager.setScrollable(false);
       mBinding.wordGridRecyclerView.setLayoutManager(manager);
    }

    public void initWordListAdapter() {
        mWordListAdapter = new WordListAdapter(mWordSearchGame.getWordGrid().getWordMapList(), mBinding.wordListRecyclerView.getMeasuredHeight());
        mBinding.wordListRecyclerView.setAdapter(mWordListAdapter);
        mBinding.wordListRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
    }

    //endregion INITIALIZATION

    //region ADAPTER_HELPER

    public void updateWordSearchAdapter() {
        if (mWordSearchAdapter == null) {
            initWordSearchAdapter();
        } else {
            mWordSearchAdapter.notifyDataSetChanged();
        }

    }

    public void updateWordListAdapter() {
        if (mWordListAdapter == null) {
            initWordListAdapter();
        } else {
            mWordListAdapter.notifyDataSetChanged();
        }
    }

    //endregion ADAPTER_HELPER

    //region BUTTON_ON_CLICK

    public void startOrStopGameButtonClicked(View view) {
        if (!mWordSearchGame.isRunning()) {
            startGame();
        } else {
            stopGame();
        }
    }

    public void watchAdButtonClicked(View view) {
        AdLoadingDialogFragment dialog = getAdLoadingDialog();

        if (mWordSearchGame.isRunning()) {
            mWordSearchGame.cancelGameTimer();
        }

        if (dialog == null) {
            showAdLoadingDialog();
        }

        if (AdHandler.getInstance().getCurrentRewardedAdmobLoader() == null) {
            AdHandler.getInstance().setRewardedAdmobLoader(RewardedAdmobManager.createLoader());
            AdHandler.getInstance().getCurrentRewardedAdmobLoader().setIsRequested(true);
            AdHandler.getInstance().getCurrentRewardedAdmobLoader().loadAd(this);
        }
    }

    public void buyHintButtonClicked(View view) {
        mWordSearchGame.buyHint();
    }

    //endregion BUTTON_ON_CLICK

    //region GAME_HELPERS

    public void startGame() {
        mWordSearchGame.startGame();
        updateWordSearchAdapter();
        updateWordListAdapter();
    }

    public void stopGame() {
        mWordSearchGame.stopGame();
        mWordSearchGame.addCoinBonus();
        showResultScreen();
    }

    public void processSelectedCells() {
        mWordSearchGame.processSelectedCells();
        if (mWordSearchGame.isLevelCleared()){
            stopGame();
        }
    }

    //endregion GAME_HELPERS

    //region AD

    public void showRewardedAd() {
        dismissAdLoadingDialog();
        AdHandler.getInstance().getCurrentRewardedAdmobLoader().showAd(this);
    }

    public void dismissAdLoadingDialog() {
        AdLoadingDialogFragment dialog = getAdLoadingDialog();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void showAdLoadingDialog() {
        AdLoadingDialogFragment dialog = new AdLoadingDialogFragment();
        dialog.show(getSupportFragmentManager(), AdLoadingDialogFragment.TAG);
    }

    public AdLoadingDialogFragment getAdLoadingDialog() {
        return (AdLoadingDialogFragment)getSupportFragmentManager().findFragmentByTag(AdLoadingDialogFragment.TAG);
    }

    //endregion AD

    //region RESULT

    public void showResultScreen() {
        Intent intent = new Intent();
        intent.setClass(this, ResultActivity.class);
        startActivityForResult(intent, RESULT);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onTimeLimitReached() {
        showResultScreen();
    }

    //endregion RESULT

    //region ON_DESTROY

    public void destroyListeners() {
        EagerEventDispatcher.removeListener(R.string.event_rewarded_ad_dismissed, mEventRewardedAdDismissedEventListener);
        EagerEventDispatcher.removeListener(R.string.event_rewarded_ad_failed, mEventRewardedAdFailedEventListener);
        EagerEventDispatcher.removeListener(R.string.event_rewarded_ad_loaded, mEventRewardedAdLoadedEventListener);
    }

    //endregion ON_DESTROY

}
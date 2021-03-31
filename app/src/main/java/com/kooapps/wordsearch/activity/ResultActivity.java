package com.kooapps.wordsearch.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kooapps.wordsearch.R;
import com.kooapps.wordsearch.databinding.ActivityResultBinding;
import com.kooapps.wordsearch.handler.WordSearchGameHandler;
import com.kooapps.wordsearch.model.core.main.WordSearchGame;
import com.kooapps.wordsearch.view.adapter.WordSearchResultAdapter;
import com.kooapps.wordsearch.view.layoutmanager.WordSearchGameLayoutManager;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding mBinding;

    private WordSearchGame mWordSearchGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_result);
        init();
    }

    @Override
    public void onBackPressed() {
        // nothing
    }

    public void init() {
        mBinding.resultGridRecyclerView.post(this::initData);
        mBinding.resultGridRecyclerView.post(this::initAdapters);
        initBinding();
    }

    public void initData() {
        mWordSearchGame = WordSearchGameHandler.getInstance().getWordSearchGame();
    }

    public void initAdapters() {
        WordSearchResultAdapter adapter =
                new WordSearchResultAdapter(mWordSearchGame, mBinding.resultGridRecyclerView.getMeasuredHeight());
        WordSearchGameLayoutManager layoutManager = new WordSearchGameLayoutManager(getApplicationContext(),
                mWordSearchGame.getGrid().getColumn());
        mBinding.resultGridRecyclerView.setAdapter(adapter);
        mBinding.resultGridRecyclerView.setLayoutManager(layoutManager);
    }

    public void initBinding() {
        mBinding.setGame(WordSearchGameHandler.getInstance().getWordSearchGame());
    }

    public void nextButtonClicked(View view) {
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
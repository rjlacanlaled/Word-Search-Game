package com.kooapps.wordsearch.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kooapps.wordsearch.databinding.LayoutWordSeachResultCellBinding;
import com.kooapps.wordsearch.databinding.LayoutWordSearchCellBinding;
import com.kooapps.wordsearch.model.core.helper.Cell;
import com.kooapps.wordsearch.model.core.main.WordSearchGame;

public class WordSearchResultAdapter extends RecyclerView.Adapter<WordSearchResultAdapter.ViewHolder> {

    private WordSearchGame mWordSearchGame;
    private int mParentHeight;

    public WordSearchResultAdapter(WordSearchGame wordSearchGame, int parentHeight) {
        mWordSearchGame = wordSearchGame;
        mParentHeight = parentHeight;
    }

    @NonNull
    @Override
    public WordSearchResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutWordSeachResultCellBinding binding = LayoutWordSeachResultCellBinding
                .inflate(LayoutInflater.from(parent.getContext()),parent, false);
        return new WordSearchResultAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WordSearchResultAdapter.ViewHolder holder, int position) {
        Cell cell = Cell.getCellLocation(position, mWordSearchGame.getGrid());
        holder.mBinding.setGame(mWordSearchGame);
        holder.mBinding.setWordGridCell(mWordSearchGame.getWordGrid().getGeneratedWordGrid().get(cell.getRow()).get(cell.getColumn()));
    }

    @Override
    public int getItemCount() {
        return mWordSearchGame.getGrid().getSize();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LayoutWordSeachResultCellBinding mBinding;

        public ViewHolder(@NonNull LayoutWordSeachResultCellBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            ViewGroup.LayoutParams params = binding.getRoot().getLayoutParams();
            params.height = mParentHeight / mWordSearchGame.getGrid().getRow();
            binding.getRoot().setLayoutParams(params);
        }
    }

}

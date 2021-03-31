package com.kooapps.wordsearch.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kooapps.wordsearch.databinding.LayoutWordSearchCellBinding;
import com.kooapps.wordsearch.model.core.helper.Cell;
import com.kooapps.wordsearch.model.core.main.WordSearchGame;

public class WordSearchAdapter extends RecyclerView.Adapter<WordSearchAdapter.ViewHolder>  {

    private WordSearchGame mWordSearchGame;
    private int mParentHeight;

    public WordSearchAdapter(WordSearchGame wordSearchGame, int parentHeight) {
        mWordSearchGame = wordSearchGame;
        mParentHeight = parentHeight;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutWordSearchCellBinding binding = LayoutWordSearchCellBinding
                .inflate(LayoutInflater.from(parent.getContext()),parent, false);
        return new WordSearchAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cell cell = Cell.getCellLocation(position, mWordSearchGame.getGrid());
        holder.mBinding.setWordGridCell(mWordSearchGame.getWordGrid().getGeneratedWordGrid().get(cell.getRow()).get(cell.getColumn()));

    }

    @Override
    public int getItemCount() {
        return mWordSearchGame.getGrid().getSize();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LayoutWordSearchCellBinding mBinding;

        public ViewHolder(@NonNull LayoutWordSearchCellBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            ViewGroup.LayoutParams params = binding.getRoot().getLayoutParams();
            params.height = mParentHeight / mWordSearchGame.getGrid().getRow();
            binding.getRoot().setLayoutParams(params);
        }
    }
}

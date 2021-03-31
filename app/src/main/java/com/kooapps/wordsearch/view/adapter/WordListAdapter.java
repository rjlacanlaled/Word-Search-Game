package com.kooapps.wordsearch.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kooapps.wordsearch.databinding.LayoutWordListItemBinding;
import com.kooapps.wordsearch.model.core.main.WordMap;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder> {

    private List<WordMap> mWordMapList;
    private int mParentHeight;

    public WordListAdapter(List<WordMap> wordMapList, int parentHeight) {
        mWordMapList = wordMapList;
        mParentHeight = parentHeight;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutWordListItemBinding binding = LayoutWordListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WordListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mWordMapList != null ) {
            holder.mBinding.setWordMap(mWordMapList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mWordMapList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LayoutWordListItemBinding mBinding;

        public ViewHolder(@NonNull LayoutWordListItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
             ViewGroup.LayoutParams params = binding.getRoot().getLayoutParams();
             params.height = mParentHeight / 4;
             binding.getRoot().setLayoutParams(params);
        }
    }
}

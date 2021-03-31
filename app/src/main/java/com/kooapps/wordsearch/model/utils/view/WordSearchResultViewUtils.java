package com.kooapps.wordsearch.model.utils.view;

import android.content.Context;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.BindingAdapter;

import com.kooapps.wordsearch.R;
import com.kooapps.wordsearch.model.core.helper.Cell;
import com.kooapps.wordsearch.model.core.main.WordGridCell;
import com.kooapps.wordsearch.model.core.main.WordMap;
import com.kooapps.wordsearch.model.core.main.WordSearchGame;


public class WordSearchResultViewUtils {

    @BindingAdapter({"android:earnedCoinsContext", "android:wordSearchGame"})
    public static void setEarnedCoinsText(TextView textView, Context context, WordSearchGame game) {
        textView.setText(String.format(context.getResources().getString(R.string.result_earned_coins_text), game.getWordBonus()));
    }

    @BindingAdapter({"android:bgColorContext", "android:wordSearchGame", "android:bgColorForWordGridCell"})
    public static void setBackgroundColorForCell(TextView textview,
                                                 Context context, WordSearchGame game,
                                                 WordGridCell wordGridCell) {

        if (wordGridCell.isFound()) {
            textview.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.found_cell_border, null));
        } else {
            boolean isPartOfWord = false;
            for (WordMap wordMap : game.getWordGrid().getWordMapList()) {
                for (Cell cell : wordMap.getWordLocation()) {
                    if (cell.getRow() == wordGridCell.getCell().getRow() &&
                            cell.getColumn() == wordGridCell.getCell().getColumn()) {
                        isPartOfWord = true;
                    }
                }
            }

            if (isPartOfWord) {
                textview.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.selected_cell_border, null));
            } else {
                textview.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.default_cell_border, null));
            }

        }
    }
}

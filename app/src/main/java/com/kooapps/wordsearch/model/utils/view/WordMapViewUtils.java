package com.kooapps.wordsearch.model.utils.view;

import android.content.Context;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.BindingAdapter;

import com.kooapps.wordsearch.R;
import com.kooapps.wordsearch.model.core.main.WordMap;
import com.kooapps.wordsearch.model.utils.animation.GameAnimationUtils;

import org.w3c.dom.Text;

public class WordMapViewUtils {

    private static final String WORD_NOT_FOUND_PLACE_HOLDER = "-";

    @BindingAdapter({"android:bgColorContext", "android:bgColorForSelected", "android:bgColorForFound"})
    public static void setBackgroundColorForCell(TextView textview, Context context, boolean isSelected, boolean isFound) {
        if (isSelected) {
            textview.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.selected_cell_border, null));
        } else {
            if (isFound) {
                textview.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.found_cell_border, null));
            } else {
                textview.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.default_cell_border, null));
            }
        }
    }

    @BindingAdapter({"android:wordMap", "android:textForState"})
    public static void setTextForWordGridListItem(TextView textView, WordMap wordMap, boolean isFound) {

        if (isFound) {
            if (!wordMap.isFoundAnimated()) {
                wordMap.setFoundAnimated(true);
                GameAnimationUtils.animateTextReveal(textView, wordMap.getWord(), 1500);
            } else {
                textView.setText(wordMap.getWord());
            }
        } else {
            StringBuilder hiddenWord = new StringBuilder();
            for (int i = 0; i < wordMap.getWord().length(); i++) {
                hiddenWord.append(WORD_NOT_FOUND_PLACE_HOLDER);
            }
            textView.setText(hiddenWord);
        }
    }

    @BindingAdapter({"android:hintContext", "android:hintVisible"})
    public static void setHintAnimation(ConstraintLayout constraintLayout, Context context, boolean isVisible) {
        if (isVisible) {
            GameAnimationUtils.bounceLayout(constraintLayout, context, 3000);
        }
    }
}

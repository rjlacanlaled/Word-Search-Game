package com.kooapps.wordsearch.model.utils.view;

import android.content.Context;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.kooapps.wordsearch.R;
import com.kooapps.wordsearch.model.utils.animation.GameAnimationUtils;

public class WordSearchGameViewUtils {

    public static float convertTimeToFloat(long time) {
        return time / 1000f;
    }

    public static String textForStartOrStopButton(Context context, boolean isRunning) {
        return isRunning ? context.getString(R.string.stop_game_text) :
                context.getString(R.string.start_game_text);
    }

    @BindingAdapter({"android:coinsContext", "android:textForCoin"})
    public static void setTextForCoins(TextView textView, Context context, int afterCoin) {
        String beforeCoinsString = textView.getText().toString();
        if (!beforeCoinsString.equals("")) {
            String stringNum = beforeCoinsString.replaceAll("\\D+","");
            int beforeCoin = Integer.parseInt(stringNum);
            GameAnimationUtils.animateCountTextView(textView, beforeCoin, afterCoin, 2500,
                    context.getString(R.string.coins_text));
        } else {
            textView.setText(String.format(context.getString(R.string.coins_text), afterCoin));
        }
    }

    @BindingAdapter({"android:timeRemainingContext", "android:textForTimeRemaining"})
    public static void setTextForTimeRemaining(TextView textView, Context context, long timeRemaining) {
        textView.setText(String.format(context.getResources().getString(R.string.time_remaining_text),
                convertTimeToFloat(timeRemaining)));
    }
}

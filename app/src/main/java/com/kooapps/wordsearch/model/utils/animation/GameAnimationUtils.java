package com.kooapps.wordsearch.model.utils.animation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.kooapps.wordsearch.R;

import org.w3c.dom.Text;

public class GameAnimationUtils {

    public static void overShootTextView(TextView textView, int originalColor, int animationColor, long duration) {
        textView.setTextColor(animationColor);
        textView.setScaleX(0.5f);
        textView.setScaleY(0.5f);
        textView.setAlpha(0f);
        textView.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(duration)
                .setInterpolator(new OvershootInterpolator())
                .withEndAction(() -> textView.setTextColor(originalColor));
    }

    public static void overShootLayout(ConstraintLayout layout, long duration) {
        layout.setScaleX(0.5f);
        layout.setScaleY(0.5f);
        layout.setAlpha(0f);
        layout.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(duration)
                .setInterpolator(new OvershootInterpolator());
    }

    public static void animateCountTextView(TextView textView, int from, int to, long duration, String formattedIntMessage) {
        ValueAnimator animator = ValueAnimator.ofInt(from, to);
        animator.setDuration(duration);
        animator.addUpdateListener(animation ->
                textView.setText(String.format(formattedIntMessage, animation.getAnimatedValue())));
        animator.start();
    }

    public static void animateTextReveal(TextView textview, String text, long duration) {
        StringBuilder builder = new StringBuilder(textview.getText().toString());
        ValueAnimator animator = ValueAnimator.ofInt(0, text.length() - 1);
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            int index = Integer.parseInt(animation.getAnimatedValue().toString());
            builder.setCharAt(index, text.charAt(index));
            textview.setText(builder.toString());
        });
        animator.start();
    }

    public static void bounceLayout(ConstraintLayout constraintLayout, Context context, long duration) {
        Animation animator = AnimationUtils.loadAnimation(context, R.anim.bounce_animation);
        animator.setDuration(duration);
        constraintLayout.startAnimation(animator);
    }

//    public static void rotateTextView(Context context, TextView textView, boolean clockWise, long duration) {
//        Animation animation = AnimationUtils
//                .loadAnimation(context, clockWise ? R.anim.rotate_clockwise : R.anim.rotate_counterclockwise);
//        animation.setDuration(duration);
//        textView.startAnimation(animation);
//    }
//
//    public static void shakeLayout(Context context, ConstraintLayout layout) {
//        Animation animation = AnimationUtils.loadAnimation(context, R.anim.shake);
//        layout.startAnimation(animation);
//    }

}

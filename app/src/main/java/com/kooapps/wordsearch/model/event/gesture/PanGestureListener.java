package com.kooapps.wordsearch.model.event.gesture;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class PanGestureListener extends GestureDetector.SimpleOnGestureListener {

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

}

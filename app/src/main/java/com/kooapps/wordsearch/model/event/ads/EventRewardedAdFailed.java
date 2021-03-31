package com.kooapps.wordsearch.model.event.ads;

import androidx.annotation.NonNull;

import com.kooapps.wordsearch.R;
import com.kooapps.wordsearch.model.event.main.Event;

public class EventRewardedAdFailed extends Event {

    @NonNull
    @Override
    public int getId() {
        return R.string.event_rewarded_ad_failed;
    }

}

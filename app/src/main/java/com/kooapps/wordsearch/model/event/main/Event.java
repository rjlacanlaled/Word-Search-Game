package com.kooapps.wordsearch.model.event.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class Event<S, U> {

    @Nullable
    private final S mSource;
    @Nullable private final U mUserInfo;

    @NonNull
    public abstract int getId();

    @Nullable
    public S getSource() {
        return mSource;
    }

    @Nullable
    public U getUserInfo() {
        return mUserInfo;
    }

    public Event() {
        mSource = null;
        mUserInfo = null;
    }

    public Event(@Nullable S source, @Nullable U userInfo) {
        mSource = source;
        mUserInfo = userInfo;
    }
}

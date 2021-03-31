package com.kooapps.wordsearch.handler;

import android.content.Context;

import androidx.databinding.BaseObservable;

import com.google.android.gms.ads.MobileAds;
import com.kooapps.wordsearch.ads.main.admob.RewardedAdmobLoader;

public class AdHandler {
    private static final AdHandler sAdHandler = new AdHandler();
    private static boolean sInitialized;
    private RewardedAdmobLoader mCurrentRewardedAdmobLoader;

    public static AdHandler getInstance() {
        return sAdHandler;
    }

    public void init(Context context) {
        if (sInitialized) {
            return;
        }

        new Thread(() -> {
            MobileAds.initialize(context);
            sInitialized = true;
        });
    }

    public void setRewardedAdmobLoader(RewardedAdmobLoader rewardedAdmobLoader) {
        mCurrentRewardedAdmobLoader = rewardedAdmobLoader;
    }

    public RewardedAdmobLoader getCurrentRewardedAdmobLoader() {
        return mCurrentRewardedAdmobLoader;
    }

    public void clearRewardedAdmobLoader() {
        mCurrentRewardedAdmobLoader = null;
    }
}

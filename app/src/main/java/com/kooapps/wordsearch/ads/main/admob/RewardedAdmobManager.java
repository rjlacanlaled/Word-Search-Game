package com.kooapps.wordsearch.ads.main.admob;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

public class RewardedAdmobManager {

    public static RewardedAdmobLoader createLoader() {
        return new RewardedAdmobLoader();
    }

    public static void executePendingAd(RewardedAdmobLoader loader, Activity activity) {
        if (loader.isRequested()) {
            if (loader.hasLoaded()) {
                if (!loader.isShown()) {
                    loader.setupAd();
                    loader.showAd(activity);
                }
            } else {
                loader.loadAd(activity);
            }
        }
    }
}

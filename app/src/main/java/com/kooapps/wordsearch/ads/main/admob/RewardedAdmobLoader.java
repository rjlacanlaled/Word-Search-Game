package com.kooapps.wordsearch.ads.main.admob;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.kooapps.wordsearch.model.event.ads.EventRewardedAdDismissed;
import com.kooapps.wordsearch.model.event.ads.EventRewardedAdFailed;
import com.kooapps.wordsearch.model.event.ads.EventRewardedAdLoaded;
import com.kooapps.wordsearch.model.event.main.EagerEventDispatcher;


public class RewardedAdmobLoader implements AdmobLoader {

    private static final String UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
    private RewardedAd mRewardedAd;
    private boolean mIsRequested;
    private boolean mHasLoaded;
    private boolean mIsShown;
    private boolean mLoading;
    private boolean mRewardEarned;

    //region GETTERS/SETTERS

    public RewardedAd getRewardedAd() {
        return mRewardedAd;
    }

    public boolean hasLoaded() {
        return mHasLoaded;
    }

    public void setHasLoaded(boolean hasLoaded) {
        mHasLoaded = hasLoaded;
    }

    public boolean isShown() {
        return mIsShown;
    }

    public void setIsShown(boolean mIsShown) {
        this.mIsShown = mIsShown;
    }

    public boolean isRequested() {
        return mIsRequested;
    }

    public void setIsRequested(boolean mIsRequested) {
        this.mIsRequested = mIsRequested;
    }

    public boolean isLoading() {
        return mLoading;
    }

    public void setLoading(boolean mLoading) {
        this.mLoading = mLoading;
    }

    public boolean isRewardEarned() {
        return mRewardEarned;
    }

    public void setRewardEarned(boolean mRewardEarned) {
        this.mRewardEarned = mRewardEarned;
    }

    //endregion GETTERS/SETTERS

    //region ABSTRACT_METHOD_IMPLEMENTATION

    @Override
    public void loadAd(Activity activity) {
        if (!isLoading() && !hasLoaded()) {
            RewardedAd.load(activity.getApplicationContext(), UNIT_ID,
                    new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            super.onAdLoaded(rewardedAd);
                            mRewardedAd = rewardedAd;
                            setHasLoaded(true);
                            setLoading(false);
                            setupAd();
                            EagerEventDispatcher.dispatch(new EventRewardedAdLoaded());
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            setLoading(false);
                            EagerEventDispatcher.dispatch(new EventRewardedAdFailed());
                        }
                    });
        }
    }

    @Override
    public void setupAd() {
        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                 EagerEventDispatcher.dispatch(new EventRewardedAdDismissed());
            }
        });
    }

    @Override
    public void showAd(Activity activity) {
        mRewardedAd.show(activity, rewardItem -> mRewardEarned = true);
    }

    //endregion ABSTRACT_METHOD_IMPLEMENTATION
}

package com.example.weatherapplication.Classes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.weatherapplication.BuildConfig;
import com.example.weatherapplication.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.Map;

public class AdsManager {
    private static AdsManager instance;
    //private InterstitialAd mInterstitialAd;
//    int loadAdCounter = 0;
    String interstid;
//    int showAdCounter = 0;
    private InterstitialAd mInterstitialAd;
    public static NativeAd nativeAd;

    private int counter = 0;

    public static AdsManager getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new AdsManager();
        return instance;
    }

    public void initializeAds(Context context) {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                for (String adapterClass : statusMap.keySet()) {
                    AdapterStatus status = statusMap.get(adapterClass);
                    Log.d("MyApp", String.format(
                            "Adapter name: %s, Description: %s, Latency: %d",
                            adapterClass, status.getDescription(), status.getLatency()));
                }

            }
        });
    }
    public static String getPref(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        return preferences.getString(key, null);
    }
    public void loadBanner(Activity activity, Context context, LinearLayout linearLayout) {
        AdView adView = new AdView(context);
//        if (BuildConfig.DEBUG) {
//            adView.setAdUnitId(Ids.AD_MOB_BANNER_TEST);
//        } else {
String bid=getPref("banner", activity.getApplication());
        adView.setAdUnitId(bid);
//        }

        linearLayout.removeAllViews();
        linearLayout.addView(adView);
        AdSize adSize = getAdSize(context, activity, linearLayout);
        adView.setAdSize(adSize);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public void loadBannerSplash(Activity activity, Context context, LinearLayout linearLayout, ImageButton imageButton) {
        AdView adView = new AdView(context);
        if (BuildConfig.DEBUG) {
            adView.setAdUnitId(AdId.AD_MOB_BANNER_TEST);
        } else {
        String bid=getPref("banner", activity.getApplication());
         interstid=getPref("interst", activity.getApplication());

    }

//        lottieAnimationView.setVisibility(View.VISIBLE);
//        lottieAnimationView.playAnimation();

        linearLayout.removeAllViews();
        linearLayout.addView(adView);
        AdSize adSize = getAdSize(context, activity, linearLayout);
        adView.setAdSize(adSize);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
//                lottieAnimationView.setVisibility(View.GONE);
//                lottieAnimationView.pauseAnimation();
                imageButton.setVisibility(View.VISIBLE);
               // Toast.makeText(activity, "loaded", Toast.LENGTH_SHORT).show();
                Log.e("AdsManager", "Loadded Banner: ");
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
//                lottieAnimationView.setVisibility(View.GONE);
//                lottieAnimationView.pauseAnimation();
                imageButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.e("AdsManager", "Failed Banner: " + loadAdError);
//                lottieAnimationView.setVisibility(View.GONE);
//                lottieAnimationView.pauseAnimation();
                imageButton.setVisibility(View.VISIBLE);
            }

        });
    }

    private AdSize getAdSize(Context context, Activity activity, LinearLayout linearLayout) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float desnsity = displayMetrics.density;
        float adWidthPixels = linearLayout.getWidth();
        if (adWidthPixels == 0) {
            adWidthPixels = displayMetrics.widthPixels;
        }
        int adWidth = (int) (adWidthPixels / desnsity);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }


    public void loadInterstitialAds(Context context) {
        AdRequest adRequest = new AdRequest.Builder().build();
        String adId;
//        if (BuildConfig.DEBUG) {
//            adId = Ids.AD_MOB_INTERSTITIAL_TEST;
//        } else {
        adId = interstid;
//        }
        InterstitialAd.load(context, adId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;Log.d("Ads","Loaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mInterstitialAd = null;
                Log.d("Ads","Failes"+loadAdError.getMessage());
            }
        });
    }

    public void showInterstitialAds(Activity activity, Context context) {
        if (mInterstitialAd != null) {
            mInterstitialAd.show((Activity) context);
            counter++;

            this.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    loadInterstitialAds(context);
                }
            });
            return;
        }

        loadInterstitialAds(context);
    }

    public void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        // Set the media view.
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView)  adView.getHeadlineView()).setText(nativeAd.getHeadline());adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAd);


    }

    public void loadNativeBanner(Activity activity, Context context) {
        AdLoader.Builder builder;
        if (BuildConfig.DEBUG) {
            builder = new AdLoader.Builder(context, AdId.Test_Admob_Native_ID);
        } else {
//        String nativeid=getPref("native", activity.getApplication());
       String nativeid="ca-app-pub-7956328597641937/8720032157";
        builder = new AdLoader.Builder(context, nativeid);
      }
        builder.forNativeAd(nativeAd -> {
            boolean isDestroyed = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                isDestroyed = activity.isDestroyed();
            }
            if (isDestroyed || activity.isFinishing() || activity.isChangingConfigurations()) {
                nativeAd.destroy();
                return;
            }
            if (AdsManager.nativeAd != null) {
                AdsManager.nativeAd.destroy();
            }
            AdsManager.nativeAd = nativeAd;
        });

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                @SuppressLint("DefaultLocale") String error = String.format("domain: %s, code: %d, message: %s", loadAdError.getDomain(),
                        loadAdError.getCode(), loadAdError.getMessage());
                Log.e("TAG", "loadNative: " + error);
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void showNativeBanner(Context context, Activity activity, FrameLayout frameLayout) {
        if (AdsManager.nativeAd != null) {
            NativeAdView adView =
                    (NativeAdView) activity.getLayoutInflater().inflate(R.layout.custom_native_banner_layout, null);
            populateNativeAdView(nativeAd, adView);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
        } else {
            loadNativeBanner(activity, context);
        }
    }
    public void showNativeBanner2(Context context, Activity activity, FrameLayout frameLayout) {
        if (AdsManager.nativeAd != null) {
            NativeAdView adView =
                    (NativeAdView) activity.getLayoutInflater().inflate(R.layout.custom_native_banner_layout2, null);
            populateNativeAdView(nativeAd, adView);

            frameLayout.removeAllViews();
            frameLayout.addView(adView);
        } else {
            loadNativeBanner(activity, context);
        }
    }
}

//    public static NativeAd nativeAd;
//    public AdLoader admobNativeadLoader;

//    public static AdsManager getInstance() {
//        if (instance != null) {
//            return instance;
//        }
//        instance = new AdsManager();
//        return instance;
//    }
//
//    public void initializeAds(Context context) {
//        MobileAds.initialize(context, initializationStatus -> {
//
//        });
//    }
//
//    public void loadBanner(Activity activity, Context context, LinearLayout linearLayout) {
//        AdView adView = new AdView(context);
//        if (BuildConfig.DEBUG) {
////            Toast.makeText(activity, "debug", Toast.LENGTH_SHORT).show();
//            adView.setAdUnitId(AdId.AD_MOB_BANNER_TEST);
//        } else {
////            Toast.makeText(activity, "no debug", Toast.LENGTH_SHORT).show();
//            adView.setAdUnitId(AdId.AD_MOB_BANNER_REAL);
//        }
//
//        linearLayout.removeAllViews();
//        linearLayout.addView(adView);
//        AdSize adSize = getAdSize(context, activity, linearLayout);
//        adView.setAdSize(adSize);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);
//        adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//                Log.e("AdsManager", "Failed Banner: " + loadAdError);
//            }
//        });
//    }
//
//    public void loadBannerSplash(Activity activity, Context context, LinearLayout linearLayout, ImageButton imageButton, LottieAnimationView lottieAnimationView) {
//        AdView adView = new AdView(context);
//        if (BuildConfig.DEBUG) {
//            adView.setAdUnitId(AdId.AD_MOB_BANNER_TEST);
//        } else {
//            adView.setAdUnitId(AdId.AD_MOB_BANNER_REAL);
//        }
//        lottieAnimationView.setVisibility(View.VISIBLE);
//        lottieAnimationView.playAnimation();
//
//        linearLayout.removeAllViews();
//        linearLayout.addView(adView);
//        AdSize adSize = getAdSize(context, activity, linearLayout);
//        adView.setAdSize(adSize);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);
//        adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                lottieAnimationView.pauseAnimation();
//                lottieAnimationView.setVisibility(View.GONE);
//                imageButton.setVisibility(View.VISIBLE);
//                Log.e("AdsManager", "Loadded Banner: " );
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//                Log.e("AdsManager", "Failed Banner: " + loadAdError);
//                lottieAnimationView.pauseAnimation();
//                lottieAnimationView.setVisibility(View.GONE);
//                imageButton.setVisibility(View.VISIBLE);
//            }
//
//        });
//    }
//
//
//    private AdSize getAdSize(Context context, Activity activity, LinearLayout linearLayout) {
//        Display display = activity.getWindowManager().getDefaultDisplay();
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        display.getMetrics(displayMetrics);
//        float desnsity = displayMetrics.density;
//        float adWidthPixels = linearLayout.getWidth();
//        if (adWidthPixels == 0) {
//            adWidthPixels = displayMetrics.widthPixels;
//        }
//        int adWidth = (int) (adWidthPixels / desnsity);
//        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
//    }
//
//
//    public void loadInterstitialAds(Context context) {
//        AdRequest adRequest = new AdRequest.Builder().build();
//        String adId;
//        if (BuildConfig.DEBUG) {
//            adId = AdId.AD_MOB_INTERSTITIAL_TEST;
//        } else {
//            adId = AdId.AD_MOB_INTERSTITIAL_REAL;
//        }
//        InterstitialAd.load(context, adId, adRequest, new InterstitialAdLoadCallback() {
//            @Override
//            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                super.onAdLoaded(interstitialAd);
//                mInterstitialAd = interstitialAd;
//                loadAdCounter = 1;
//                Log.e("AdsManager", "onAdLoaded: ");
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//                mInterstitialAd = null;
//                Log.e("AdsManager", "Failed Interstitial: " + loadAdError.getMessage());
//
//
//            }
//        });
//    }
//
//    public void showInterstitialAds(Activity activity, Context context) {
//        if (showAdCounter > 0) {
//            showAdCounter--;
//            if (loadAdCounter > 0) {
//                loadAdCounter--;
//            } else {
//                loadInterstitialAds(context);
//            }
//        } else {
//            if (this.mInterstitialAd != null) {
//                this.mInterstitialAd.show(activity);
//                this.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                    @Override
//                    public void onAdDismissedFullScreenContent() {
//                        Log.e("AdsManager", "onAdShown: " + loadAdCounter);
//                        showAdCounter = 2;
//                    }
//                });
//            }
//        }
//    }
//    public void populateNativeBannerAd(NativeAd nativeAd, NativeAdView adView) {
//
//        // Set the media view.
//        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
//
//        // Set other ad assets.
//        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
//        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
//        adView.setIconView(adView.findViewById(R.id.ad_icon));
//
//
//        // The headline and mediaContent are guaranteed to be in every NativeAd.
//        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
//        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
//
//        // These assets aren't guaranteed to be in every NativeAd, so it's important to
//        // check before trying to display them.
//
//        if (nativeAd.getCallToAction() == null) {
//            adView.getCallToActionView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getCallToActionView().setVisibility(View.VISIBLE);
//            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
//        }
//
//        if (nativeAd.getIcon() == null) {
//            adView.getIconView().setVisibility(View.GONE);
//        } else {
//            ((ImageView) adView.getIconView()).setImageDrawable(
//                    nativeAd.getIcon().getDrawable());
//            adView.getIconView().setVisibility(View.VISIBLE);
//        }
//
//        // This method tells the Google Mobile Ads SDK that you have finished populating your
//        // native ad view with this native ad.
//        adView.setNativeAd(nativeAd);
//
//    }
//    public void loadNativeBanner(Activity activity, Context context) {
//        AdLoader.Builder builder;
//        if (BuildConfig.DEBUG) {
//            builder = new AdLoader.Builder(context, AdId.Test_Admob_Native_ID);
//        } else {
//            builder = new AdLoader.Builder(context, AdId.Admob_Native_ID);
//        }
//        builder.forNativeAd(nativeAd -> {
//            boolean isDestroyed = false;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                isDestroyed = activity.isDestroyed();
//            }
//            if (isDestroyed || activity.isFinishing() || activity.isChangingConfigurations()) {
//                nativeAd.destroy();
//                return;
//            }
//            if (AdsManager.nativeAd != null) {
//                AdsManager.nativeAd.destroy();
//            }
//            AdsManager.nativeAd = nativeAd;
//        });
//
//        AdLoader adLoader = builder.withAdListener(new AdListener() {
//            @Override
//            public void onAdFailedToLoad(LoadAdError loadAdError) {
//                String error = String.format("domain: %s, code: %d, message: %s", loadAdError.getDomain(),
//                        loadAdError.getCode(), loadAdError.getMessage());
//                Log.e("AdsManager", "loadNative: " + error);
//
//            }
//        }).build();
//        adLoader.loadAd(new AdRequest.Builder().build());
//    }
//
//    public void showNativeBanner(Context context, Activity activity, FrameLayout frameLayout) {
//        if (AdsManager.nativeAd != null) {
//            NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.custom_native_banner_layout, null);
//            populateNativeBannerAd(nativeAd, adView);
//            frameLayout.removeAllViews();
//            frameLayout.addView(adView);
//        } else {
//            loadNativeBanner(activity, context);
//        }
//    }



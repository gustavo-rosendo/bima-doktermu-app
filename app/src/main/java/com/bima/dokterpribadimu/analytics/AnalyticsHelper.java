package com.bima.dokterpribadimu.analytics;

import android.os.Bundle;
import android.util.Log;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.google.android.gms.analytics.HitBuilders;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gustavo.santos on 7/19/2016.
 */
public class AnalyticsHelper {

    private static final String TAG = AnalyticsHelper.class.getSimpleName();

    // Flags to enable/disable Firebase/GoogleAnalytics
    private static final boolean FIREBASE           = false;
    private static final boolean GOOGLE_ANALYTICS   = true;

    public static void logViewScreenEvent(String screenName) {
        // [START view_screen event]
        Log.d(TAG, "Analytics log: logViewScreenEvent(screenName=" + screenName + ")");
        if(FIREBASE) {
            Bundle bundle = new Bundle();
            bundle.putString(EventConstants.PARAM_EVENT_NAME, screenName);
            DokterPribadimuApplication.getInstance()
                    .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_VIEW_SCREEN, bundle);
        }
        if(GOOGLE_ANALYTICS) {
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().setScreenName(screenName);
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().send(new HitBuilders.ScreenViewBuilder().build());
        }
        // [END view_screen event]
    }

    public static void logViewScreenNewsEvent(String screenName, String newsCategory) {
        // [START view_screen event]
        Log.d(TAG, "Analytics log: logViewScreenNewsEvent(screenName=" + screenName + ", "
                + "newsCategory=" + newsCategory + ")");
        if(FIREBASE) {
            Bundle bundle = new Bundle();
            bundle.putString(EventConstants.PARAM_EVENT_NAME, screenName);
            bundle.putString(EventConstants.PARAM_NEWS_CATEGORY, newsCategory);
            DokterPribadimuApplication.getInstance()
                    .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_VIEW_SCREEN, bundle);
        }
        if(GOOGLE_ANALYTICS) {
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().setScreenName(screenName);
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().send(new HitBuilders.ScreenViewBuilder().
                            set(EventConstants.PARAM_NEWS_CATEGORY, newsCategory).build());
        }
        // [END view_screen event]
    }

    public static void logViewDialogEvent(String dialogName) {
        // [START view_dialog event]
        Log.d(TAG, "Analytics log: logViewDialogEvent(dialogName=" + dialogName + ")");
        if(FIREBASE) {
            Bundle bundle = new Bundle();
            bundle.putString(EventConstants.PARAM_EVENT_NAME, dialogName);
            DokterPribadimuApplication.getInstance()
                    .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_VIEW_DIALOG, bundle);
        }
        if(GOOGLE_ANALYTICS) {
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().setScreenName(dialogName);
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().send(new HitBuilders.ScreenViewBuilder().build());
        }
        // [END view_dialog event]
    }

    public static void logViewDialogRatingEvent(String dialogName, String rating) {
        // [START view_dialog event]
        Log.d(TAG, "Analytics log: logViewDialogRatingEvent(dialogName=" + dialogName + ", rating=" + rating + ")");
        if(FIREBASE) {
            Bundle bundle = new Bundle();
            bundle.putString(EventConstants.PARAM_EVENT_NAME, dialogName);
            bundle.putString(EventConstants.PARAM_RATING, rating);
            DokterPribadimuApplication.getInstance()
                    .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_VIEW_DIALOG, bundle);
        }
        if(GOOGLE_ANALYTICS) {
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().setScreenName(dialogName);
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().send(new HitBuilders.ScreenViewBuilder().
                            set(EventConstants.PARAM_RATING, rating).build());
        }
        // [END view_dialog event]
    }

    public static void logViewDialogFailedEvent(String dialogName, String message) {
        // [START view_dialog event]
        Log.d(TAG, "Analytics log: logViewDialogFailedEvent(dialogName=" + dialogName + ", message=" + message + ")");
        if(FIREBASE) {
            Bundle bundle = new Bundle();
            bundle.putString(EventConstants.PARAM_EVENT_NAME, dialogName);
            bundle.putString(EventConstants.PARAM_MESSAGE, message);
            DokterPribadimuApplication.getInstance()
                    .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_VIEW_DIALOG, bundle);
        }
        if(GOOGLE_ANALYTICS) {
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().setScreenName(dialogName);
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().send(new HitBuilders.ScreenViewBuilder().
                            set(EventConstants.PARAM_MESSAGE, message).build());
        }
        // [END view_dialog event]
    }

    public static void logViewWindowEvent(String windowName) {
        // [START view_window event]
        Log.d(TAG, "Analytics log: logViewWindowEvent(windowName=" + windowName + ")");
        if(FIREBASE) {
            Bundle bundle = new Bundle();
            bundle.putString(EventConstants.PARAM_EVENT_NAME, windowName);
            DokterPribadimuApplication.getInstance()
                    .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_VIEW_WINDOW, bundle);
        }
        if(GOOGLE_ANALYTICS) {
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().setScreenName(windowName);
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().send(new HitBuilders.ScreenViewBuilder().build());
        }
        // [END view_window event]
    }

    public static void logButtonClickEvent(String buttonName) {
        // [START button_click event]
        Log.d(TAG, "Analytics log: logButtonClickEvent(buttonName=" + buttonName + ")");
        if(FIREBASE) {
            Bundle bundle = new Bundle();
            bundle.putString(EventConstants.PARAM_EVENT_NAME, buttonName);
            DokterPribadimuApplication.getInstance()
                    .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_BUTTON_CLICK, bundle);
        }
        if(GOOGLE_ANALYTICS) {
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().send(
                        new HitBuilders.EventBuilder()
                                .setCategory(EventConstants.CATEGORY_ACTION)
                                .setAction(EventConstants.TYPE_BUTTON_CLICK)
                                .setLabel(buttonName)
                                .build());
        }
        // [END button_click event]
    }

    public static void logBookCallBtnClickEvent(String buttonName, String callTopic, String callSubtopic) {
        // [START button_click event]
        Log.d(TAG, "Analytics log: logBookCallBtnClickEvent(buttonName=" + buttonName + ", "
                + "callTopic=" + callTopic + ", callSubtopic=" + callSubtopic + ")");
        if(FIREBASE) {
            Bundle bundle = new Bundle();
            bundle.putString(EventConstants.PARAM_EVENT_NAME, buttonName);
            bundle.putString(EventConstants.PARAM_CALL_TOPIC, callTopic);
            bundle.putString(EventConstants.PARAM_CALL_SUBTOPIC, callSubtopic);
            DokterPribadimuApplication.getInstance()
                    .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_BUTTON_CLICK, bundle);
        }
        if(GOOGLE_ANALYTICS) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(EventConstants.PARAM_CALL_TOPIC, callTopic);
            params.put(EventConstants.PARAM_CALL_SUBTOPIC, callSubtopic);

            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().send(
                        new HitBuilders.EventBuilder()
                                .setCategory(EventConstants.CATEGORY_ACTION)
                                .setAction(EventConstants.TYPE_BUTTON_CLICK)
                                .setLabel(buttonName)
                                .setAll(params)
                                .build());
        }
        // [END button_click event]
    }

    public static void logCategoryBtnClickEvent(String buttonName, String categoryName) {
        // [START button_click event]
        Log.d(TAG, "Analytics log: logCategoryBtnClickEvent(buttonName=" + buttonName + ", "
                + "categoryName=" + categoryName + ")");
        if(FIREBASE) {
            Bundle bundle = new Bundle();
            bundle.putString(EventConstants.PARAM_EVENT_NAME, buttonName);
            bundle.putString(EventConstants.PARAM_CATEGORY, categoryName);
            DokterPribadimuApplication.getInstance()
                    .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_BUTTON_CLICK, bundle);
        }
        if(GOOGLE_ANALYTICS) {
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().send(
                    new HitBuilders.EventBuilder()
                            .setCategory(EventConstants.CATEGORY_ACTION)
                            .setAction(EventConstants.TYPE_BUTTON_CLICK)
                            .setLabel(buttonName)
                            .set(EventConstants.PARAM_CATEGORY, categoryName)
                            .build());
        }
        // [END button_click event]
    }

    public static void logSearchEvent(String screenName, String keyword) {
        // [START search event]
        Log.d(TAG, "Analytics log: logSearchEvent(screenName=" + screenName + ", " + "keyword=" + keyword + ")");
        if(FIREBASE) {
            Bundle bundle = new Bundle();
            bundle.putString(EventConstants.PARAM_EVENT_NAME, screenName);
            bundle.putString(EventConstants.PARAM_SEARCH_KEYWORD, keyword);
            DokterPribadimuApplication.getInstance()
                    .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_SEARCH, bundle);
        }
        if(GOOGLE_ANALYTICS) {
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().send(
                    new HitBuilders.EventBuilder()
                            .setCategory(EventConstants.CATEGORY_ACTION)
                            .setAction(EventConstants.TYPE_SEARCH)
                            .setLabel(screenName)
                            .set(EventConstants.PARAM_SEARCH_KEYWORD, keyword)
                            .build());
        }
        // [END search event]
    }

    public static void logItemViewEvent(String screenName, String newsCategory, String title, String date) {
        // [START item_view event]
        Log.d(TAG, "Analytics log: logItemViewEvent(screenName=" + screenName + ", "
                + "newsCategory=" + newsCategory + ", " + "title=" + title + ", " + "date="+ date + ")");
        if(FIREBASE) {
            Bundle bundle = new Bundle();
            bundle.putString(EventConstants.PARAM_EVENT_NAME, screenName);
            bundle.putString(EventConstants.PARAM_NEWS_CATEGORY, newsCategory);
            bundle.putString(EventConstants.PARAM_NEWS_TITLE, title);
            bundle.putString(EventConstants.PARAM_NEWS_DATE, date);
            DokterPribadimuApplication.getInstance()
                    .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_ITEM_VIEW, bundle);
        }
        if(GOOGLE_ANALYTICS) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(EventConstants.PARAM_NEWS_CATEGORY, newsCategory);
            params.put(EventConstants.PARAM_NEWS_TITLE, title);
            params.put(EventConstants.PARAM_NEWS_DATE, date);

            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().send(
                    new HitBuilders.EventBuilder()
                            .setCategory(EventConstants.CATEGORY_ACTION)
                            .setAction(EventConstants.TYPE_ITEM_VIEW)
                            .setLabel(screenName)
                            .setAll(params)
                            .build());
        }
        // [END item_view event]
    }
}

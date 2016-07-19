package com.bima.dokterpribadimu.analytics;

import android.os.Bundle;
import android.util.Log;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by gustavo.santos on 7/19/2016.
 */
public class FirebaseAnalyticsHelper {

    private static final String TAG = FirebaseAnalyticsHelper.class.getSimpleName();

    public static void logViewScreenEvent(String screenName) {
        // [START view_screen event]
        Log.d(TAG, "Analytics log: logViewScreenEvent(screenName=" + screenName + ")");
        Bundle bundle = new Bundle();
        bundle.putString(EventConstants.PARAM_EVENT_NAME, screenName);
        DokterPribadimuApplication.getInstance()
                .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_VIEW_SCREEN, bundle);
        // [END view_screen event]
    }

    public static void logViewDialogEvent(String dialogName) {
        // [START view_dialog event]
        Log.d(TAG, "Analytics log: logViewDialogEvent(dialogName=" + dialogName + ")");
        Bundle bundle = new Bundle();
        bundle.putString(EventConstants.PARAM_EVENT_NAME, dialogName);
        DokterPribadimuApplication.getInstance()
                .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_VIEW_DIALOG, bundle);
        // [END view_dialog event]
    }

    public static void logViewDialogFailedEvent(String dialogName, String message) {
        // [START view_dialog event]
        Log.d(TAG, "Analytics log: logViewDialogFailedEvent(dialogName=" + dialogName + ", message=" + message + ")");
        Bundle bundle = new Bundle();
        bundle.putString(EventConstants.PARAM_EVENT_NAME, dialogName);
        bundle.putString(EventConstants.PARAM_MESSAGE, message);
        DokterPribadimuApplication.getInstance()
                .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_VIEW_DIALOG, bundle);
        // [END view_dialog event]
    }

    public static void logViewWindowEvent(String windowName) {
        // [START view_window event]
        Log.d(TAG, "Analytics log: logViewWindowEvent(windowName=" + windowName + ")");
        Bundle bundle = new Bundle();
        bundle.putString(EventConstants.PARAM_EVENT_NAME, windowName);
        DokterPribadimuApplication.getInstance()
                .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_VIEW_WINDOW, bundle);
        // [END view_window event]
    }

    public static void logButtonClickEvent(String buttonName) {
        // [START button_click event]
        Log.d(TAG, "Analytics log: logButtonClickEvent(buttonName=" + buttonName + ")");
        Bundle bundle = new Bundle();
        bundle.putString(EventConstants.PARAM_EVENT_NAME, buttonName);
        DokterPribadimuApplication.getInstance()
                .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_BUTTON_CLICK, bundle);
        // [END button_click event]
    }

    public static void logBookCallBtnClickEvent(String buttonName, String callTopic, String callSubtopic) {
        // [START button_click event]
        Log.d(TAG, "Analytics log: logBookCallBtnClickEvent(buttonName=" + buttonName + ", "
                + "callTopic=" + callTopic + ", callSubtopic=" + callSubtopic + ")");
        Bundle bundle = new Bundle();
        bundle.putString(EventConstants.PARAM_EVENT_NAME, buttonName);
        bundle.putString(EventConstants.PARAM_CALL_TOPIC, callTopic);
        bundle.putString(EventConstants.PARAM_CALL_SUBTOPIC, callSubtopic);
        DokterPribadimuApplication.getInstance()
                .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_BUTTON_CLICK, bundle);
        // [END button_click event]
    }

    public static void logCategoryBtnClickEvent(String buttonName, String categoryName) {
        // [START button_click event]
        Log.d(TAG, "Analytics log: logCategoryBtnClickEvent(buttonName=" + buttonName + ", "
                + "categoryName=" + categoryName + ")");
        Bundle bundle = new Bundle();
        bundle.putString(EventConstants.PARAM_EVENT_NAME, buttonName);
        bundle.putString(EventConstants.PARAM_CATEGORY, categoryName);
        DokterPribadimuApplication.getInstance()
                .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_BUTTON_CLICK, bundle);
        // [END button_click event]
    }

    public static void logSearchEvent(String screenName, String keyword) {
        // [START search event]
        Log.d(TAG, "Analytics log: logSearchEvent(screenName=" + screenName + ", " + "keyword=" + keyword + ")");
        Bundle bundle = new Bundle();
        bundle.putString(EventConstants.PARAM_EVENT_NAME, screenName);
        bundle.putString(EventConstants.PARAM_SEARCH_KEYWORD, keyword);
        DokterPribadimuApplication.getInstance()
                .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_SEARCH, bundle);
        // [END search event]
    }

    public static void logItemViewEvent(String screenName, String newsCategory, String title, String date) {
        // [START item_view event]
        Log.d(TAG, "Analytics log: logItemViewEvent(screenName=" + screenName + ", "
                + "newsCategory=" + newsCategory + ", " + "title=" + title + ", " + "date="+ date + ")");
        Bundle bundle = new Bundle();
        bundle.putString(EventConstants.PARAM_EVENT_NAME, screenName);
        bundle.putString(EventConstants.PARAM_NEWS_CATEGORY, newsCategory);
        bundle.putString(EventConstants.PARAM_NEWS_TITLE, title);
        bundle.putString(EventConstants.PARAM_NEWS_DATE, date);
        DokterPribadimuApplication.getInstance()
                .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_ITEM_VIEW, bundle);
        // [END item_view event]
    }
}

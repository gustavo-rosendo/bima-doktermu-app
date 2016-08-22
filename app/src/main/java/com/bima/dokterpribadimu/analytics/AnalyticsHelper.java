package com.bima.dokterpribadimu.analytics;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.google.ads.conversiontracking.AdWordsConversionReporter;
import com.google.android.gms.analytics.HitBuilders;

/**
 * Created by gustavo.santos on 7/19/2016.
 */
public class AnalyticsHelper {

    private static final String TAG = AnalyticsHelper.class.getSimpleName();

    // Flags to enable/disable Firebase/GoogleAnalytics
    private static final boolean FIREBASE           = true;
    private static final boolean GOOGLE_ANALYTICS   = true;

    // This assumes the use of Google Analytics campaign
    // "utm" parameters, like "utm_source".
    private static final String CAMPAIGN_SOURCE_PARAM = "utm_source";

    public static void reportAdWordsConversionRegistration() {
        //Google Android in-app conversion tracking snippet for successful Registration
        AdWordsConversionReporter.reportWithConversionId(DokterPribadimuApplication.getInstance().getApplicationContext(),
                "926691219", "bo6bCMjIu2UQk9_wuQM", "1.00", true);
    }

    public static void reportAdWordsConversionSubscription() {
        //Google AdWords Android in-app conversion tracking snippet for successful Subscription
        AdWordsConversionReporter.reportWithConversionId(DokterPribadimuApplication.getInstance().getApplicationContext(),
                "926691219", "yAbrCLeyyGUQk9_wuQM", "1.00", true);
    }

    public static void reportCampaignSourceAttribution(Uri uri, String screenName) {
        // If no URI, return here.
        if (uri == null) { return; }

        HitBuilders.EventBuilder campaignBuilder = new HitBuilders.EventBuilder()
                .setCategory(EventConstants.TYPE_VIEW_SCREEN)
                .setAction(screenName);

        // Source is the only required campaign field.
        // No need to continue if not present.
        if (uri.getQueryParameter(CAMPAIGN_SOURCE_PARAM) != null) {

            // MapBuilder.setCampaignParamsFromUrl parses Google Analytics campaign
            // ("UTM") parameters from a string URL into a Map that can be set on
            // the Tracker.
            campaignBuilder.setCampaignParamsFromUrl(uri.toString());

            //Send the event to GoogleAnalytics
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().send(campaignBuilder.build());
        }
    }

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
                    getDefaultGATracker().send(new HitBuilders.EventBuilder()
                    .setCategory(EventConstants.TYPE_VIEW_SCREEN)
                    .setAction(screenName).build());
            // Reset screen name after sending the event
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().setScreenName(null);
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
            //avoid Firebase error due to limit of characters in an event parameter
            newsCategory = trimLongString(newsCategory);
            bundle.putString(EventConstants.PARAM_NEWS_CATEGORY, newsCategory);
            DokterPribadimuApplication.getInstance()
                    .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_VIEW_SCREEN, bundle);
        }
        if(GOOGLE_ANALYTICS) {
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().send(new HitBuilders.EventBuilder()
                            .setCategory(EventConstants.TYPE_VIEW_SCREEN)
                            .setAction(screenName)
                            .setLabel(EventConstants.PARAM_NEWS_CATEGORY + ": " + newsCategory).build());
            // Reset screen name after sending the event
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().setScreenName(null);
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
                    getDefaultGATracker().send(new HitBuilders.EventBuilder()
                    .setCategory(EventConstants.TYPE_VIEW_DIALOG)
                    .setAction(dialogName).build());
            // Reset screen name after sending the event
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().setScreenName(null);
        }
        // [END view_dialog event]
    }

    public static void logViewDialogRatingEvent(String dialogName, Integer rating) {
        // [START view_dialog event]
        Log.d(TAG, "Analytics log: logViewDialogRatingEvent(dialogName=" + dialogName + ", rating=" + rating + ")");
        if(FIREBASE) {
            Bundle bundle = new Bundle();
            bundle.putString(EventConstants.PARAM_EVENT_NAME, dialogName);
            bundle.putString(EventConstants.PARAM_RATING, rating.toString());
            DokterPribadimuApplication.getInstance()
                    .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_VIEW_DIALOG, bundle);
        }
        if(GOOGLE_ANALYTICS) {
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().send(new HitBuilders.EventBuilder()
                    .setCategory(EventConstants.TYPE_VIEW_DIALOG)
                    .setAction(dialogName)
                    .setLabel(EventConstants.PARAM_RATING + ": " + rating.toString()).build());
            // Reset screen name after sending the event
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().setScreenName(null);
        }
        // [END view_dialog event]
    }

    public static void logViewDialogFailedEvent(String dialogName, String message) {
        // [START view_dialog event]
        Log.d(TAG, "Analytics log: logViewDialogFailedEvent(dialogName=" + dialogName + ", message=" + message + ")");
        if(FIREBASE) {
            Bundle bundle = new Bundle();
            bundle.putString(EventConstants.PARAM_EVENT_NAME, dialogName);
            //avoid Firebase error due to limit of characters in an event parameter
            message = trimLongString(message);
            bundle.putString(EventConstants.PARAM_MESSAGE, message);
            DokterPribadimuApplication.getInstance()
                    .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_VIEW_DIALOG, bundle);
        }
        if(GOOGLE_ANALYTICS) {
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().send(new HitBuilders.EventBuilder()
                    .setCategory(EventConstants.TYPE_VIEW_DIALOG)
                    .setAction(dialogName)
                    .setLabel(EventConstants.PARAM_MESSAGE + ": " + message).build());
            // Reset screen name after sending the event
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().setScreenName(null);
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
                    getDefaultGATracker().send(new HitBuilders.EventBuilder()
                    .setCategory(EventConstants.TYPE_VIEW_WINDOW)
                    .setAction(windowName).build());
            // Reset screen name after sending the event
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().setScreenName(null);
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
                    getDefaultGATracker().send(new HitBuilders.EventBuilder()
                    .setCategory(EventConstants.TYPE_BUTTON_CLICK)
                    .setAction(buttonName).build());
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
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().send(new HitBuilders.EventBuilder()
                    .setCategory(EventConstants.TYPE_BUTTON_CLICK)
                    .setAction(buttonName)
                    .setLabel(EventConstants.PARAM_CALL_TOPIC + ": " + callTopic + ", "
                            + EventConstants.PARAM_CALL_SUBTOPIC + ": " + callSubtopic).build());
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
            //avoid Firebase error due to limit of characters in an event parameter
            categoryName = trimLongString(categoryName);
            bundle.putString(EventConstants.PARAM_CATEGORY, categoryName);
            DokterPribadimuApplication.getInstance()
                    .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_BUTTON_CLICK, bundle);
        }
        if(GOOGLE_ANALYTICS) {
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().send(new HitBuilders.EventBuilder()
                    .setCategory(EventConstants.TYPE_BUTTON_CLICK)
                    .setAction(buttonName)
                    .setLabel(EventConstants.PARAM_CATEGORY + ": " + categoryName).build());
        }
        // [END button_click event]
    }

    public static void logSearchEvent(String screenName, String keyword) {
        // [START search event]
        Log.d(TAG, "Analytics log: logSearchEvent(screenName=" + screenName + ", " + "keyword=" + keyword + ")");
        if(FIREBASE) {
            Bundle bundle = new Bundle();
            bundle.putString(EventConstants.PARAM_EVENT_NAME, screenName);
            //avoid Firebase error due to limit of characters in an event parameter
            keyword = trimLongString(keyword);
            bundle.putString(EventConstants.PARAM_SEARCH_KEYWORD, keyword);
            DokterPribadimuApplication.getInstance()
                    .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_SEARCH, bundle);
        }
        if(GOOGLE_ANALYTICS) {
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().send(new HitBuilders.EventBuilder()
                    .setCategory(EventConstants.TYPE_SEARCH)
                    .setAction(screenName)
                    .setLabel(EventConstants.PARAM_SEARCH_KEYWORD + ": " + keyword).build());
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
            //avoid Firebase error due to limit of characters in an event parameter
            title = trimLongString(title);
            bundle.putString(EventConstants.PARAM_NEWS_TITLE, title);
            bundle.putString(EventConstants.PARAM_NEWS_DATE, date);
            DokterPribadimuApplication.getInstance()
                    .getDefaultFirebaseAnalytics().logEvent(EventConstants.TYPE_ITEM_VIEW, bundle);
        }
        if(GOOGLE_ANALYTICS) {
            DokterPribadimuApplication.getInstance().
                    getDefaultGATracker().send(new HitBuilders.EventBuilder()
                    .setCategory(EventConstants.TYPE_ITEM_VIEW)
                    .setAction(screenName)
                    .setLabel(EventConstants.PARAM_NEWS_CATEGORY + ": " + newsCategory + ", "
                            + EventConstants.PARAM_NEWS_TITLE + ": " + title + ", "
                            + EventConstants.PARAM_NEWS_DATE + ": " + date).build());
        }
        // [END item_view event]
    }

    private static String trimLongString(String str) {
        String trimmedMessage = str;
        if(trimmedMessage.length() > 35) {
            //avoid Firebase error due to limit of 36 characters
            trimmedMessage = str.substring(0, 35);
        }
        return trimmedMessage;
    }
}

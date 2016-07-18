package com.bima.dokterpribadimu.utils;

import android.content.Context;
import android.content.Intent;

import com.bima.dokterpribadimu.model.News;
import com.bima.dokterpribadimu.model.Partner;
import com.bima.dokterpribadimu.view.activities.BookCallActivity;
import com.bima.dokterpribadimu.view.activities.DoctorCallActivity;
import com.bima.dokterpribadimu.view.activities.HomeActivity;
import com.bima.dokterpribadimu.view.activities.LandingActivity;
import com.bima.dokterpribadimu.view.activities.NewsActivity;
import com.bima.dokterpribadimu.view.activities.NewsDetailActivity;
import com.bima.dokterpribadimu.view.activities.OnboardingActivity;
import com.bima.dokterpribadimu.view.activities.PartnersActivity;
import com.bima.dokterpribadimu.view.activities.PartnersDetailActivity;
import com.bima.dokterpribadimu.view.activities.PartnersLandingActivity;
import com.bima.dokterpribadimu.view.activities.PartnersSearchActivity;
import com.bima.dokterpribadimu.view.activities.SignInActivity;

/**
 * Created by apradanas on 5/9/16.
 */
public class IntentUtils {

    /**
     *
     * @param context caller's activity / fragment context
     */
    public static void startOnboardingActivity(Context context) {
        context.startActivity(new Intent(context, OnboardingActivity.class));
    }

    /**
     *
     * @param context caller's activity / fragment context
     */
    public static void startLandingActivity(Context context) {
        context.startActivity(new Intent(context, LandingActivity.class));
    }

    /**
     *
     * @param context caller's activity / fragment context
     * @param isSignIn true for SignIn, false for Register
     */
    public static void startSignInActivity(Context context, boolean isSignIn) {
        context.startActivity(SignInActivity.create(context, isSignIn));
    }

    /**
     *
     * @param context caller's activity / fragment context
     */
    public static void startHomeActivity(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
    }

    /**
     * Start HomeActivity and clear any stack behind it
     * @param context caller's activity / fragment context
     */
    public static void startHomeActivityOnTop(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    /**
     *
     * @param context caller's activity / fragment context
     */
    public static void startPartnersActivity(Context context) {
        context.startActivity(new Intent(context, PartnersActivity.class));
    }

    /**
     *
     * @param context caller's activity / fragment context
     */
    public static void startPartnersLandingActivity(Context context) {
        context.startActivity(new Intent(context, PartnersLandingActivity.class));
    }

    /**
     *
     * @param context caller's activity / fragment context
     */
    public static void startPartnersSearchActivity(Context context) {
        context.startActivity(new Intent(context, PartnersSearchActivity.class));
    }

    /**
     *
     * @param context caller's activity / fragment context
     * @param partner partner's detail to be displayed
     */
    public static void startPartnersDetailActivity(Context context, Partner partner) {
        context.startActivity(PartnersDetailActivity.create(context, partner));
    }

    /**
     *
     * @param context caller's activity / fragment context
     */
    public static void startNewsActivity(Context context) {
        context.startActivity(new Intent(context, NewsActivity.class));
    }

    /**
     *
     * @param context caller's activity / fragment context
     * @param news news to be displayed
     */
    public static void startNewsDetailActivity(Context context, News news) {
        context.startActivity(NewsDetailActivity.create(context, news));
    }

    /**
     *
     * @param context caller's activity / fragment context
     */
    public static void startBookCallActivity(Context context) {
        context.startActivity(new Intent(context, BookCallActivity.class));
    }

    /**
     * Start DoctorCallActivity and clear any stack behind it
     * @param context caller's activity / fragment context
     */
    public static void startDoctorCallActivityOnTop(Context context) {
        Intent intent = new Intent(context, DoctorCallActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}

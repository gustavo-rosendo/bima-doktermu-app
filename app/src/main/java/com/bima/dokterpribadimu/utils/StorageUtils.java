package com.bima.dokterpribadimu.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.bima.dokterpribadimu.BuildConfig;

import java.util.Set;

/**
 * Created by apradanas on 2/21/16.
 */
public class StorageUtils {

    private static final String PREFS_NAME = BuildConfig.APPLICATION_ID;

    public static void clearAll(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }

    public static void remove(Context ctx, String key) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void putString(Context ctx, String key, String value) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context ctx, String key, String defValue) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(key, defValue);
    }

    public static void putStringSet(Context ctx, String key, Set<String> value) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }

    public static Set<String> getStringSet(Context ctx, String key,
                                           Set<String> defValue) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        return settings.getStringSet(key, defValue);
    }

    public static void putInt(Context ctx, String key, int value) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(Context ctx, String key, int defValue) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        return settings.getInt(key, defValue);
    }

    public static void putLong(Context ctx, String key, long value) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getInt(Context ctx, String key, long defValue) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        return settings.getLong(key, defValue);
    }

    public static void putFloat(Context ctx, String key, float value) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static float getFloat(Context ctx, String key, float defValue) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        return settings.getFloat(key, defValue);
    }

    public static void putDouble(Context ctx, String key, double value) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, String.valueOf(value));
        editor.commit();
    }

    public static double getDouble(Context ctx, String key, double defaultValue) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        String str = settings.getString(key, String.valueOf(defaultValue));
        return Double.valueOf(str);
    }

    public static void putBoolean(Context ctx, String key, boolean value) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context ctx, String key,
                                     boolean defaultValue) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean(key, defaultValue);
    }
}

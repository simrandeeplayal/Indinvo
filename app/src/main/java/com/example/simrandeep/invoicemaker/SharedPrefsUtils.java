package com.example.simrandeep.invoicemaker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

/**
 * A pack of helpful getter and setter methods for reading/writing to {@link SharedPreferences}.
 */
public class SharedPrefsUtils {

    // shared pref keys
    public static final String USER_REFERRAL_SKIP = "USER_REFERRAL_SKIP";
    public static final String USER_AUTH_KEY = "AUTH_KEY";
    public static final String USER_ID = "USER_ID";
    public static final String TOTAL_SURVEY_COMPLETED = "TOTAL_SURVEY_COMPLETED";
    public static final String TOTAL_MISSIONS_COMPLETED = "TOTAL_MISSIONS_COMPLETED";
    public static final String TOTAL_EARNINGS = "TOTAL_EARNINGS";
    public static final String USER_MOBILE = "USER_MOBILE";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_IMAGE = "USER_IMAGE";
    public static final String USER_LOGIN_TYPE = "LOGIN_TYPE";
    public static final String USER_REFERRAL_APPLIED = "USER_REFERRAL_APPLIED";
    public static final String USER_MOBILE_VERIFIED = "USER_MOBILE_VERIFIED";
    public static final String USER_EMAIL_VERIFIED = "USER_EMAIL_VERIFIED";
    public static final String USER_HAS_BANK = "USER_HAS_BANK";
    public static final String REGISTERED_DEVICE_TOKEN = "REGISTERED_DEVICE_TOKEN";
    public static final String USER_HAS_PROFILE = "HAS_PROFILE";
    public static final String REFERRAL_CODE = "REFERRAL_CODE";
    public static final String SHOW_WALKTHROUGH = "SHOW_WALKTHROUGH";
    public static final String IN_APP = "IN_APP";
    public static final String HAVE_SETTINGS = "HAVE_SETTINGS";
    public static final String REFERRAL_AMOUNT = "referral_amount";

    private static SharedPrefsUtils sharedPrefsUtils;

    Context context;

    public static SharedPrefsUtils getInstance(Context context) {
        if (sharedPrefsUtils != null) {
            return sharedPrefsUtils;
        } else {
            sharedPrefsUtils = new SharedPrefsUtils(context);
            return sharedPrefsUtils;
        }
    }

    public SharedPrefsUtils(Context context) {
        this.context = context;
    }

    /**
     * Helper method to retrieve a String value from {@link SharedPreferences}.
     *
     * @param key
     * @return The value from shared preferences, or null if the value could not be read.
     */
    public String getStringPreference(String key) {
        String value = null;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            value = preferences.getString(key, null);
        }
        return value;
    }

    /**
     * Helper method to write a String value to {@link SharedPreferences}.
     *
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public boolean setStringPreference(String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * Helper method to retrieve a float value from {@link SharedPreferences}.
     *
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    public float getFloatPreference(String key, float defaultValue) {
        float value = defaultValue;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            value = preferences.getFloat(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write a float value to {@link SharedPreferences}.
     *
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public boolean setFloatPreference(String key, float value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putFloat(key, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * Helper method to retrieve a long value from {@link SharedPreferences}.
     *
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    public long getLongPreference(String key, long defaultValue) {
        long value = defaultValue;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            value = preferences.getLong(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write a long value to {@link SharedPreferences}.
     *
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public boolean setLongPreference(String key, long value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(key, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * Helper method to retrieve an integer value from {@link SharedPreferences}.
     *
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    public int getIntegerPreference(String key, int defaultValue) {
        int value = defaultValue;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            value = preferences.getInt(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write an integer value to {@link SharedPreferences}.
     *
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public boolean setIntegerPreference(String key, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(key, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * Helper method to retrieve a boolean value from {@link SharedPreferences}.
     *
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    public boolean getBooleanPreference(String key, boolean defaultValue) {
        boolean value = defaultValue;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            value = preferences.getBoolean(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write a boolean value to {@link SharedPreferences}.
     *
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public boolean setBooleanPreference(String key, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(key, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * Helper method to remove a key from {@link SharedPreferences}.
     *
     * @param key
     * @return true if the new value was successfully written to persistent storage.
     */
    public boolean removePreference(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(key);
            return editor.commit();
        }
        return false;
    }

    public boolean contains(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.contains(key);
    }

    public boolean clear() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        return editor.commit();
    }
}
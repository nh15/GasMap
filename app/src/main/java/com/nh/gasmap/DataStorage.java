package com.nh.gasmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.Serializable;

// shared preferences でフィルター情報を永続化
public class DataStorage implements Serializable {
    private static final String TAG = "DataStorage";

    private SharedPreferences mSharedPreferences;
    public static final String KEY_SAVE_FILTER = "SAVE_FILTER";
    public static final String KEY_ERROR_GET_FILTER = "ERROR_GET_FILTER";


    public DataStorage(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    public boolean saveFilter(String gasStation) {
        Log.d(TAG, "saveFilter(): gasStation = [" + gasStation + "]");

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_SAVE_FILTER, gasStation);

        // apply()とcommit()のどっちのほうがいいか？
        return editor.commit();
    }

    public String getFilter() {
        Log.d(TAG, "getFilter()");
        return mSharedPreferences.getString(KEY_SAVE_FILTER, KEY_ERROR_GET_FILTER);
    }

}

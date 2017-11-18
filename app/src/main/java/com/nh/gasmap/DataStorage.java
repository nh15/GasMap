package com.nh.gasmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArraySet;
import android.util.Log;

import java.io.Serializable;
import java.util.Set;

// shared preferences でフィルター情報を永続化
public class DataStorage implements Serializable {
    private static final String TAG = "DataStorage";

    private SharedPreferences mFilterSharedPreferences;
    private SharedPreferences mLocationSharedPreferences;
    public static final String KEY_SAVE_FILTER = "SAVE_FILTER";
    public static final String KEY_ERROR_GET_FILTER = "ERROR_GET_FILTER";
    public static final String KEY_SAVE_USER_LOCATION = "SAVE_USER_LOCATION";
    public static final String LATITUDE = "35.681167";
    public static final String LONGITUDE = "139.767052";


    public DataStorage(SharedPreferences sharedPreferences, Context context) {
        mFilterSharedPreferences = sharedPreferences;
        mLocationSharedPreferences = context.getSharedPreferences("location", Context.MODE_PRIVATE);

    }

    public boolean saveFilter(String gasStation) {
        Log.d(TAG, "saveFilter(): gasStation = [" + gasStation + "]");

        SharedPreferences.Editor editor = mFilterSharedPreferences.edit();
        editor.putString(KEY_SAVE_FILTER, gasStation);

        // apply()とcommit()のどっちのほうがいいか？
        return editor.commit();
    }

    public String getFilter() {
        Log.d(TAG, "getFilter()");
        return mFilterSharedPreferences.getString(KEY_SAVE_FILTER, KEY_ERROR_GET_FILTER);
    }

    //onStop()が呼ばれたときに、ユーザーの現在地を保存する
    public boolean saveLastUserLocation(String latitude, String longitude) {
        Log.d(TAG, "saveUserLocation(): latitude: " + latitude + ", longitude: " + longitude);

        Set<String> locate = new ArraySet<>();
        locate.add(latitude);
        locate.add(longitude);
        SharedPreferences.Editor editor = mLocationSharedPreferences.edit();
        editor.putStringSet(KEY_SAVE_USER_LOCATION, locate);

        return editor.commit();
    }

    public Object[] getLastUserLocation() {
        Log.d(TAG, "getLastUserLocation()");
        Set<String> set = new ArraySet<>();
        set.add(LATITUDE);
        set.add(LONGITUDE);

        // 戻り値をObject[]型にするか、Set<String>型にする
        return mLocationSharedPreferences.getStringSet(KEY_SAVE_USER_LOCATION, set).toArray();
    }
}

package com.nh.gasmap;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final String KEY_GET_DATA_STORAGE = "GET_DATA_STORAGE";

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private Marker mMarker;
    private LatLng mUserLocation;
    private boolean mIsMoveCamera = true;
    private MarkerOptions mUserMarker;
    private SharedPreferences mSharedPreferences;
    private DataStorage mDataStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // init
        init();
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mUserMarker = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.user));
        checkLocationPermission();
        setupOnClickListener();
        // initここまで

    }

    private void init() {
        mSharedPreferences = getSharedPreferences("GasStation", MODE_PRIVATE);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);

        mDataStorage = new DataStorage(mSharedPreferences);
    }

    private void setupOnClickListener() {
        Log.d(TAG, "setupOnClickListener()");
        Button btnFilter = (Button) findViewById(R.id.btn_filter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new FilterDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_GET_DATA_STORAGE, mDataStorage);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), "dialog");
            }
        });

        Button btnLocation = (Button) findViewById(R.id.btn_location);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, "location", Toast.LENGTH_SHORT).show();
                mIsMoveCamera = true;
                mMap.moveCamera(CameraUpdateFactory.zoomTo(17f));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(mUserLocation));
            }
        });

        Button btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, LocationAddress.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        requestLocationUpdates();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
        mLocationManager.removeUpdates(mLocationListener);
        super.onDestroy();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //注意：onResume()が完了してから呼ばれる
        Log.d(TAG, "onMapReady");
        mMap = googleMap;

        //初期値：後々、GPSで取得した位置情報を初期値にする
        mUserLocation = new LatLng(35.681167, 139.767052);
        mMarker = mMap.addMarker(mUserMarker.position(mUserLocation));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mUserLocation));
    }

    private void checkLocationPermission() {
        Log.d(TAG, "checkLocationPermission()");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "OK1", Toast.LENGTH_SHORT).show();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);
                Toast.makeText(this, "OK2", Toast.LENGTH_SHORT).show();
            } else {
                Toast toast = Toast.makeText(this, "許可されないとアプリが実行できません", Toast.LENGTH_SHORT);
                toast.show();

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 10);

            }
        }
    }

    private void requestLocationUpdates() {
        Log.d(TAG, "requestLocationUpdates()");

        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, mLocationListener);
        } catch (SecurityException e) {
            Log.d(TAG, "SecurityException");
            e.printStackTrace();
        }
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged()");
            mMarker.remove();
            mUserLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMarker = mMap.addMarker(mUserMarker.position(mUserLocation));

            // defaultもしくは現在地ボタンが押された→true : 現在地とともにカメラ移動
            // 指で、または地図の位置を移動させた→false : カメラを動かさない
            if (mIsMoveCamera) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(mUserLocation));
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged()");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled()");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled()");
        }
    };

    private SharedPreferences.OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    //　全てのマーカーを削除し、選択されている銘柄のマーカーを設置する
                }
            };
}

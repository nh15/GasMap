package com.nh.gasmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationAddress extends Activity {
    private static final String TAG ="LocationAddress";

    public static final String KEY_GET_LOCATION = "GET_LOCATION";

    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_location_address);

        setupOnClickListener();
    }

    private void setupOnClickListener() {
        Toast.makeText(this, "setup", Toast.LENGTH_SHORT).show();

        Button btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText address = (EditText) findViewById(R.id.edittext_address);
                double location[] = getLocation(address.getText().toString());
                Intent intent = new Intent();
                intent.putExtra(KEY_GET_LOCATION, location);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private double[] getLocation(String address) {
        double[] location = {0, 0};
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(address, 1);
            if (addressList != null && addressList.size() > 0) {
                Address addr = addressList.get(0);
                location[0] = (double) addr.getLatitude();
                location[1] = (double) addr.getLongitude();
                Toast.makeText(this, "緯度: " + location[0] + "\n経度: " + location[1], Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.e(TAG, "getLocation() Catch the IOException");
            e.printStackTrace();
        }
        return location;
    }

}

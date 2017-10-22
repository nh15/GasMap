package com.nh.gasmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LocationAddress extends Activity {
    private static final String TAG ="LocationAddress";

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

}

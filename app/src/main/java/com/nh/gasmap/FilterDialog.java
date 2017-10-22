package com.nh.gasmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class FilterDialog extends Activity {
    private static final String TAG = "FilterDialog";
    private final String[] gasStationItems = {"ALL", "A", "B", "C"};
    private String selectedItem = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("");
        dialog.setSingleChoiceItems(gasStationItems, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedItem = gasStationItems[which];
            }
        });
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(FilterDialog.this, "OK : " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(FilterDialog.this, "Cancel : " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });
        dialog.create().show();
    }
}

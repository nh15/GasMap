package com.nh.gasmap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class FilterDialogFragment extends DialogFragment {
    private static final String TAG = "FilterDialog";
    private final String[] gasStationItems = {"ALL", "A", "B", "C"};
    private String selectedItem = "ALL";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
        .setTitle("")
        .setSingleChoiceItems(gasStationItems, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedItem = gasStationItems[which];
            }
        })
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "OK : " + selectedItem, Toast.LENGTH_SHORT).show();

            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancel : " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        })
        .create();
    }

}

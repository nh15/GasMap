package com.nh.gasmap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class FilterDialogFragment extends DialogFragment {
    private static final String TAG = "FilterDialog";
    private final String[] gasStationItems = {"ALL", "A", "B", "C"}; // 選択肢の銘柄が決定したら値を変更する
    private String selectedItem = "ALL";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        final DataStorage dataStorage = (DataStorage) bundle.getSerializable(MapsActivity.KEY_GET_DATA_STORAGE);

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
                try {
                    if (dataStorage.saveFilter(selectedItem)) {
                        Toast.makeText(getActivity(), "saved " + selectedItem, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "failed to save", Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "saveFilter failed");
                    }
                } catch (NullPointerException e) {
                    Log.e(TAG, "Catch the NullPointerException.");
                    e.printStackTrace();
                }
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

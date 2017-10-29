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
                Toast.makeText(getActivity(), "OK : " + selectedItem, Toast.LENGTH_SHORT).show();

                try {
                    if (!dataStorage.saveFilter(gasStationItems[which])) {
                        Toast.makeText(getActivity(), "failed to save", Toast.LENGTH_SHORT).show();
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

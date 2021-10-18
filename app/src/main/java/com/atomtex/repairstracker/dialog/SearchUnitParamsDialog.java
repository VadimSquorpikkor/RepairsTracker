package com.atomtex.repairstracker.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.atomtex.repairstracker.R;

import org.jetbrains.annotations.NotNull;

public class SearchUnitParamsDialog extends BaseDialog {

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeWithVM(R.layout.dialog_search_unit_param);
        Button searchButton = view.findViewById(R.id.show_button);
        searchButton.setOnClickListener(v -> startSearch());
        return dialog;
    }

    private void startSearch() {
        EditText trackIdEdit = view.findViewById(R.id.editTextTrackId);
        String trackId = trackIdEdit.getText().toString();
        Log.e("TAG", "♦♦"+trackId);
        mViewModel.addUnitToObserveCollection(trackId);
        dismiss();
    }
}

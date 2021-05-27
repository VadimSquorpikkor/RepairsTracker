package com.atomtex.repairstracker;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import org.jetbrains.annotations.NotNull;
import static com.atomtex.repairstracker.Constant.ANY_VALUE;
import static com.atomtex.repairstracker.Constant.REPAIR_TYPE;

public class SearchUnitParamsDialog extends BaseDialog {

    Button searchButton;
    EditText serialEdit;

    public SearchUnitParamsDialog() {
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeWithVM(R.layout.dialog_search_unit_param);

        searchButton = view.findViewById(R.id.show_button);
        serialEdit = view.findViewById(R.id.editTextSerial);

        searchButton.setOnClickListener(v -> startSearch());
        return dialog;
    }

    private void startSearch() {
        String serial = serialEdit.getText().toString();
        mViewModel.getUnitListFromBD(ANY_VALUE, ANY_VALUE, ANY_VALUE, REPAIR_TYPE, ANY_VALUE, serial);
        dismiss();
    }

}

package com.gregacucnik.editableseekbar;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceDialogFragmentCompat;

public class EditableSeekBarPreferenceDialog extends PreferenceDialogFragmentCompat {

    private static final String SAVE_STATE_VALUE = "EditableSeekBarPreferenceDialog.text";

    private EditableSeekBar editableSeekBar;

    private int value;

    public static EditableSeekBarPreferenceDialog newInstance(String key) {
        final EditableSeekBarPreferenceDialog fragment = new EditableSeekBarPreferenceDialog();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            value = getEditableSeekBarPreference().getValue();
        } else {
            value = savedInstanceState.getInt(SAVE_STATE_VALUE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_STATE_VALUE, value);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        editableSeekBar = view.findViewById(R.id.seekBar);
        editableSeekBar.setRange(getEditableSeekBarPreference().getMinValue(), getEditableSeekBarPreference().getMaxValue());
        editableSeekBar.setValue(value);

        editableSeekBar.requestFocus();
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            int value = editableSeekBar.getValue();
            if (getEditableSeekBarPreference().callChangeListener(value)) {
                getEditableSeekBarPreference().setValue(value);
            }
        }
    }

    private EditableSeekBarPreference getEditableSeekBarPreference() {
        return (EditableSeekBarPreference) getPreference();
    }

}

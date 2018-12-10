package com.gregacucnik.editableseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;


public class EditableSeekBarPreference extends DialogPreference {

    private static final int DEFAULT_VALUE = 25;

    private int value;

    private int minVal = 1, maxVal = 100;

    public EditableSeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EditableSeekBarPreference, 0, 0);

        minVal = a.getInt(R.styleable.EditableSeekBarPreference_esbpMin, 0);
        maxVal = a.getInt(R.styleable.EditableSeekBarPreference_esbpMax, 0);

        setDialogLayoutResource(R.layout.editable_seekbar_preference);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if(positiveResult) {
            // save new value
            persistInt(value);
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if(restorePersistedValue) {
            value = this.getPersistedInt(DEFAULT_VALUE);
        } else {
            value = (int) defaultValue;
            persistInt(value);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        EditableSeekBar seekBar = (EditableSeekBar) view.findViewById(R.id.seekBar);
        if(seekBar != null) {
            seekBar.setMinValue(minVal);
            seekBar.setMaxValue(maxVal);
            seekBar.setValue(value);
            seekBar.setOnEditableSeekBarChangeListener(new EditableSeekBar.OnEditableSeekBarChangeListener() {
                @Override public void onEditableSeekBarProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
                @Override public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override public void onStopTrackingTouch(SeekBar seekBar) {}
                @Override public void onEnteredValueTooHigh() {}
                @Override public void onEnteredValueTooLow() {}
                @Override
                public void onEditableSeekBarValueChanged(int value) {
                    EditableSeekBarPreference.this.value = value;
                }
            });
        } else Log.wtf(getClass().getSimpleName(), "SeekBar was not found when Dialog was bound!");
    }

    public int getValue() {
        return value;
    }
}

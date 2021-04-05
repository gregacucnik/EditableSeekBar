package com.gregacucnik.editableseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;

import com.gregacucnik.editableseekbar.util.TypedArrayUtils;

import static com.gregacucnik.editableseekbar.util.Constants.DEFAULT_MAX_VALUE;
import static com.gregacucnik.editableseekbar.util.Constants.DEFAULT_MIN_VALUE;
import static com.gregacucnik.editableseekbar.util.Constants.DEFAULT_VALUE;

public class EditableSeekBarPreference extends DialogPreference {

    private int value, minValue, maxValue;

    public EditableSeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray a1 = context.obtainStyledAttributes(attrs, R.styleable.EditableSeekBar, defStyleAttr, defStyleRes);
        TypedArray a2 = context.obtainStyledAttributes(attrs, R.styleable.EditableSeekBarPreference, defStyleAttr, defStyleRes);

        minValue = a1.getInt(R.styleable.EditableSeekBar_minValue, DEFAULT_MIN_VALUE);
        maxValue = a1.getInt(R.styleable.EditableSeekBar_maxValue, DEFAULT_MAX_VALUE);

        if (TypedArrayUtils.getBoolean(a2, R.styleable.EditableSeekBarPreference_useSimpleSummaryProvider,
                R.styleable.EditableSeekBarPreference_useSimpleSummaryProvider, false)) {
            setSummaryProvider(SimpleSummaryProvider.getInstance());
        }

        a1.recycle();
        a2.recycle();

        setDialogLayoutResource(R.layout.editable_seekbar_preference);
    }

    public EditableSeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EditableSeekBarPreference(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(context, R.attr.editTextPreferenceStyle, android.R.attr.editTextPreferenceStyle));
    }

    public EditableSeekBarPreference(Context context) {
        this(context, null);
    }

    public void setValue(int value) {
        this.value = value;
        persistInt(value);
        notifyChanged();
    }

    public int getValue() {
        return value;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        if (defaultValue == null) {
            setValue(getPersistedInt(DEFAULT_VALUE));
        } else {
            setValue(getPersistedInt((Integer) defaultValue));
        }
    }

    public static final class SimpleSummaryProvider implements SummaryProvider<EditableSeekBarPreference> {

        private static SimpleSummaryProvider provider;

        private SimpleSummaryProvider() {}

        public static SimpleSummaryProvider getInstance() {
            if (provider == null) {
                provider = new SimpleSummaryProvider();
            }
            return provider;
        }

        @Override
        public CharSequence provideSummary(EditableSeekBarPreference preference) {
            return String.valueOf(preference.getValue());
        }
    }
}

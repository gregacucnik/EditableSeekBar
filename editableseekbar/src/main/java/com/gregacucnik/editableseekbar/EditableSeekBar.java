package com.gregacucnik.editableseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gregacucnik.editableseekbar.util.Constants;

public class EditableSeekBar extends RelativeLayout implements
        TextWatcher,
        View.OnFocusChangeListener,
        EsbEditText.OnEditTextListener,
        SeekBar.OnSeekBarChangeListener {

    private static final int EDITTEXT_DEFAULT_WIDTH = 50;
    private static final int EDITTEXT_DEFAULT_FONT_SIZE = 18;

    public interface OnEditableSeekBarChangeListener{
        void onEditableSeekBarProgressChanged(SeekBar seekBar, int progress, boolean fromUser);
        void onStartTrackingTouch(SeekBar seekBar);
        void onStopTrackingTouch(SeekBar seekBar);
        void onEnteredValueTooHigh();
        void onEnteredValueTooLow();
        void onEditableSeekBarValueChanged(int value);
    }

    //

    private TextView esbTitle;
    private EsbSeekBar esbSeekBar;
    private EsbEditText esbEditText;

    private OnEditableSeekBarChangeListener mListener;

    private int currentValue = 0;
    private int minValue = 0;
    private int maxValue = 100;

    private boolean selectOnFocus;
    private boolean touching = false;

    private boolean animateChanges;

    //

    public EditableSeekBar(Context context) {
        super(context);
    }

    public EditableSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        setSaveEnabled(true);
        initView(context);
        initAttrs(context, attrs);
    }

    private void initView(Context context) {
        inflate(context, R.layout.editable_seekbar, this);
        esbTitle = findViewById(R.id.esbTitle);
        esbSeekBar = findViewById(R.id.esbSeekBar);
        esbEditText = findViewById(R.id.esbEditText);

        esbSeekBar.setOnSeekBarChangeListener(this);
        esbEditText.addTextChangedListener(this);
        esbEditText.setOnFocusChangeListener(this);
        esbEditText.setOnKeyboardDismissedListener(this);

        esbSeekBar.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard();
                return false;
            }
        });
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        float defaultEditTextWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EDITTEXT_DEFAULT_WIDTH, getResources().getDisplayMetrics());
        int defaultEditTextFontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, EDITTEXT_DEFAULT_FONT_SIZE, getResources().getDisplayMetrics());

        TypedArray a1 = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EditableSeekBar,0, 0);

        esbTitle.setTextAppearance(context, a1.getResourceId(R.styleable.EditableSeekBar_titleAppearance, 0));
        selectOnFocus = a1.getBoolean(R.styleable.EditableSeekBar_selectAllOnFocus, true);
        animateChanges = a1.getBoolean(R.styleable.EditableSeekBar_animateSeekBar, true);

        esbEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, a1.getDimensionPixelSize(R.styleable.EditableSeekBar_editTextFontSize, defaultEditTextFontSize));
        ViewGroup.LayoutParams params = esbEditText.getLayoutParams();
        params.width = (int) a1.getDimension(R.styleable.EditableSeekBar_editTextWidth, defaultEditTextWidth);
        esbEditText.setLayoutParams(params);

        int min = a1.getInteger(R.styleable.EditableSeekBar_minValue, Constants.DEFAULT_MIN_VALUE);
        int max = a1.getInteger(R.styleable.EditableSeekBar_maxValue, Constants.DEFAULT_MAX_VALUE);
        setRange(min, max);

        TypedArray a2 = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EditableSeekBarView,0, 0);
        setTitle(a2.getString(R.styleable.EditableSeekBarView_title));
        setValue(a2.getInteger(R.styleable.EditableSeekBarView_value, translateToRealValue(getRange()/2)));

        esbEditText.setSelectAllOnFocus(selectOnFocus);

        a1.recycle();
        a2.recycle();
    }

    //

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState state = new SavedState(superState);
        state.value = currentValue;
        state.focus = selectOnFocus;
        state.animate = animateChanges;
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable restoreState) {
        SavedState state = (SavedState) restoreState;
        super.onRestoreInstanceState(state.getSuperState());
        setValue(state.value);
        this.animateChanges = state.animate;
        this.selectOnFocus = state.focus;
    }

    //

    @Override
    public void setEnabled(boolean enabled) {
        esbTitle.setEnabled(enabled);
        esbSeekBar.setEnabled(enabled);
        esbEditText.setEnabled(enabled);
    }

    //

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (touching) return;

        if (isNumber(editable.toString())) {
            int value = Integer.parseInt(editable.toString());

            if (value > maxValue && currentValue != maxValue) {
                // entered value is higher than max
                value = maxValue;
                esbEditText.setValue(value);

                if (selectOnFocus) {
                    esbEditText.selectAll();
                } else {
                    esbEditText.selectNone();
                }

                if (mListener != null) {
                    mListener.onEnteredValueTooHigh();
                }
            }

            if (value < minValue && currentValue != minValue) {
                // entered value is lower than min
                value = minValue;
                esbEditText.setValue(value);

                if (selectOnFocus) {
                    esbEditText.selectAll();
                } else {
                    esbEditText.selectNone();
                }

                if (mListener != null) {
                    mListener.onEnteredValueTooLow();
                }
            }

            if (value >= minValue && value <= maxValue && value != currentValue) {
                // value is in bounds, but has changed
                currentValue = value;
                esbSeekBar.setProgress(translateFromRealValue(currentValue), animateChanges);

                if (mListener != null) {
                    mListener.onEditableSeekBarValueChanged(currentValue);
                }
            }
        }
    }

    //

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (view instanceof EditText) {
            if (!hasFocus) {
                // if focus has been lost and edit text does not hold a valid value,
                // reset edit text back to currentValue
                if (!editTextHasValidValue()) {
                    esbEditText.setValue(currentValue);
                    if (mListener != null) {
                        mListener.onEditableSeekBarValueChanged(currentValue);
                    }
                } else {
                    if (selectOnFocus) {
                        esbEditText.selectAll();
                    } else {
                        esbEditText.selectNone();
                    }
                }
            }
        }
    }

    //

    @Override
    public void onEditTextKeyboardDismissed() {
        esbEditText.setValue(currentValue);
        if (mListener != null) {
            mListener.onEditableSeekBarValueChanged(currentValue);
        }
    }

    @Override
    public void onEditTextKeyboardDone() {
        esbEditText.setValue(currentValue);
        if (mListener != null) {
            mListener.onEditableSeekBarValueChanged(currentValue);
        }
        hideKeyboard();
    }

    //

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        currentValue = translateToRealValue(progress);
        if (fromUser) {
            esbEditText.setValue(currentValue);
            if (selectOnFocus) {
                esbEditText.selectAll();
            } else {
                esbEditText.selectNone();
            }
        }

        if (mListener != null) {
            mListener.onEditableSeekBarProgressChanged(seekBar, progress, fromUser);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (mListener != null) {
            mListener.onStartTrackingTouch(seekBar);
        }

        touching = true;
        esbEditText.requestFocus();

        if (selectOnFocus) {
            esbEditText.selectAll();
        } else {
            esbEditText.selectNone();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mListener != null) {
            mListener.onStopTrackingTouch(seekBar);
        }

        touching = false;
        currentValue = translateToRealValue(seekBar.getProgress());

        if (mListener != null) {
            mListener.onEditableSeekBarValueChanged(currentValue);
        }
    }

    //

    /**
     * Set callback listener for changes of EditableSeekBar.
     * @param listener OnEditableSeekBarChangeListener
     */
    public void setOnEditableSeekBarChangeListener(OnEditableSeekBarChangeListener listener){
        this.mListener = listener;
    }

    /**
     * Programmatically set value for EditableSeekBar. If out of range, appropriate callback sent and value set to closest (min or max).
     * @param value integer
     */
    public void setValue(Integer value) {
        if (value == null) return;

        if (!isInRange(value)) {
            if (value < minValue) {
                value = minValue;
            } else if (value > maxValue) {
                value = maxValue;
            }
        }

        currentValue = value;
        esbEditText.setValue(currentValue);
        esbSeekBar.setProgressAnimate(translateFromRealValue(currentValue), animateChanges);
    }

    /**
     * Returns current value of EditableSeekBar.
     * @return integer
     */
    public int getValue(){
        return currentValue;
    }

    /***
     * Set range for EditableSeekBar. Min value must be smaller than max value.
     * @param min integer
     * @param max integer
     */
    public void setRange(int min, int max) {
        if(min > max) {
            minValue = Constants.DEFAULT_MIN_VALUE;
            maxValue = Constants.DEFAULT_MAX_VALUE;
        } else {
            minValue = min;
            maxValue = max;
        }

        esbSeekBar.setMax(getRange());
        // re-set value to fit new ranges
        setValue(currentValue);
    }

    /**
     * Get range of EditableSeekBar.
     * @return integer - Absolute range
     */
    public int getRange() {
        return maxValue < 0 ? Math.abs(maxValue - minValue) : maxValue - minValue;
    }

    /**
     * Set maximum value for EditableSeekBar.
     * @param max integer
     */
    public void setMaxValue(int max) {
        setRange(minValue, max);
    }

    /**
     * Set minimum value for EditableSeekBar.
     * @param min integer
     */
    public void setMinValue(int min) {
        setRange(min, maxValue);
    }

    /**
     * Set title for EditableSeekBar. Title hidden if empty or null.
     * @param title String
     */
    public void setTitle(String title) {
        if (title != null && !title.isEmpty()) {
            esbTitle.setText(title);
            esbTitle.setVisibility(View.VISIBLE);
        } else {
            esbTitle.setVisibility(View.GONE);
        }
    }

    /**
     * Set title color.
     * @param color integer
     */
    public void setTitleColor(int color) {
        esbTitle.setTextColor(color);
    }

    /**
     * Enable or disable SeekBar animation on value change
     * @param animate true/false
     */
    public void setAnimateSeekBar(boolean animate) {
        this.animateChanges = animate;
    }

    //

    private boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean isInRange(int value) {
        if(value < minValue) {
            if(mListener != null) {
                mListener.onEnteredValueTooLow();
            }
            return false;
        }

        if(value > maxValue) {
            if(mListener != null) {
                mListener.onEnteredValueTooHigh();
            }
            return false;
        }

        return true;
    }

    private boolean editTextHasValidValue() {
        Editable editable = esbEditText.getText();
        if (editable == null) {
            return false;
        }

        String textValue = esbEditText.getText().toString();
        return !textValue.isEmpty() && isNumber(textValue) && isInRange(Integer.parseInt(textValue));
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(esbEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private int translateFromRealValue(int realValue) {
        return realValue < 0 ? Math.abs(realValue - minValue) : realValue - minValue;
    }

    private int translateToRealValue(int sbValue) {
        return minValue + sbValue;
    }

    //

    private static class SavedState extends BaseSavedState {
        int value;
        boolean focus, animate;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            value = source.readInt();
            focus = source.readInt() == 1;
            animate = source.readInt() == 1;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(value);
            out.writeInt(focus ? 1 : 0);
            out.writeInt(animate ? 1 : 0);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

    }

}

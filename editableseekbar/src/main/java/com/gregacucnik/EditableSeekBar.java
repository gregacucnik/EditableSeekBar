package com.gregacucnik;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gregacucnik.editableseekbar.R;

/**
 * Created by Grega on 13/11/15.
 */
public class EditableSeekBar extends RelativeLayout implements SeekBar.OnSeekBarChangeListener, TextWatcher, View.OnFocusChangeListener {

    private TextView esbTitle;
    private SeekBar esbSeekBar;
    private EditText esbEditText;

    private boolean selectOnFocus;
    private boolean animateChanges;
    private ValueAnimator seekBarAnimator;

    private static final int SEEKBAR_DEFAULT_MAX = 100;
    private static final int EDITTEXT_DEFAULT_WIDTH = 50;
    private static final int EDITTEXT_DEFAULT_FONT_SIZE = 18;
    private static final int ANIMATION_DEFAULT_DURATION = 300;

    private OnEditableSeekBarChangeListener mListener;

    public interface OnEditableSeekBarChangeListener{
        void onEditabelSeekBarProgressChanged(SeekBar seekBar, int progress, boolean fromUser);
        void onStartTrackingTouch(SeekBar seekBar);
        void onStopTrackingTouch(SeekBar seekBar);
        void onEnteredValueTooHigh();
    }

    public EditableSeekBar(Context context) {
        super(context);
    }

    public EditableSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(getContext(), R.layout.editable_seekbar, this);

        setSaveEnabled(true);

        esbTitle = (TextView)findViewById(R.id.esbTitle);
        esbSeekBar = (SeekBar)findViewById(R.id.esbSeekBar);
        esbEditText = (EditText)findViewById(R.id.esbEditText);


        float defaultEditTextWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EDITTEXT_DEFAULT_WIDTH, getResources().getDisplayMetrics());
        int defaultEditTextFontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, EDITTEXT_DEFAULT_FONT_SIZE, getResources().getDisplayMetrics());

        TypedArray a = context.getTheme().obtainStyledAttributes(
                                            attrs,
                                            R.styleable.EditableSeekBar,
                                            0, 0);

        try {
            setTitle(a.getString(R.styleable.EditableSeekBar_esbTitle));
            esbTitle.setTextAppearance(getContext(), a.getResourceId(R.styleable.EditableSeekBar_esbTitleAppearance, 0));
            selectOnFocus = a.getBoolean(R.styleable.EditableSeekBar_esbSelectAllOnFocus, true);
            animateChanges = a.getBoolean(R.styleable.EditableSeekBar_esbAnimateSeekBar, true);
            esbEditText.setSelectAllOnFocus(selectOnFocus);
            esbEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimensionPixelSize(R.styleable.EditableSeekBar_esbEditTextFontSize, defaultEditTextFontSize));
            setMaxValue(a.getInteger(R.styleable.EditableSeekBar_esbMax, SEEKBAR_DEFAULT_MAX));
            setValue(a.getInteger(R.styleable.EditableSeekBar_esbValue, SEEKBAR_DEFAULT_MAX / 2));
            setEditTextWidth(a.getDimension(R.styleable.EditableSeekBar_esbEditTextWidth, defaultEditTextWidth));
        } finally {
            a.recycle();
        }

        esbSeekBar.setOnSeekBarChangeListener(this);
        esbEditText.addTextChangedListener(this);
        esbEditText.setOnFocusChangeListener(this);

        esbSeekBar.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });
    }

    private void setEditTextWidth(float width) {
        ViewGroup.LayoutParams params = esbEditText.getLayoutParams();
        params.width = (int) width;

        esbEditText.setLayoutParams(params);
    }

    /**
     * Set callback listener for changes of EditableSeekBar.
     * @param listener OnEditableSeekBarChangeListener
     */
    public void setOnEditableSeekBarChangeListener(OnEditableSeekBarChangeListener listener){
        this.mListener = listener;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if(fromUser){
            setEditTextValue(progress);

            if(selectOnFocus)
                esbEditText.selectAll();
        }

        if(mListener != null)
            mListener.onEditabelSeekBarProgressChanged(seekBar, progress, fromUser);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (mListener != null)
            mListener.onStartTrackingTouch(seekBar);

        esbEditText.requestFocus();

        if(selectOnFocus)
            esbEditText.selectAll();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(mListener != null)
            mListener.onStopTrackingTouch(seekBar);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!s.toString().isEmpty()){
            int value = Integer.parseInt(s.toString());

            if(value > esbSeekBar.getMax()){
                value = esbSeekBar.getMax();
                setEditTextValue(value);

                if(selectOnFocus)
                    esbEditText.selectAll();

                if(mListener != null)
                    mListener.onEnteredValueTooHigh();
            }

            setSeekBarValue(value);
        }else
            setSeekBarValue(0);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v instanceof EditText){
            if(!hasFocus) {
                if (esbEditText.getText().toString().isEmpty())
                    setEditTextValue(esbSeekBar.getProgress());
            }else{
                if(selectOnFocus)
                    esbEditText.selectAll();
            }
        }
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(esbEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * Set title for EditableSeekBar. Title hidden if empty or null.
     * @param title String
     */
    public void setTitle(String title){
        if(title != null && !title.isEmpty()) {
            esbTitle.setText(title);
            esbTitle.setVisibility(View.VISIBLE);
        }else
            esbTitle.setVisibility(View.GONE);
    }

    /**
     * Set title color.
     * @param color integer
     */
    public void setTitleColor(int color){
        esbTitle.setTextColor(color);
    }

    private void setEditTextValue(int value){
        if(esbEditText != null) {
            esbEditText.removeTextChangedListener(this);
            esbEditText.setText(Integer.toString(value));
            esbEditText.addTextChangedListener(this);
        }
    }

    private void setSeekBarValue(int value){
        if(esbSeekBar != null){
            if(animateChanges)
                animateSeekbar(esbSeekBar.getProgress(), value);
            else
            esbSeekBar.setProgress(value);
        }
    }

    /**
     * Programmatically set value for EditableSeekBar.
     * @param value integer
     */
    public void setValue(Integer value){
        if(value == null)
            return;

        setEditTextValue(value);
        setSeekBarValue(value);
    }

    /**
     * Returns current value of EditableSeekBar.
     * @return integer
     */
    public int getValue(){
        return esbSeekBar.getProgress();
    }

    /**
     * Set maximum value for EditableSeekBar.
     * @param max integer
     */
    public void setMaxValue(int max){
        if(esbSeekBar != null && max > 0){
            esbSeekBar.setMax(max);
            setEditTextValue(esbSeekBar.getProgress());
        }
    }

    public void setAnimateSeekBar(boolean animate){
        this.animateChanges = animate;
    }


    private static class SavedState extends BaseSavedState {
        int value;
        boolean focus;
        boolean animate;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            value = in.readInt();
            focus = in.readInt() == 1;
            animate = in.readInt() == 1;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(value);
            out.writeInt(focus ? 1 : 0);
            out.writeInt(animate ? 1 : 0);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.value = esbSeekBar.getProgress();
        ss.focus = selectOnFocus;
        ss.animate = animateChanges;
        return ss;
    }



    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setValue(ss.value);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray container) {
        super.dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray container) {
        super.dispatchThawSelfOnly(container);
    }

    private void animateSeekbar(int startValue, int endValue){
        if(seekBarAnimator != null && seekBarAnimator.isRunning())
            seekBarAnimator.cancel();

        if(seekBarAnimator == null){
            seekBarAnimator = ValueAnimator.ofInt(startValue, endValue);
            seekBarAnimator.setInterpolator(new DecelerateInterpolator());
            seekBarAnimator.setDuration(ANIMATION_DEFAULT_DURATION);

            seekBarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    esbSeekBar.setProgress((Integer) animation.getAnimatedValue());
                }
            });
        }else
            seekBarAnimator.setIntValues( startValue, endValue);


        seekBarAnimator.start();
    }

}

package com.gregacucnik.editableseekbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

public class EsbEditText extends AppCompatEditText {

    private OnEditTextListener mListener;

    public EsbEditText(Context context) {
        super(context);
        init();
    }

    public EsbEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EsbEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(mListener != null) {
                        mListener.onEditTextKeyboardDone();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void setOnKeyboardDismissedListener(OnEditTextListener listener) {
        this.mListener = listener;
    }

    public void setValue(Integer value) {
        if (value == null) {
            setText("");
        } else {
            setText(String.valueOf(value));
        }
    }

    public void selectNone() {
        // inversely to selectAll, this method de-selects text and moves
        // cursor to the end.
        if (getText() != null) {
            setSelection(getText().length());
        } else {
            setSelection(0, 0);
        }
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

            if(mListener != null)
                mListener.onEditTextKeyboardDismissed();

            return false;
        }

        return super.dispatchKeyEvent(event);
    }

    public interface OnEditTextListener{
        void onEditTextKeyboardDismissed();
        void onEditTextKeyboardDone();
    }

}

package com.gregacucnik.editableseekbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;

import androidx.appcompat.widget.AppCompatSeekBar;

public class EsbSeekBar extends AppCompatSeekBar {

    private static final int ANIMATION_DEFAULT_DURATION = 300;

    private ValueAnimator seekBarAnimator;

    public EsbSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setProgressAnimate(int progress, boolean animate) {
        if (animate) {
            animateSeekBar(this.getProgress(), progress);
        } else {
            this.setProgress(progress);
        }
    }

    private void animateSeekBar(int startValue, int endValue) {
        if (seekBarAnimator != null && seekBarAnimator.isRunning()) {
            seekBarAnimator.cancel();
        }

        if (seekBarAnimator == null) {
            seekBarAnimator = ValueAnimator.ofInt(startValue, endValue);
            seekBarAnimator.setInterpolator(new DecelerateInterpolator());
            seekBarAnimator.setDuration(ANIMATION_DEFAULT_DURATION);

            seekBarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    EsbSeekBar.this.setProgress((Integer) animation.getAnimatedValue());
                }
            });
        } else {
            seekBarAnimator.setIntValues(startValue, endValue);
        }

        seekBarAnimator.start();
    }
}

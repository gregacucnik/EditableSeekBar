package com.gregacucnik.editableseekbardemo;

import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gregacucnik.editableseekbar.EditableSeekBar;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        EditableSeekBar esbExample1 = findViewById(R.id.esbExample1);
        esbExample1.setValue(90);

        esbExample1.setOnEditableSeekBarChangeListener(new EditableSeekBar.OnEditableSeekBarChangeListener() {
            @Override
            public void onEditableSeekBarProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(LOG_TAG, "onEditableSeekBarProgressChanged: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(LOG_TAG, "onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(LOG_TAG, "onStopTrackingTouch");
            }

            @Override
            public void onEnteredValueTooHigh() {
                Log.d(LOG_TAG, "onEnteredValueTooHigh");
            }

            @Override
            public void onEnteredValueTooLow() {
                Log.d(LOG_TAG, "onEnteredValueTooLow");
            }

            @Override
            public void onEditableSeekBarValueChanged(int value) {
                Log.d(LOG_TAG, "onEditableSeekBarValueChanged: " + value);
            }
        });

        // example usage of a EditableSeekBarPreference (see res/xml/settings.xml)
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new MyPreferenceFragment()).commit();

    }
}

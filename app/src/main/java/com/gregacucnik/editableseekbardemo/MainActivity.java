package com.gregacucnik.editableseekbardemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.gregacucnik.editableseekbar.EditableSeekBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        EditableSeekBar esbExample1 = (EditableSeekBar)findViewById(R.id.esbExample1);
        esbExample1.setValue(90);

        esbExample1.setOnEditableSeekBarChangeListener(new EditableSeekBar.OnEditableSeekBarChangeListener() {
            @Override
            public void onEditableSeekBarProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onEnteredValueTooHigh() {

            }

            @Override
            public void onEnteredValueTooLow() {

            }

            @Override
            public void onEditableSeekBarValueChanged(int value) {

            }
        });

    }
}

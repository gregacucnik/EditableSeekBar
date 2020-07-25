package com.gregacucnik.editableseekbardemo;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.gregacucnik.editableseekbar.EditableSeekBarPreferenceDialogHandler;

public class MyPreferenceFragment extends PreferenceFragmentCompat {

    private static final String FRAGMENT_MANAGER_TAG = "MyPreferenceFragment.Manager";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        if (getParentFragmentManager().findFragmentByTag(FRAGMENT_MANAGER_TAG) != null) {
            return;
        }

        boolean handled = EditableSeekBarPreferenceDialogHandler.showDialog(this, preference, 0, FRAGMENT_MANAGER_TAG);
        if (!handled) {
            super.onDisplayPreferenceDialog(preference);
        }
    }

}

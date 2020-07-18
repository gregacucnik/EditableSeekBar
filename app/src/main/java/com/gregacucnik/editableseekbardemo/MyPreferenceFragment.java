package com.gregacucnik.editableseekbardemo;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.gregacucnik.editableseekbar.EditableSeekBarPreference;
import com.gregacucnik.editableseekbar.EditableSeekBarPreferenceDialog;

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

        if (preference instanceof EditableSeekBarPreference) {
            final EditableSeekBarPreferenceDialog f = EditableSeekBarPreferenceDialog.newInstance(preference.getKey());
            f.setTargetFragment(this, 0);
            f.show(getParentFragmentManager(), FRAGMENT_MANAGER_TAG);

        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

}

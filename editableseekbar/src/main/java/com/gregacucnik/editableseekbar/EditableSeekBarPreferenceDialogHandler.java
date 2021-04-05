package com.gregacucnik.editableseekbar;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class EditableSeekBarPreferenceDialogHandler {

    public static boolean showDialog(PreferenceFragmentCompat preferenceFragment, Preference preference, int requestCode, String fragmentManagerTag) {
        if (preference instanceof EditableSeekBarPreference) {
            final EditableSeekBarPreferenceDialog f = EditableSeekBarPreferenceDialog.newInstance(preference.getKey());
            f.setTargetFragment(preferenceFragment, requestCode);
            f.show(preferenceFragment.getParentFragmentManager(), fragmentManagerTag);
            return true;
        } else {
            return false;
        }
    }
}

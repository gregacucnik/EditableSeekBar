# EditableSeekBar

[![Release](https://jitpack.io/v/mc0239/EditableSeekBar.svg)](https://jitpack.io/#mc0239/EditableSeekBar)

Combination of SeekBar and EditText. Android 4.4+

![Sample Screenshot](https://raw.githubusercontent.com/gregacucnik/EditableSeekBar/master/editableseekbar2.gif)

# Usage
*For a working implementation in the sample app, see the `app/` folder.*

1. Add Jitpack to repositories in `build.gradle` and add EditableSeekBar dependency to dependencies:
```gradle
repositories {
    // your other repos...
    maven { url "https://jitpack.io" }
}

dependencies {
    // your other deps...
    implementation 'com.github.mc0239:EditableSeekBar:3.0.0-rc1'
}
```

## As a widget

Include EditableSeekBar widget in your layout:
    
```xml
<com.gregacucnik.editableseekbar.EditableSeekBar
    android:id="@+id/esbExample1"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:title="Example 1 with title"
    app:minValue="-100"
    app:maxValue="100" />
```

(Optional) Use `OnEditableSeekBarChangeListener` to get callbacks for any changes
 * `onEditableSeekBarProgressChanged(SeekBar seekBar, int progress, boolean fromUser)` Sent when seekbar is moving
 * `onStartTrackingTouch(SeekBar seekBar)` Seekbar moving started by user touch
 * `onStopTrackingTouch(SeekBar seekBar)` Seekbar moving finished by user touch (also onEditableSeekBarValueChanged sent)
 * `onEnteredValueTooHigh()` Entered value is higher than maxValue
 * `onEnteredValueTooLow()` Entered value is lower than minValue
 * `onEditableSeekBarValueChanged(int value)` Current value after change or when keyboard dismissed

## As a preference (androidx)

Include EditableSeekBarPreference in your PreferenceScreen: 

```xml
<com.gregacucnik.editableseekbar.EditableSeekBarPreference
    android:key="preference_key"
    android:defaultValue="20"
    android:title="Preference title"
    app:minValue="10"
    app:maxValue="120"
    app:useSimpleSummaryProvider="true" />
```

## Customization

 * `value` Value for EditableSeekBar (must be in range)
 * (*) `maxValue` Maximum value
 * (*) `minValue` Minimum value
 * `title` Hidden, if empty
 * `titleAppearance` Custom Title TextView Appearance
 * `selectAllOnFocus` Auto select EditText on touch (default: true)
 * `animateSeekBar` Animate SeekBar on value change (default: true)
 * `editTextWidth` Custom EditText width (default: 50dp)
 * `editTextFontSize` Custom EditText font size (default: 18sp)

Note: EditableSeekBarPreference can use only attributes marked with (*). It also uses `android:` prefix for `title` and `defaultValue`, just like built-in preferences do.

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-EditableSeekBar-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/2888)

License
=======
The MIT License (MIT)

Copyright (c) 2015 Grega Čučnik
Copyright (c) 2020 Martin Čebular

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

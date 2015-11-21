# EditableSeekBar

# Usage

1. Add the dependency in your build.gradle

            `compile 'com.gregacucnik:editableseekbar:1.0'`
            
2. Include EditableSeekBar widget in your layout
    
    `<com.gregacucnik.EditableSeekBar
        android:id="@+id/esbExample"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:esbTitle="Example title"
        app:esbMax="5000"
        app:esbValue="1000" />`

3. (Optional) Use `OnEditableSeekBarChangeListener` to get callbacks for any changes

# Customization

 * `esbValue` Value for EditableSeekBar
 * `esbMax` Maximum value
 * `esbTitle` Hidden, if empty
 * `esbTitleAppearance` Custom Title TextView Appearance
 * `esbSelectAllOnFocus` Auto select EditText on touch (default: true)
 * `esbAnimateSeekBar` Animate SeekBar on value change (default: true)
 * `esbEditTextWidth` Custom EditText width (default: 50dp)
 * `esbEditTextFontSize` Custom EditText font size (default: 18sp)

License
=======
The MIT License (MIT)

Copyright (c) 2015 Grega Čučnik

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

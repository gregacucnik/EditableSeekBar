package com.gregacucnik.editableseekbar.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.StyleableRes;

/**
 * Copy of {@link androidx.core.content.res.TypedArrayUtils}, because it's annotated as
 * restricted.
 */
public class TypedArrayUtils {

    /**
     * @see androidx.core.content.res.TypedArrayUtils#getAttr(Context, int, int)
     */
    public static int getAttr(@NonNull Context context, int attr, int fallbackAttr) {
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(attr, value, true);
        if (value.resourceId != 0) {
            return attr;
        }
        return fallbackAttr;
    }

    /**
     * @see androidx.core.content.res.TypedArrayUtils#getBoolean(TypedArray, int, int, boolean)
     */
    public static boolean getBoolean(@NonNull TypedArray a, @StyleableRes int index,
                                     @StyleableRes int fallbackIndex, boolean defaultValue) {
        boolean val = a.getBoolean(fallbackIndex, defaultValue);
        return a.getBoolean(index, val);
    }

}

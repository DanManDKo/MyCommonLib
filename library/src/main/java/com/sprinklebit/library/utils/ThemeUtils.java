package com.sprinklebit.library.utils;

import android.content.Context;
import android.content.res.TypedArray;

public class ThemeUtils {

    public static int getColorFromAttrRes(Context context, int attr) {
        TypedArray a = context.obtainStyledAttributes(new int[] {attr});
        try {
            return a.getColor(0, 0);
        } finally {
            a.recycle();
        }
    }
}

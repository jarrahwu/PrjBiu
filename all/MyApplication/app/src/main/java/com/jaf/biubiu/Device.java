package com.jaf.biubiu;

import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings;
import android.util.TypedValue;

import com.jaf.jcore.Application;

/**
 * Created by jarrah on 2015/4/14.
 */
public class Device {
    public static String getId(Context context) {
        String aid = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return aid;
    }

    public static float dp2px(float dp) {
        Resources r = Application.getInstance().getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return px;
    }

    public static float sp2px(float sp) {
        Resources r = Application.getInstance().getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, r.getDisplayMetrics());
        return px;
    }
}

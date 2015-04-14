package com.jaf.biubiu;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by jarrah on 2015/4/14.
 */
public class Device {
    public static String getId(Context context) {
        String aid = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return aid;
    }
}

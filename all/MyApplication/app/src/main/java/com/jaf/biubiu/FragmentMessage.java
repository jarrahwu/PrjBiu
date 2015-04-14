package com.jaf.biubiu;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jaf.jcore.BindableFragment;

/**
 * Created by jarrah on 2015/4/14.
 */
public class FragmentMessage extends BindableFragment{
    public static Fragment newInstance(Bundle arg) {
        return new FragmentMessage();
    }

    @Override
    protected int onLoadViewResource() {
        return R.layout.fragment_message;
    }
}

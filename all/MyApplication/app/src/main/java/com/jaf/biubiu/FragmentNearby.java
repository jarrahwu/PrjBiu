package com.jaf.biubiu;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jaf.jcore.BindableFragment;

/**
 * Created by jarrah on 2015/4/14.
 */
public class FragmentNearby extends BindableFragment{

    public FragmentNearby() {}

    @Override
    protected int onLoadViewResource() {
        return R.layout.fragment_nearby;
    }

    public static Fragment newInstance(Bundle arg) {
        return new FragmentNearby();
    }
}

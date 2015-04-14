package com.jaf.biubiu;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jaf.jcore.BaseFragment;
import com.jaf.jcore.BindableFragment;

/**
 * Created by jarrah on 2015/4/14.
 */
public class FragmentMe extends BindableFragment{

    public static Fragment newInstance(Bundle arg) {
        return new FragmentMe();
    }

    @Override
    protected int onLoadViewResource() {
        return R.layout.fragment_me;
    }

    @Override
    protected void onViewDidLoad(Bundle savedInstanceState) {

    }
}

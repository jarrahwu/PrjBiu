package com.jaf.biubiu;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jaf.jcore.BindableFragment;

/**
 * Created by jarrah on 2015/4/21.
 */
public class FragmentMyA extends BindableFragment{

    public static final String KEY_MY_A = "my_a";

    public FragmentMyA() {}

    public static  FragmentMyA newInstance(Bundle arg) {
        FragmentMyA fragmentMyQ = new FragmentMyA();
        fragmentMyQ.setArguments(arg);
        return fragmentMyQ;
    }

    public static Fragment newInstance(ActivityMyQA.Extra extra) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_MY_A, extra);
        return newInstance(bundle);
    }

    @Override
    protected int onLoadViewResource() {
        return R.layout.fragment_my_a;
    }


}

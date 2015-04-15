package com.jaf.biubiu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaf.jcore.AbsWorker;
import com.jaf.jcore.BaseFragment;
import com.jaf.jcore.BindView;
import com.jaf.jcore.BindableFragment;
import com.jaf.jcore.NetworkListView;

import org.json.JSONObject;

import java.util.ArrayList;

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

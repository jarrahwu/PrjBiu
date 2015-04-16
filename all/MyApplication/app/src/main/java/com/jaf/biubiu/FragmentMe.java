package com.jaf.biubiu;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jaf.bean.BeanUser;
import com.jaf.bean.ResponseUser;
import com.jaf.jcore.BindableFragment;
import com.jaf.jcore.Http;
import com.jaf.jcore.HttpCallBack;
import com.jaf.jcore.JacksonWrapper;

import org.json.JSONObject;

/**
 * Created by jarrah on 2015/4/14.
 */
public class FragmentMe extends BindableFragment{

    private static final String TAG = "Fragment Me";
    private BeanUser mBeanUser;

    public static Fragment newInstance(Bundle arg) {
        return new FragmentMe();
    }

    @Override
    protected int onLoadViewResource() {
        return R.layout.fragment_me;
    }

    @Override
    protected void onViewDidLoad(Bundle savedInstanceState) {
        super.onViewDidLoad(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        requestUserInfo();
    }

    private void requestUserInfo() {
        Http http = new Http(TAG);
        http.url(Constant.API).JSON(U.buildUser()).post(new HttpCallBack() {
            @Override
            public void onResponse(JSONObject response) {
                super.onResponse(response);
                ResponseUser responseUser = JacksonWrapper.json2Bean(response, ResponseUser.class);
                if (responseUser != null) {
                    mBeanUser = responseUser.getReturnData();
                    L.dbg(TAG + " " + response.toString());
                }
            }
        });
    }

}

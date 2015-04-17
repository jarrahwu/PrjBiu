package com.jaf.biubiu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaf.bean.BeanNearbyItem;
import com.jaf.bean.BeanRequestQuestionList;
import com.jaf.bean.ResponseNearby;
import com.jaf.jcore.AbsWorker;
import com.jaf.jcore.Application;
import com.jaf.jcore.BindView;
import com.jaf.jcore.BindableFragment;
import com.jaf.jcore.Http;
import com.jaf.jcore.HttpCallBack;
import com.jaf.jcore.JacksonWrapper;
import com.jaf.jcore.NetworkListView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jarrah on 2015/4/14.
 */
public class FragmentNearby extends BindableFragment implements Constant{

    private static final String TAG = "FragmentNearby";

    public FragmentNearby() {}

    @BindView(id = R.id.networkListView)
    private NetworkListView<View, BeanNearbyItem> mNetworkListView;

    private com.jaf.jcore.AbsWorker.AbsLoader<android.view.View, BeanNearbyItem> loader;

    public static Fragment newInstance(Bundle arg) {
        return new FragmentNearby();
    }

    @Override
    protected int onLoadViewResource() {
        return R.layout.fragment_nearby;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onViewDidLoad(Bundle savedInstanceState) {
        super.onViewDidLoad(savedInstanceState);

        loader = new AbsWorker.AbsLoader<View, BeanNearbyItem>() {
            @Override
            public String parseNextUrl(JSONObject response) {
                return null;
            }

            @Override
            public ArrayList<BeanNearbyItem> parseJSON2ArrayList(JSONObject response) {
                L.dbg(response.toString());
                ResponseNearby responseNearby = JacksonWrapper.json2Bean(response, ResponseNearby.class);
                return responseNearby.getReturnData().getContData();
            }

            @Override
            public void updateItemUI(int position, final BeanNearbyItem data, View itemView) {
                TextView tv = (TextView) itemView;
                tv.setText(U.b642s(data.getQuest()));
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityQuestion.Extra extra = new ActivityQuestion.Extra();
                        extra.questId = data.getQuestId();
                        extra.fromNearby = data;
                        ActivityQuestion.start(getActivity(), extra);
                    }
                });
            }

            @Override
            public View makeItem(LayoutInflater inflater, int position, View convertView, ViewGroup parent) {
                return new TextView(getActivity());
            }
        };

        requestListWhenLocated();
    }

    private void requestListWhenLocated() {
        LocationManager.getInstance().requestLocation(new LocationManager.JLsn() {
            @Override
            public void onResult(double latitude, double longitude) {
                super.onResult(latitude, longitude);
                Application.getInstance().setAppExtraInfo(Device.getId(Application.getInstance().getApplicationContext()), latitude, longitude);
                registerDevice();
            }
        });
    }

    private void registerDevice() {
        Http http = new Http(TAG);
        final Application.AppExtraInfo info = Application.getInstance().getAppExtraInfo();
        http.url(Constant.API).JSON(U.buildRequest(Constant.CMD.USER_REG, info.lat, info.lon)).post(new HttpCallBack() {
            @Override
            public void onResponse(JSONObject response) {
                super.onResponse(response);
                L.dbg(TAG + " register : " + response);
                //request url when located and registered
                JSONObject jo = U.buildNearby(info.lat, info.lon, true, 0);
                mNetworkListView.request(API, loader, jo);
            }
        });
    }


}

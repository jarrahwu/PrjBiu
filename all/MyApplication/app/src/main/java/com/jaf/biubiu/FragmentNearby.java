package com.jaf.biubiu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocation;
import com.jaf.bean.BeanNearbyItem;
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
    private NetworkListView<ViewNearbyItem, BeanNearbyItem> mNetworkListView;

    private com.jaf.jcore.AbsWorker.AbsLoader<ViewNearbyItem, BeanNearbyItem> loader;

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

        loader = new AbsWorker.AbsLoader<ViewNearbyItem, BeanNearbyItem>() {
            @Override
            public String parseNextUrl(JSONObject response) {
                return Constant.API;
            }

            @Override
            public JSONObject parseNextJSON(JSONObject response) {
                ResponseNearby responseNearby = JacksonWrapper.json2Bean(response, ResponseNearby.class);
                ArrayList<BeanNearbyItem> data = responseNearby.getReturnData().getContData();
                if(data.size() > 0 ) {
                    final Application.AppExtraInfo info = Application.getInstance().getAppExtraInfo();
                    int lastId = data.get(data.size() - 1).getSortId();
                    return U.buildNearby(info.lat, info.lon, false, lastId);
                }
                return null;
            }

            @Override
            public ArrayList<BeanNearbyItem> parseJSON2ArrayList(JSONObject response) {
                L.dbg(response.toString());
                ResponseNearby responseNearby = JacksonWrapper.json2Bean(response, ResponseNearby.class);
                return responseNearby.getReturnData().getContData();
            }

            @Override
            public void updateItemUI(int position, final BeanNearbyItem data, ViewNearbyItem itemView) {
                ViewNearbyItem view = (ViewNearbyItem) itemView;
                view.setData(data, position);

            }

            @Override
            public ViewNearbyItem makeItem(LayoutInflater inflater, int position, View convertView, ViewGroup parent) {
                return new ViewNearbyItem(getActivity());
            }
        };

        requestListWhenLocated();
    }


    private void requestListWhenLocated() {
        LocationManager.getInstance().requestLocation(new LocationManager.JLsn() {
            @Override
            public void onResult(double latitude, double longitude, BDLocation location) {
                Application.getInstance().setAppExtraInfo(Device.getId(Application.getInstance().getApplicationContext()), latitude, longitude);
                Application.getInstance().mAppExtraInfo.city = location.getCity();
                String city = getString(R.string.app_name);
                if(!TextUtils.isEmpty(Application.getInstance().mAppExtraInfo.city)) {
                    city = location.getCity();
                }
                getActivity().setTitle(city);
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

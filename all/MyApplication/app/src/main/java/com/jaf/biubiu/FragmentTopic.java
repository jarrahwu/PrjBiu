package com.jaf.biubiu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaf.bean.BeanTopicItem;
import com.jaf.bean.ResponseTopic;
import com.jaf.jcore.AbsWorker;
import com.jaf.jcore.BindView;
import com.jaf.jcore.BindableFragment;
import com.jaf.jcore.JacksonWrapper;
import com.jaf.jcore.NetworkListView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jarrah on 2015/4/14.
 */
public class FragmentTopic extends BindableFragment implements Constant.CMD{

    @BindView(id = R.id.topicList)
    private NetworkListView<View, BeanTopicItem> mNetworkListView;
    private com.jaf.jcore.AbsWorker.AbsLoader<android.view.View,com.jaf.bean.BeanTopicItem> loader;

    public FragmentTopic() {}

    @Override
    protected int onLoadViewResource() {
        return R.layout.fragment_topic;
    }

    @Override
    protected void onViewDidLoad(Bundle savedInstanceState) {
        super.onViewDidLoad(savedInstanceState);
        loader = new AbsWorker.AbsLoader<View, BeanTopicItem>() {
            @Override
            public String parseNextUrl(JSONObject response) {
                return null;
            }

            @Override
            public ArrayList<BeanTopicItem> parseJSON2ArrayList(JSONObject response) {
                ResponseTopic responseTopic = JacksonWrapper.json2Bean(response, ResponseTopic.class);
                L.dbg("TOPIC : " + response);
                if (responseTopic != null && responseTopic.getReturnData() != null) {
                    return responseTopic.getReturnData().getContData();
                }else {
                    return null;
                }
            }

            @Override
            public void updateItemUI(int position, BeanTopicItem data, View itemView) {
                TextView tv = (TextView) itemView;
                tv.setText(data.getOwnerNick());
            }

            @Override
            public View makeItem(LayoutInflater inflater, int position, View convertView, ViewGroup parent) {
                return new TextView(getActivity());
            }
        };
        mNetworkListView.request(Constant.API, loader, U.buildTopic());
    }

    public static Fragment newInstance(Bundle arg) {
        return new FragmentTopic();
    }
}

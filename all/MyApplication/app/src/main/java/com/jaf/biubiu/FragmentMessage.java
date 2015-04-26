package com.jaf.biubiu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaf.bean.BeanMsgItem;
import com.jaf.bean.ResponseMsgList;
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
public class FragmentMessage extends BindableFragment{

    @BindView(id = R.id.messageList)
    private NetworkListView<ViewMsgItem, BeanMsgItem> mListView;
    private com.jaf.jcore.AbsWorker.AbsLoader<com.jaf.biubiu.ViewMsgItem,com.jaf.bean.BeanMsgItem> loader;

    public static Fragment newInstance(Bundle arg) {
        return new FragmentMessage();
    }

    @Override
    protected int onLoadViewResource() {
        return R.layout.fragment_message;
    }

    @Override
    protected void onViewDidLoad(Bundle savedInstanceState) {
        super.onViewDidLoad(savedInstanceState);
        loader = new AbsWorker.AbsLoader<ViewMsgItem, BeanMsgItem>() {
            @Override
            public String parseNextUrl(JSONObject response) {
                return Constant.API;
            }

            @Override
            public JSONObject parseNextJSON(JSONObject response) {
                ResponseMsgList responseMsgList = JacksonWrapper.json2Bean(response, ResponseMsgList.class);
                ArrayList<BeanMsgItem> data = responseMsgList.getReturnData().getContData();
                if(data.size() > 0 ) {
                    int lastId = data.get(data.size() - 1).getUllId();
                    return U.buildMsgList(false, lastId);
                }
                return null;
            }

            @Override
            public ArrayList<BeanMsgItem> parseJSON2ArrayList(JSONObject response) {
                ResponseMsgList responseMsgList = JacksonWrapper.json2Bean(response, ResponseMsgList.class);
                if(responseMsgList != null)
                    return responseMsgList.getReturnData().getContData();
                return null;
            }

            @Override
            public void updateItemUI(int position, BeanMsgItem data, ViewMsgItem itemView) {
                itemView.setData(data);
            }

            @Override
            public ViewMsgItem makeItem(LayoutInflater inflater, int position, View convertView, ViewGroup parent) {
                return new ViewMsgItem(getActivity());
            }
        };
        mListView.request(Constant.API, loader, U.buildMsgList(true, Integer.MAX_VALUE));
    }
}

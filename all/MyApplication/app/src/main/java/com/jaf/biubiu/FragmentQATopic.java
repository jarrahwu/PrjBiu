package com.jaf.biubiu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaf.bean.BeanNearbyItem;
import com.jaf.bean.BeanRequestTopicQuestionList;
import com.jaf.bean.ResponseTopicQuestions;
import com.jaf.jcore.AbsWorker;
import com.jaf.jcore.BindView;
import com.jaf.jcore.BindableFragment;
import com.jaf.jcore.JacksonWrapper;
import com.jaf.jcore.NetworkListView;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jarrah on 2015/4/14.
 * 一个完整问答 来自 板块
 */
public class FragmentQATopic extends BindableFragment implements Constant{

    private static final String TAG = "FragmentQuestionList";
    public static final String KEY_REQUEST = "request_json";
    private int mUnionId;

    public FragmentQATopic() {}

    @BindView(id = R.id.networkListView)
    private NetworkListView<ViewNearbyItem, BeanNearbyItem> mNetworkListView;

    private AbsWorker.AbsLoader<ViewNearbyItem, BeanNearbyItem> loader;

    public static Fragment newInstance(BeanRequestTopicQuestionList arg) {
        FragmentQATopic f = new FragmentQATopic();
        Bundle b = new Bundle();
        b.putSerializable(KEY_REQUEST, arg);
        f.setArguments(b);
        return f;
    }

    @Override
    protected int onLoadViewResource() {
        return R.layout.fragment_question_list;
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
                ResponseTopicQuestions r = JacksonWrapper.json2Bean(response, ResponseTopicQuestions.class);
                ArrayList<BeanNearbyItem> data = r.getReturnData().getContData();
                if(data.size() > 0 ) {
                    int lastId = data.get(data.size() - 1).getSortId();
                    return U.buildTopicQuestion(false, lastId, mUnionId);
                }
                return null;
            }

            @Override
            public ArrayList<BeanNearbyItem> parseJSON2ArrayList(JSONObject response) {
                L.dbg("fragment question  response " + response.toString());
                ResponseTopicQuestions r = JacksonWrapper.json2Bean(response, ResponseTopicQuestions.class);
                return r.getReturnData().getContData();
            }

            @Override
            public void updateItemUI(int position, final BeanNearbyItem data, ViewNearbyItem itemView) {
                itemView.setData(data, position);
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ActivityDetail.Extra extra = new ActivityDetail.Extra();
//                        extra.questId = data.getQuestId();
//                        extra.fromNearby = data;
//                        ActivityDetail.start(getActivity(), extra);
//                    }
//                });
            }

            @Override
            public ViewNearbyItem makeItem(LayoutInflater inflater, int position, View convertView, ViewGroup parent) {
                return new ViewNearbyItem(getActivity());
            }
        };

        doRequest();
    }

    private void doRequest() {
        Serializable serializable = getArguments().getSerializable(KEY_REQUEST);
        if (getArguments() != null && serializable != null) {
            BeanRequestTopicQuestionList request = (BeanRequestTopicQuestionList) serializable;
            mUnionId = request.getUnionId();
            mNetworkListView.request(Constant.API, loader, U.buildTopicQuestion(true, 0, request.getUnionId()));
        }else {
            L.dbg(TAG + " do request but data is null!");
        }
    }
}

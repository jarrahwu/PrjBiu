package com.jaf.biubiu;

import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaf.bean.BeanAnswerItem;
import com.jaf.bean.BeanRequestQuestionList;
import com.jaf.bean.ResponseQuestion;
import com.jaf.jcore.AbsWorker;
import com.jaf.jcore.BindView;
import com.jaf.jcore.BindableFragment;
import com.jaf.jcore.JacksonWrapper;
import com.jaf.jcore.NetworkListView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jarrah on 2015/4/16.
 */
public class FragmentQuestionList extends BindableFragment{

    private static final String KEY_ARGS = "args";

    @BindView(id = R.id.questionList)
    private NetworkListView<ViewAnswerItem, BeanAnswerItem> mListView;

    private com.jaf.jcore.AbsWorker.AbsLoader<ViewAnswerItem,com.jaf.bean.BeanAnswerItem> mLoader;

    public FragmentQuestionList() {}

    public static FragmentQuestionList newInstance(BeanRequestQuestionList b) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_ARGS, b);
        FragmentQuestionList fragmentQuestionList = new FragmentQuestionList();
        fragmentQuestionList.setArguments(args);
        return fragmentQuestionList;
    }

    @Override
    protected int onLoadViewResource() {
        return R.layout.fragment_question_list;
    }

    @Override
    protected void onViewDidLoad(Bundle savedInstanceState) {
        super.onViewDidLoad(savedInstanceState);
        if (getData() != null) {
            setupList();
        }else {
            L.dbg("get data is null!");
        }
    }

    private void setupList() {
        JSONObject jo = JacksonWrapper.bean2Json(getData());
        mLoader = new AbsWorker.AbsLoader<ViewAnswerItem, BeanAnswerItem>() {
            @Override
            public String parseNextUrl(JSONObject response) {
                return null;
            }

            @Override
            public ArrayList<BeanAnswerItem> parseJSON2ArrayList(JSONObject response) {
                ResponseQuestion responseQuestion = JacksonWrapper.json2Bean(response, ResponseQuestion.class);
                L.dbg("FragmentQuestionList response :" + response);
                if (responseQuestion != null) {
                    return responseQuestion.getReturnData().getContData();
                }else {
                    L.dbg("responseQuestion is null !");
                    return null;
                }
            }

            @Override
            public void updateItemUI(int position, BeanAnswerItem data, ViewAnswerItem itemView) {
                itemView.setData(data);
            }

            @Override
            public ViewAnswerItem makeItem(LayoutInflater inflater, int position, View convertView, ViewGroup parent) {
                return new ViewAnswerItem(getActivity());
            }
        };
        mListView.request(Constant.API, mLoader, jo);
    }

    public BeanRequestQuestionList getData() {
        return (BeanRequestQuestionList) getArguments().getSerializable(KEY_ARGS);
    }
}

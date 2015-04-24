package com.jaf.biubiu;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jaf.bean.BeanAnswerItem;
import com.jaf.bean.BeanNearbyItem;
import com.jaf.bean.ResponseQuestion;
import com.jaf.jcore.BindView;
import com.jaf.jcore.BindableView;
import com.jaf.jcore.Http;
import com.jaf.jcore.HttpCallBack;
import com.jaf.jcore.JacksonWrapper;

import org.json.JSONObject;

import java.util.ArrayList;

import master.flame.danmaku.ui.widget.DanmakuView;

/**
 * Created by jarrah on 2015/4/23.
 */
public class ViewNearbyItem extends BindableView {

    @BindView(id = R.id.name)
    private TextView mName;

    @BindView(id = R.id.content)
    private TextView mContent;

    @BindView(id = R.id.locDesc)
    private TextView mLocDesc;

    @BindView(id = R.id.replyCount)
    private TextView mReplyCount;

    @BindView(id = R.id.optionContainer)
    private View mOptionContainer;

    @BindView(id = R.id.danmu)
    private DanmakuView mDanmakuView;

    private BeanNearbyItem mBeanNearbyItem;
    private Http http;
    private ArrayList<BeanAnswerItem> mDanmuSouce;

    @BindView(id = R.id.listMode, onClick = "onListModeClick")
    View btnListMode;

    @BindView(id = R.id.reply)
    View btnReply;

    private LikePanelHolder mLikePanelHolder;

    public ViewNearbyItem(Context context) {
        super(context);
    }

    @Override
    public void onViewDidLoad() {
        http = new Http();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDanmu();
            }
        });
    }

    private void showDanmu() {
        int vb = mOptionContainer.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
        mOptionContainer.setVisibility(vb);
        if(vb == VISIBLE) {
            DanmuHelper.setupDanmu(mDanmakuView);
            requestDanmuData();
        }else {
            mDanmakuView.stop();
        }

    }

    private void requestDanmuData() {
        http.url(Constant.API).JSON(U.buildQuestion(false, 999, mBeanNearbyItem.getQuestId())).post(new HttpCallBack(){
            @Override
            public void onResponse(JSONObject response) {
                super.onResponse(response);
                ResponseQuestion responseQuestion = JacksonWrapper.json2Bean(response, ResponseQuestion.class);
                if (responseQuestion != null) {
                    L.dbg("danmu get!");
                    mDanmuSouce = responseQuestion.getReturnData().getContData();
                    delayDanmu();
                }
            }
        });
    }

    int startIndex = 0;
    private void delayDanmu() {
        if(mDanmuSouce == null || mDanmuSouce.isEmpty()) return;

        postDelayed(new Runnable() {
            @Override
            public void run() {
                BeanAnswerItem item = null;
                try {
                    item = mDanmuSouce.get(startIndex);
                } catch (Exception e) {
                    item = null;
                }
                if(item == null) return;

                DanmuHelper.addDanmaku(mDanmakuView, false, item.getAns());
                startIndex ++;
                startIndex = startIndex == mDanmuSouce.size() ? 0 : startIndex;
                delayDanmu();
            }
        }, 300);
    }


    @Override
    public int onLoadViewResource() {
        return R.layout.view_nearby_item;
    }

    public void setData(BeanNearbyItem beanNearbyItem) {
        mBeanNearbyItem = beanNearbyItem;
        String name = TextUtils.isEmpty(beanNearbyItem.getSign()) ? getContext().getString(R.string.anonymity) : beanNearbyItem.getSign();
        mName.setText(name);
        mContent.setText(beanNearbyItem.getQuest());
        mLocDesc.setText(beanNearbyItem.getSelfLocDesc());
        mReplyCount.setText(getContext().getString(R.string.replyCount, beanNearbyItem.getAnsNum()));

        //like unlike
        LikePanelHolder.Extra extra = new LikePanelHolder.Extra();
        extra.aid = 0;
        extra.qid = mBeanNearbyItem.getQuestId();
        mLikePanelHolder = new LikePanelHolder(extra, mOptionContainer);
        mLikePanelHolder.setData(mBeanNearbyItem);

        //magic
        mOptionContainer.setVisibility(View.GONE);
        mDanmakuView.stop();
    }

    public void onListModeClick(View v) {
        ActivityDetail.Extra extra = new ActivityDetail.Extra();
        extra.fromNearby = mBeanNearbyItem;
        extra.questId = mBeanNearbyItem.getQuestId();
        ActivityDetail.start((Activity) getContext(), extra);
    }
}

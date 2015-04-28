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
	private View mDanmuContainer;

    @BindView(id = R.id.danmuLoadingTips)
    private TextView mDanmuTips;

	@BindView(id = R.id.danmu)
	private DanmakuView mDanmakuView;

	@BindView(id = R.id.itemContainer)
	private View mItemContainer;

	@BindView(id = R.id.itemSubContainer)
	private View mItemSubContainer;

    @BindView(id = R.id.itemClickArea, onClick = "onItemClick")
    private View mItemClickArea;

    private BeanNearbyItem mBeanNearbyItem;
	private Http http;
	private ArrayList<BeanAnswerItem> mDanmuSouce;

	@BindView(id = R.id.listMode, onClick = "onListModeClick")
	View btnListMode;
	private LikePanelHolder mLikePanelHolder;

    @BindView(id = R.id.likePanelContainer, onClick = "doNothing")
    private View mLikePanelContainer;

	public ViewNearbyItem(Context context) {
		super(context);
	}

	@Override
	public void onViewDidLoad() {
		http = new Http();
	}

	private void toggleDanmuPanel() {
		int vb = mDanmuContainer.getVisibility() == View.VISIBLE
				? View.GONE
				: View.VISIBLE;
		mDanmuContainer.setVisibility(vb);
		if (vb == VISIBLE) {
			if (mBeanNearbyItem.getAnsNum() == 0) {
                mDanmuTips.setVisibility(View.VISIBLE);
                mDanmuTips.setText(R.string.commentCome);
			}else {
                mDanmuTips.setVisibility(View.VISIBLE);
                mDanmuTips.setText(R.string.danmuComming);
            }
			DanmuHelper.setupDanmu(mDanmakuView);
			requestDanmuData();
		} else {
			mDanmakuView.stop();
		}
	}

	private void requestDanmuData() {
		http.url(Constant.API)
				.JSON(U.buildQuestion(false, 999, mBeanNearbyItem.getQuestId()))
				.post(new HttpCallBack() {
					@Override
					public void onResponse(JSONObject response) {
						super.onResponse(response);
						ResponseQuestion responseQuestion = JacksonWrapper
								.json2Bean(response, ResponseQuestion.class);
						if (responseQuestion != null) {
							L.dbg("danmu get!");
							mDanmuSouce = responseQuestion.getReturnData()
									.getContData();
                            if(mDanmuSouce.size() > 0) {
                                mDanmuTips.setVisibility(View.GONE);
                            }
							delayDanmu();
						}
					}
				});
	}

	int startIndex = 0;
	private void delayDanmu() {
		if (mDanmuSouce == null || mDanmuSouce.isEmpty())
			return;

		postDelayed(new Runnable() {
			@Override
			public void run() {
				BeanAnswerItem item = null;
				try {
					item = mDanmuSouce.get(startIndex);
				} catch (Exception e) {
					item = null;
				}
				if (item == null)
					return;

				DanmuHelper.addDanmaku(mDanmakuView, false, item.getAns());
				startIndex++;
				startIndex = startIndex == mDanmuSouce.size() ? 0 : startIndex;
				delayDanmu();
			}
		}, 300);
	}

	@Override
	public int onLoadViewResource() {
		return R.layout.view_nearby_item;
	}

	public void setData(BeanNearbyItem beanNearbyItem, int position) {
		mBeanNearbyItem = beanNearbyItem;
		String name = TextUtils.isEmpty(beanNearbyItem.getSign())
				? getContext().getString(R.string.anonymity)
				: beanNearbyItem.getSign();
		mName.setText(name);
		mContent.setText(beanNearbyItem.getQuest());
		// mLocDesc.setText(beanNearbyItem.getSelfLocDesc());
		mReplyCount.setText(getContext().getString(R.string.replyCount,
				beanNearbyItem.getAnsNum()));

		// distance
		String distance = beanNearbyItem.getDistance() < 3 ? " <3" : String
				.valueOf(beanNearbyItem.getDistance());
		mLocDesc.setText(getContext().getString(R.string.distance, distance));

		// manage sign color
		if (beanNearbyItem.getIsYellow() == 1) {
			mName.setTextColor(getResources().getColor(R.color.dfYellow));
		} else {
			mName.setTextColor(getResources().getColor(R.color.dfBlue));
		}


//		// like unlike
//		LikePanelHolder.Extra extra = new LikePanelHolder.Extra();
//		extra.aid = mBeanNearbyItem.getAnsId();
//		extra.qid = mBeanNearbyItem.getQuestId();
//		mLikePanelHolder = new LikePanelHolder(extra, mDanmuContainer);
//		mLikePanelHolder.setData(mBeanNearbyItem);

		// magic
		mDanmuContainer.setVisibility(View.GONE);
		mDanmakuView.stop();

		// padding color
		int index = position % 3;
		int res = R.color.tagYellow;
		switch (index) {
			case 1 :
				res = R.color.tagRed;
				break;
			case 2 :
				res = R.color.tagGreen;
				break;
			default :
				break;
		}

		// List ITEM INTERVAL COLOR
		int color = getResources().getColor(R.color.white);
		if (position % 2 == 1) {
			color = getResources().getColor(R.color.listItemDark);
		} else {
			color = getResources().getColor(R.color.white);
		}
		mItemSubContainer.setBackgroundColor(color);

		mItemContainer.setBackgroundColor(getResources().getColor(res));
	}

	public void onListModeClick(View v) {
		ActivityDetail.Extra extra = new ActivityDetail.Extra();
		extra.fromNearby = mBeanNearbyItem;
		extra.questId = mBeanNearbyItem.getQuestId();
		ActivityDetail.start((Activity) getContext(), extra);
	}

    public void doNothing(View v) {
        L.dbg("do nothing");
    }

    public void onItemClick(View v) {
        toggleDanmuPanel();
    }
}

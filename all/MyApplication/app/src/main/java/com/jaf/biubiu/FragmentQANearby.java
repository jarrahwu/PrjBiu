package com.jaf.biubiu;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaf.bean.BeanAnswerItem;
import com.jaf.bean.BeanNearbyItem;
import com.jaf.bean.BeanRequestAnswerList;
import com.jaf.bean.ResponseQuestion;
import com.jaf.jcore.AbsWorker;
import com.jaf.jcore.Application;
import com.jaf.jcore.BindView;
import com.jaf.jcore.BindableFragment;
import com.jaf.jcore.JacksonWrapper;
import com.jaf.jcore.NetworkListView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jarrah on 2015/4/16.
 * 一个完整的问答模块 包含一个 问题header 一些答案list
 * 附近 , 板块 公共
 */
public class FragmentQANearby extends BindableFragment {

	private static final String KEY_ARGS = "args";

	@BindView(id = R.id.questionList)
	private NetworkListView<ViewAnswerItem, BeanAnswerItem> mListView;

	private com.jaf.jcore.AbsWorker.AbsLoader<ViewAnswerItem, com.jaf.bean.BeanAnswerItem> mLoader;
	private View mHeader;
	private HeaderHolder mHeaderHolder;
	private ArrayList<BeanAnswerItem> mDataSource;
    private BeanNearbyItem mFromNearby;

    public FragmentQANearby() {
	}

	public static FragmentQANearby newInstance(BeanRequestAnswerList b) {
		Bundle args = new Bundle();
		args.putSerializable(KEY_ARGS, b);
		FragmentQANearby fragmentQuestionList = new FragmentQANearby();
		fragmentQuestionList.setArguments(args);
		return fragmentQuestionList;
	}

	@Override
	protected int onLoadViewResource() {
		return R.layout.fragment_answer_list;
	}

	@Override
	protected void onViewDidLoad(Bundle savedInstanceState) {
		super.onViewDidLoad(savedInstanceState);
		if (getData() != null) {
			headerViewFromNearby();
			setupList();
		} else {
			L.dbg("get data is null!");
		}
	}

	private void headerViewFromNearby() {
		mHeaderHolder = new HeaderHolder();
		mHeader = getActivity().getLayoutInflater().inflate(
				R.layout.layout_question_top, null);
		mHeaderHolder.author = (TextView) mHeader.findViewById(R.id.author);
		mHeaderHolder.title = (TextView) mHeader
				.findViewById(R.id.questionText);
		mListView.getRefreshableView().addHeaderView(mHeader);

		Activity activity = getActivity();
		if (activity instanceof ActivityDetail) {
			ActivityDetail activityDetail = (ActivityDetail) activity;
            mFromNearby = activityDetail.getData().fromNearby;
            if(mFromNearby == null) return;
			mHeaderHolder.author.setText(mFromNearby.getSign());
			mHeaderHolder.title.setText(mFromNearby.getQuest());
			LikePanelHolder.Extra extra = new LikePanelHolder.Extra();
			extra.aid = 0;
            extra.qid = mFromNearby.getQuestId();
			LikePanelHolder likePanelHolder = new LikePanelHolder(extra,
					mHeader) {
                @Override
                public void onPostSuccess(boolean isLike) {
                    super.onPostSuccess(isLike);
                    int count = isLike ? Integer.valueOf(like.getText()
                            .toString()) : Integer.valueOf(unLike.getText()
                            .toString());
                    count++;

                    if (isLike) {
                        mFromNearby.setLikeNum(count);
                        mFromNearby.setLikeFlag(1);
                    } else {
                        mFromNearby.setUnlikeNum(count);
                        mFromNearby.setLikeFlag(2);
                    }
                    setData(mFromNearby);
                }
            };
            likePanelHolder.listenForChecking();
            likePanelHolder.setData(mFromNearby);

		}

	}

	private void setupList() {
		mLoader = new AbsWorker.AbsLoader<ViewAnswerItem, BeanAnswerItem>() {
			@Override
			public String parseNextUrl(JSONObject response) {
				return Constant.API;
			}

            @Override
            public JSONObject parseNextJSON(JSONObject response) {
                ResponseQuestion responseQuestion = JacksonWrapper.json2Bean(
                        response, ResponseQuestion.class);
                ArrayList<BeanAnswerItem> data = responseQuestion.getReturnData().getContData();
                if(data.size() > 0 ) {
                    final Application.AppExtraInfo info = Application.getInstance().getAppExtraInfo();
                    int lastId = data.get(data.size() - 1).getAnsId();
                    BeanRequestAnswerList bean = getData();
                    bean.setIdType(1);
                    bean.setLastId(lastId);
                    return JacksonWrapper.bean2Json(bean);
                }
                return null;
            }

            @Override
			public ArrayList<BeanAnswerItem> parseJSON2ArrayList(
					JSONObject response) {
				ResponseQuestion responseQuestion = JacksonWrapper.json2Bean(
						response, ResponseQuestion.class);
				L.dbg("FragmentAnswerList response :" + response);
				if (responseQuestion != null) {
					mDataSource = responseQuestion.getReturnData()
							.getContData();
					return mDataSource;
				} else {
					L.dbg("responseQuestion is null !");
					return null;
				}
			}

			@Override
			public void updateItemUI(final int position, BeanAnswerItem data,
					ViewAnswerItem itemView) {
				itemView.setData(data);
                itemView.setFloor(position);
				LikePanelHolder.Extra extra = new LikePanelHolder.Extra();
				extra.aid = data.getAnsId();
				LikePanelHolder likePanelHolder = new LikePanelHolder(extra,
						itemView) {

					@Override
					public void onPostSuccess(boolean isLike) {
						int count = isLike ? Integer.valueOf(like.getText()
								.toString()) : Integer.valueOf(unLike.getText()
								.toString());
						count++;

						if (isLike) {
							mDataSource.get(position).setLikeFlag(1);
							mDataSource.get(position).setLikeNum(count);
						} else {
							mDataSource.get(position).setLikeFlag(2);
							mDataSource.get(position).setUnlikeNum(count);
						}
						mListView.notifyDataSetChanged();
					}
				};
				likePanelHolder.listenForChecking();;

			}

			@Override
			public ViewAnswerItem makeItem(LayoutInflater inflater,
					int position, View convertView, ViewGroup parent) {
				return new ViewAnswerItem(getActivity());
			}
		};
		requestListView();
	}

	private void requestListView() {
		JSONObject jo = JacksonWrapper.bean2Json(getData());
		mListView.request(Constant.API, mLoader, jo);
	}

	public BeanRequestAnswerList getData() {
		return (BeanRequestAnswerList) getArguments().getSerializable(KEY_ARGS);
	}

	public void refreshAnswer() {
		requestListView();
	}

	static class HeaderHolder {
		TextView title;
		TextView author;
	}
}

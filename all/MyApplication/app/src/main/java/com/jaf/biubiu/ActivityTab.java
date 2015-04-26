package com.jaf.biubiu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jaf.bean.BeanTopicItem;
import com.jaf.bean.ResponseUser;
import com.jaf.jcore.Application;
import com.jaf.jcore.BaseActionBarActivity;
import com.jaf.jcore.BindView;
import com.jaf.jcore.Http;
import com.jaf.jcore.HttpCallBack;
import com.jaf.jcore.JacksonWrapper;

import org.json.JSONObject;

public class ActivityTab extends BaseActionBarActivity
		implements
			Constant.BottomTabBar {

	private Fragment mLastShowing;
	private Fragment[] mTabFragments;

	@BindView(id = R.id.bottomTabBar)
	private RadioGroup mBottomTabBar;

	private View mActionBarView;
	private ActionBarViewHolder mHolder;
    private int mFragmentIndex;

    @Override
	protected int onLoadViewResource() {
		return R.layout.activity_tab;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		enableTitleDisplayHomeAsUp(false);
	}

	@Override
	protected void onViewDidLoad(Bundle savedInstanceState) {
		initBottomBar();
		initFragments();
		switchFragment(FIRST_TAB);
	}

	@Override
	protected View getActionBarView() {
		mActionBarView = getLayoutInflater().inflate(
				R.layout.view_activity_tab_action_bar, null);
		mHolder = new ActionBarViewHolder();
		mHolder.title = (TextView) mActionBarView.findViewById(R.id.barTitle);
		mHolder.option = (ImageView) mActionBarView
				.findViewById(R.id.barOption);
		return mActionBarView;
	}

	private void initBottomBar() {
		final int firstTabId = FIRST_TAB;
		for (int i = 0; i < mBottomTabBar.getChildCount(); i++) {
			String text = getString(TAB_TITLES[i]);
			id(firstTabId + i, RadioButton.class).setText(text);
		}

		mBottomTabBar
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switchFragment(checkedId);
					}
				});
	}

	private void initFragments() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction trs = fm.beginTransaction();
		if (mTabFragments == null) {
			mTabFragments = new Fragment[TAB_COUNT];
			for (int i = 0; i < TAB_COUNT; i++) {
				mTabFragments[i] = TabFragmentFactory.createMain(i, null);
				trs.add(R.id.container, mTabFragments[i], "" + i);
				trs.hide(mTabFragments[i]);
			}
		}
		trs.commit();
	}

	private void switchFragment(int tabId) {
		final int fragmentIndex = tabId - FIRST_TAB;
        mFragmentIndex = fragmentIndex;
		final Fragment f = mTabFragments[fragmentIndex];
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();

		if (mLastShowing != null) {
			trans.hide(mLastShowing);
		}
		trans.show(f);
		mLastShowing = f;
		trans.commit();

		updateActionBar(fragmentIndex);
	}


    private void updateActionBar(final int index) {
		int textResId = R.string.app_name;
		int iconRes = R.drawable.sel_edit_btn;
		switch (index) {
			case 1 :
				textResId = R.string.lookAround;
				iconRes = R.drawable.sel_add_btn;
				break;
			case 2 :
				textResId = R.string.message;
				iconRes = 0;
				break;
			case 3 :
				textResId = R.string.mine;
				iconRes = R.drawable.sel_setting_btn;
				break;
		}
		// title
		String title = getString(textResId);
		String city = Application.getInstance().getAppExtraInfo().city;
		if (index == 0 && !TextUtils.isEmpty(city)) {
			title = city;
		}
		mHolder.title.setText(title);
		// title left
		int d = 0;
		if (index == 0) {
			d = R.drawable.ic_lbs_white;
		}
		if (index == 1) {
			d = R.drawable.sel_look_around_btn;
		}
		mHolder.title.setCompoundDrawablesWithIntrinsicBounds(d, 0, 0, 0);
		// option
		mHolder.option.setImageResource(iconRes);

		setupActionBarOptionEvent(index);
		setupActionBarTItleEvent(index);
	}

	private void setupActionBarOptionEvent(final int index) {
		mHolder.option.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (index == 0) {
					ActivityPublish.Extra extra = new ActivityPublish.Extra();
					extra.unionId = 0;
					extra.isUnionQuestion = false;
					ActivityPublish.start(ActivityTab.this, extra);
				}

				if (index == 1) {
					startCreateUnion();
				}

				if (index == 3) {
					ActivitySetting.start(ActivityTab.this);
				}
			}

		});
	}

	private void startCreateUnion() {
		Http http = new Http();
		http.url(Constant.API).JSON(U.buildUser()).post(new HttpCallBack() {
            @Override
            public void onResponse(JSONObject response) {
                super.onResponse(response);
                ResponseUser responseUser = JacksonWrapper.json2Bean(response,
                        ResponseUser.class);
                if (responseUser != null && responseUser.getReturnData().getOtherLikeNum() > 10) {
                    ActivityCreateUnion.start(ActivityTab.this);
                } else {
                   Toast t = Toast.makeText(ActivityTab.this, R.string.needMoreLike, Toast.LENGTH_LONG);
                   t.setGravity(Gravity.TOP, 0 , 100);
                   t.show();
                }
            }
        });

	}

	private void setupActionBarTItleEvent(final int index) {
		// option title
		mHolder.title.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (index == 1) {
                    //随便看看逻辑
					Http http = new Http();
					http.url(Constant.API).JSON(U.buildRandomTopic())
							.post(new HttpCallBack() {
								@Override
								public void onResponse(JSONObject response) {
									super.onResponse(response);
									JSONObject returnData = response
											.optJSONObject("returnData");
									L.dbg("random topic item :" + response);
									BeanTopicItem item = JacksonWrapper
											.json2Bean(returnData,
													BeanTopicItem.class);
									if (item != null) {
										ActivityUnionTopic.Extra extra = new ActivityUnionTopic.Extra();
										extra.fromTopic = U
												.buildTopicQuestionListArg(item
														.getUnionId());
										extra.topicTitle = item.getUnionName();
                                        ActivityUnionTopic.start(ActivityTab.this,
												extra);
									}
								}
							});
				}
			}
		});
	}

    public void setLocTitle(String locDesc) {
        if(mFragmentIndex == 0) {
            setTitle(locDesc);
        }
    }

    public static class ActionBarViewHolder {
		TextView title;
		ImageView option;
	}
}

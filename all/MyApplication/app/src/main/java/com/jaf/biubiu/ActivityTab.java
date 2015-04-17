package com.jaf.biubiu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jaf.jcore.BaseActionBarActivity;
import com.jaf.jcore.BindView;


public class ActivityTab extends BaseActionBarActivity implements Constant.BottomTabBar {

    private Fragment mLastShowing;
    private Fragment[] mTabFragments;

    @BindView(id = R.id.bottomTabBar)
    private RadioGroup mBottomTabBar;

    private View mActionBarView;
    private ActionBarViewHolder mHolder;

    @Override
    protected int onLoadViewResource() {
        return R.layout.activity_tab;
    }

    @Override
    protected void onViewDidLoad(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        initBottomBar();
        initFragments();
        switchFragment(FIRST_TAB);
    }

    @Override
    protected View getActionBarView() {
        mActionBarView = getLayoutInflater().inflate(R.layout.view_activity_tab_action_bar, null);
        mHolder = new ActionBarViewHolder();
        mHolder.title = (TextView) mActionBarView.findViewById(R.id.barTitle);
        mHolder.option = (ImageView) mActionBarView.findViewById(R.id.barOption);
        return mActionBarView;
    }

    private void initBottomBar() {
        final int firstTabId = FIRST_TAB;
        for (int i = 0; i < mBottomTabBar.getChildCount(); i++) {
            String text = getString(TAB_TITLES[i]);
            id(firstTabId + i, RadioButton.class).setText(text);
        }

        mBottomTabBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

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

    private void updateActionBar(int index) {
        int textResId = R.string.app_name;
        int iconRes = R.drawable.sel_edit_btn;
        switch (index) {
            case 1:
                textResId = R.string.lookAround;
                iconRes = R.drawable.sel_add_btn;
                break;
            case 2:
                textResId = R.string.message;
                iconRes = 0;
                break;
            case 3:
                textResId = R.string.mine;
                iconRes = R.drawable.sel_setting_btn;
                break;
        }
        mHolder.option.setImageResource(iconRes);
        mHolder.title.setText(textResId);
    }

    public static class ActionBarViewHolder {
        TextView title;
        ImageView option;
    }
}

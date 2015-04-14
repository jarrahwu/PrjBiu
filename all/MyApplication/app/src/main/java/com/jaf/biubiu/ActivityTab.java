package com.jaf.biubiu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jaf.jcore.BindView;
import com.jaf.jcore.BindableActivity;


public class ActivityTab extends BindableActivity implements Constant.BottomTabBar{

    private Fragment mLastShowing;
    private Fragment[] mTabFragments;

    @BindView(id = R.id.bottomTabBar)
    private RadioGroup mBottomTabBar;

    @Override
    protected int onLoadViewResource() {
        return R.layout.activity_tab;
    }

    @Override
    protected void onViewDidLoad(Bundle savedInstanceState) {
        initBottomBar();
        initFragments();
        switchFragment(FIRST_TAB);
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
    }
}

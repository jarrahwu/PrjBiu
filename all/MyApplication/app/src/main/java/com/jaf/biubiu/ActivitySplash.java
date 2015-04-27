package com.jaf.biubiu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jaf.jcore.BindableActivity;

import cn.jpush.android.api.JPushInterface;

public class ActivitySplash extends BindableActivity {

	private static final long SPLASH_DELAY = 1000;
	private final Handler mHandler = new Handler();
	private Runnable mDelayStart;

	@Override
	protected int onLoadViewResource() {
		return R.layout.activity_splash;
	}

	@Override
	protected void onViewDidLoad(Bundle savedInstanceState) {
		mDelayStart = new Runnable() {

			@Override
			public void run() {
				start();
			}
		};

		splash();
	}

	private void splash() {
		mHandler.postDelayed(mDelayStart, SPLASH_DELAY);
	}

	private void start() {
		mHandler.removeCallbacks(mDelayStart);
		Intent i = new Intent(ActivitySplash.this, ActivityTab.class);
		startActivity(i);
		finish();
	}

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
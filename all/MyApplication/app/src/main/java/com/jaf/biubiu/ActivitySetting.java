package com.jaf.biubiu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jaf.jcore.BaseActionBarActivity;
import com.jaf.jcore.BindView;

public class ActivitySetting extends BaseActionBarActivity {

	@BindView(id = R.id.feedback, onClick = "onFeedbackClick")
	private TextView mFeedback;

	@BindView(id = R.id.about, onClick = "onABoutClick")
	private TextView mAbout;

	@BindView(id = R.id.contactUs, onClick = "onContactUsClick")
	private TextView mContactUs;

	@BindView(id = R.id.userTerms, onClick = "onUserTermsClick")
	private TextView mUserTerms;

	@Override
	protected int onLoadViewResource() {
		return R.layout.activity_setting;
	}

	@Override
	protected void onViewDidLoad(Bundle savedInstanceState) {
		mFeedback.setText(R.string.userFeedback);
		mAbout.setText(getString(R.string.aboutUs));
		mContactUs.setText(getString(R.string.contactUs));
		mUserTerms.setText(getString(R.string.userTerms));
	}

	public static void start(Activity activity) {
		activity.startActivity(new Intent(activity, ActivitySetting.class));
	}

	public void onFeedbackClick(View v) {
		ActivityFeedback.start(this);
	}

	public void onContactUsClick(View v) {
		ActivityContactUs.start(this);
	}

	public void onABoutClick(View v) {
		ActivityAbout.start(this);
	}

	public void onUserTermsClick(View v) {
		ActivityTerms.start(this);
	}
}

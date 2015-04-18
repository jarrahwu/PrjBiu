package com.jaf.biubiu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jaf.jcore.BaseActionBarActivity;
import com.jaf.jcore.BindView;


public class ActivityPublish extends BaseActionBarActivity {

    @BindView(id = R.id.location)
    private TextView mLocation;

    @BindView(id = R.id.refresh)
    private View mRefreshLocation;

    @BindView(id = R.id.content)
    private EditText mContent;

    @BindView(id = R.id.sign)
    private EditText mSign;

    
    @Override
    protected int onLoadViewResource() {
        return R.layout.activity_publish;
    }

    @Override
    protected void onViewDidLoad(Bundle savedInstanceState) {

    }

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, ActivityPublish.class));
    }
}

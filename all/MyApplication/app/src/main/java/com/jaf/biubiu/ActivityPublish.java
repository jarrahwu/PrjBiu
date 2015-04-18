package com.jaf.biubiu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.jaf.jcore.Application;
import com.jaf.jcore.BaseActionBarActivity;
import com.jaf.jcore.BindView;
import com.jaf.jcore.Http;
import com.jaf.jcore.HttpCallBack;
import com.jaf.jcore.JacksonWrapper;

import org.json.JSONObject;


public class ActivityPublish extends BaseActionBarActivity {

    private static final String TAG = "Activity Publish";
    @BindView(id = R.id.location)
    private TextView mLocation;

    @BindView(id = R.id.refresh, onClick = "refreshLocation")
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
    protected View getActionBarView() {
        mActionBarView = getLayoutInflater().inflate(R.layout.view_action_bar_publish, null);
        mActionBarView.findViewById(R.id.barOption).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publish();
            }
        });
        return mActionBarView;
    }

    @Override
    protected void onViewDidLoad(Bundle savedInstanceState) {
        updateLocation();
    }

    private void publish() {
        String text = mContent.getText().toString();
        boolean valid = !TextUtils.isEmpty(text) && text.length() >= 5;
        if(valid) {
            Http http = new Http(TAG);
            String sign = mSign.getText().toString();
            String locDesc = Application.getInstance().getAppExtraInfo().addrStr;
            String quest = mContent.getText().toString();
            JSONObject jo = JacksonWrapper.bean2Json(U.buildPublishQuestion(sign, locDesc, 1, 0, quest));
            http.url(Constant.API).JSON(jo).post(new HttpCallBack() {
                @Override
                public void onResponse(JSONObject response) {
                    super.onResponse(response);
                    Toast.makeText(ActivityPublish.this, R.string.publishSuccess, Toast.LENGTH_SHORT).show();
                    ActivityPublish.this.finish();
                }
            });
        }else{
            Toast.makeText(this, R.string.publish_at_leasst, Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshLocation(View v) {
        updateLocation();
    }

    private void updateLocation() {
        LocationManager.getInstance().requestLocation(new LocationManager.JLsn() {
            @Override
            public void onResult(double latitude, double longitude, BDLocation location) {
                super.onResult(latitude, longitude, location);
                Application.getInstance().getAppExtraInfo().lat = latitude;
                Application.getInstance().getAppExtraInfo().lon = longitude;
                Application.getInstance().getAppExtraInfo().city = location.getCity();
                Application.getInstance().getAppExtraInfo().addrStr = location.getAddrStr();
                mLocation.setText(location.getAddrStr());
            }
        });
    }

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, ActivityPublish.class));
    }
}

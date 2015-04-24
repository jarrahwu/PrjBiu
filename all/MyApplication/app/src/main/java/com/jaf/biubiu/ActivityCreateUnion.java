package com.jaf.biubiu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.jaf.jcore.BaseActionBarActivity;
import com.jaf.jcore.BindView;


public class ActivityCreateUnion extends BaseActionBarActivity {

    @BindView(id = R.id.capture)
    private ImageView mCapture;

    @BindView(id = R.id.unionEdit)
    private EditText mEditText;

    @BindView(id = R.id.locDesc)
    private TextView mLocDesc;

    @BindView(id = R.id.btnCreate, onClick = "onSubmitClick")
    private Button mSubmit;

    @Override
    protected int onLoadViewResource() {
        return R.layout.activity_create_union;
    }

    @Override
    protected void onViewDidLoad(Bundle savedInstanceState) {
        LocationManager.getInstance().requestLocation(new LocationManager.JLsn() {
            @Override
            public void onResult(double latitude, double longitude, BDLocation location) {
                super.onResult(latitude, longitude, location);
                mLocDesc.setText(location.getAddrStr());
            }
        });
    }

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, ActivityCreateUnion.class));
    }
}

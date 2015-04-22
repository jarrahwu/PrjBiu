package com.jaf.biubiu;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jaf.jcore.BaseActionBarActivity;
import com.jaf.jcore.BindView;
import com.jaf.jcore.Http;
import com.jaf.jcore.HttpCallBack;

import org.json.JSONObject;


public class ActivityFeedback extends BaseActionBarActivity {

    @BindView(id = R.id.msgEdit)
    private EditText mContent;

    @BindView(id = R.id.send, onClick = "onSendClick")
    private View mSend;

    @Override
    protected int onLoadViewResource() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void onViewDidLoad(Bundle savedInstanceState) {
        mContent.setHint(R.string.feedback_tips);
    }

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, ActivityFeedback.class));
    }

    public void onSendClick(View v) {
        Http http = new Http();
        if(TextUtils.isEmpty(mContent.getText())) {
            Toast.makeText(this, R.string.saySomething, Toast.LENGTH_SHORT).show();
        }else{
            JSONObject jo = U.buildPostFeedback(mContent.getText().toString());
            http.url(Constant.API).JSON(jo).post(new HttpCallBack(){
                @Override
                public void onResponse(JSONObject response) {
                    super.onResponse(response);
                    Toast.makeText(ActivityFeedback.this, R.string.feedback_success, Toast.LENGTH_SHORT).show();
                    mContent.setText("");
                    U.hideSoftKeyboard(ActivityFeedback.this);
                }
            });
        }

    }
}

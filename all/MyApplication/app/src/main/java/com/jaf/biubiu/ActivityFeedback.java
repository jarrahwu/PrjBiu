package com.jaf.biubiu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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

import java.io.InputStream;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.Danmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.android.DanmakuGlobalConfig;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.DanmakuFactory;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.parser.android.BiliDanmukuParser;
import master.flame.danmaku.ui.widget.DanmakuView;


public class ActivityFeedback extends BaseActionBarActivity {

    @BindView(id = R.id.msgEdit)
    private EditText mContent;

    @BindView(id = R.id.send, onClick = "onSendClick")
    private View mSend;

    @BindView(id = R.id.danmu)
    private DanmakuView mDanmakuView;

    private BaseDanmakuParser mParser;

    @Override
    protected int onLoadViewResource() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void onViewDidLoad(Bundle savedInstanceState) {
        mContent.setHint(R.string.feedback_tips);
        mParser = createParser(this.getResources().openRawResource(R.raw.empty));
        DanmakuGlobalConfig.DEFAULT.setDanmakuStyle(DanmakuGlobalConfig.DANMAKU_STYLE_STROKEN, 3).setDuplicateMergingEnabled(false);
        mDanmakuView.setCallback(new DrawHandler.Callback() {

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void prepared() {
                mDanmakuView.start();
            }
        });
        mDanmakuView.enableDanmakuDrawingCache(true);
        mDanmakuView.prepare(mParser);
        mDanmakuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDanmaku(false);
            }
        });
    }

    private BaseDanmakuParser createParser(InputStream stream) {

        if(stream==null){
            return new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }


        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);

        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;

    }

    private void addDanmaku(boolean islive) {
        BaseDanmaku danmaku = DanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL, 200f, 100f, 1f);
//        BaseDanmaku danmaku = DanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        //for(int i=0;i<100;i++){
        //}
        danmaku.text = "这是一条弹幕, 继续点击屏幕吧" + System.nanoTime();
        danmaku.padding = 5;
        danmaku.priority = 1;
        danmaku.isLive = islive;
        danmaku.time = mDanmakuView.getCurrentTime() + 1200;
        danmaku.textSize = 25f;
        danmaku.textColor = Color.RED;
        danmaku.textShadowColor = Color.parseColor("#aabbcc");
        //danmaku.underlineColor = Color.GREEN;
        danmaku.borderColor = Color.GREEN;
        mDanmakuView.addDanmaku(danmaku);
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
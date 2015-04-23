package com.jaf.biubiu;

import android.graphics.Color;

import com.jaf.jcore.Application;

import java.io.InputStream;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.android.DanmakuGlobalConfig;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.DanmakuFactory;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.parser.android.BiliDanmukuParser;

/**
 * Created by jarrah on 2015/4/23.
 */
public class DanmuHelper {


    private IDanmakuView mDanmakuView;
    private BaseDanmakuParser mParser;

    private static DanmuHelper sDanmuHelper = new DanmuHelper();

    public static void setupDanmu(IDanmakuView view) {
        sDanmuHelper.mDanmakuView = view;
        sDanmuHelper.init();;
    }

    private BaseDanmakuParser createParser(InputStream stream) {

        if (stream == null) {
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

    public static void addDanmaku(IDanmakuView danmakuView, boolean islive, String text) {
        BaseDanmaku danmaku = DanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL, 200f, 100f, 1f);
//        BaseDanmaku danmaku = DanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        //for(int i=0;i<100;i++){
        //}
        if (text == null)
            danmaku.text = "这是一条弹幕, 继续点击屏幕吧" + System.nanoTime();
        else
            danmaku.text = text;

        danmaku.padding = 5;
        danmaku.priority = 1;
        danmaku.isLive = islive;
        danmaku.time = danmakuView.getCurrentTime() + 1200;
        danmaku.textSize = 25f;
        danmaku.textColor = Color.WHITE;
        danmaku.textShadowColor = Color.parseColor("#838383");
        //danmaku.underlineColor = Color.GREEN;
//        danmaku.borderColor = Color.GREEN;
        danmakuView.addDanmaku(danmaku);
    }

    public void init() {
        mParser = createParser(Application.getInstance().getResources().openRawResource(R.raw.empty));
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
    }
}

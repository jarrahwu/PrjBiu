package com.jaf.biubiu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;

import com.jaf.jcore.BindableActivity;


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


    //    private MapView mMapView;
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        SDKInitializer.initialize(getApplicationContext());
//        setContentView(R.layout.activity_main);
//        mMapView = (MapView) findViewById(R.id.bmapView);
//
//        LocationManager.getInstance().requestLocation(new LocationManager.JLsn(){
//            @Override
//            public void onResult(double latitude, double longitude) {
//                Log.e("lat", "" + latitude);
//                Log.e("lon", "" + longitude);
//            }
//        });
//
//        L.e(Device.getId(this));
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//        mMapView.onDestroy();
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
//        mMapView.onResume();
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
//        mMapView.onPause();
//    }


}
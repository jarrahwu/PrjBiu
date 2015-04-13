package com.example.jarrah.helloas;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class ActivityMain extends ActionBarActivity {

    private View mContainer;

    private static AccelerateDecelerateInterpolator sAdi = new AccelerateDecelerateInterpolator();
    private float openedX = 100;
    private float openedY = 100;
    private float closedY = 0;
    private float closedX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
//        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        menu.setMenu(R.layout.menu);
        menu.setTouchModeBehind(SlidingMenu.SLIDING_CONTENT);


        menu.setOnPageScrollListener(new SlidingMenu.OnPageScrollListener() {
            @Override
            public void onPageScroll(int position, float positionOffset, int positionOffsetPixels) {
                menu.setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer() {
                    @Override
                    public void transformCanvas(Canvas canvas, float percentOpen) {
                        Log.e("percent", "" + percentOpen);

                        float f = sAdi.getInterpolation(percentOpen);
                        float sx = Math.max(1 - percentOpen, 0.5f);
                        canvas.scale(sx, sx, 0.5f, 0.5f);
                    }
                });
            }
        });

//        menu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
//            @Override
//            public void onOpen() {
//                menu.setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer() {
//                    @Override
//                    public void transformCanvas(Canvas canvas, float percentOpen) {
//                        Log.e("percent", "" + percentOpen);
//
//                        float f = sAdi.getInterpolation(percentOpen);
//                        canvas.scale((openedX - closedX) * f + closedX,
//                                (openedY - closedY) * f + closedY, 0.5f, 0.5f);
//                    }
//                });
//            }
//        });

        menu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
            @Override
            public void onClosed() {
                menu.setAboveCanvasTransformer(null);
            }
        });



        menu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            @Override
            public void onOpened() {
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

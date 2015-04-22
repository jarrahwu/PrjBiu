package com.jaf.biubiu;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jaf.jcore.BaseActionBarActivity;


public class ActivityAbout extends BaseActionBarActivity {


    @Override
    protected int onLoadViewResource() {
        return R.layout.activity_about;
    }

    @Override
    protected void onViewDidLoad(Bundle savedInstanceState) {

    }

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, ActivityAbout.class));
    }
}

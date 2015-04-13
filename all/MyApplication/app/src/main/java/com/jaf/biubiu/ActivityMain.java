package com.jaf.biubiu;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jaf.jcore.BaseActionBarActivity;


public class ActivityMain extends BaseActionBarActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int onLoadViewResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onViewDidLoad(Bundle savedInstanceState) {

    }
}

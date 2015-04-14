package com.jaf.biubiu;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.jaf.jcore.Application;

/**
 * Created by jarrah on 2015/4/14.
 */
public class LocationManager {

    public static LocationManager sLocationManager;

    LocationClient mLocationClient;

    public LocationManager() {
        mLocationClient = new LocationClient(Application.getInstance().getApplicationContext());
    }

    public synchronized static LocationManager getInstance() {
        if (sLocationManager == null) {
            sLocationManager = new LocationManager();
        }
        return sLocationManager;
    }

    public void requestLocation(BDLocationListener l) {
        mLocationClient.registerLocationListener(l);
        mLocationClient.start();
    }

    public static class JLsn implements  BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            LocationManager.getInstance().mLocationClient.unRegisterLocationListener(this);
            LocationManager.getInstance().mLocationClient.stop();
            onResult(bdLocation.getLatitude(), bdLocation.getLongitude());
        }

        public void onResult(double latitude, double longitude) {

        }
    }


}

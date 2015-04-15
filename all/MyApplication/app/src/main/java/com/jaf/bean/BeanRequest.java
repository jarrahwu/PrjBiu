package com.jaf.bean;

/**
 * Created by jarrah on 2015/4/15.
 */
public class BeanRequest {
    private String appVersion;
    private int latitude;
    private int longitude;
    private String dvcId;
    private int cmd;

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDvcId() {
        return dvcId;
    }

    public void setDvcId(String dvcId) {
        this.dvcId = dvcId;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = (int) (latitude * 1000000);
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = (int) (longitude * 1000000);
    }
}

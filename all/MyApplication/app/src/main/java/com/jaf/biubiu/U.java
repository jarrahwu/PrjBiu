package com.jaf.biubiu;


import android.util.Base64;

import com.jaf.bean.BeanRequest;
import com.jaf.bean.BeanRequestNearby;
import com.jaf.jcore.Application;
import com.jaf.jcore.JacksonWrapper;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by jarrah on 2015/4/15.
 */
public class U implements Constant{
    public static JSONObject buidRequest(int cmd, double lat, double lon) {
        BeanRequest br = new BeanRequest();
        br.setAppVersion(VER);
        br.setCmd(cmd);
//        br.setDvcId(Device.getId(Application.getInstance().getApplicationContext()));
        br.setDvcId("075D690D-FB6F-4FF4-AC31-99C388FD7602");
        br.setLatitude(lat);
        br.setLongitude(lon);
        return JacksonWrapper.bean2Json(br);
    }

    public static JSONObject buildNeayBy(double lat, double lon, boolean fresh, int lastId) {
        BeanRequestNearby brn = new BeanRequestNearby();
        brn.setAppVersion(VER);
        brn.setCmd(CMD.LIST_NEARBY);
        brn.setDvcId(Device.getId(Application.getInstance().getApplicationContext()));
        brn.setLatitude(lat);
        brn.setLongitude(lon);
        brn.setIdType(fresh ? 0 : 1);
        brn.setLastId(lastId);
        return JacksonWrapper.bean2Json(brn);
    }

    public static String b642s(String str) {
        byte[] data = Base64.decode(str, Base64.NO_WRAP);
        try {
            String text = new String(data, "UTF-8");
            return text;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "N/A";
        }
    }
}

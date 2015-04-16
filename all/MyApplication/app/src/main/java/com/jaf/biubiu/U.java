package com.jaf.biubiu;


import android.util.Base64;

import com.jaf.bean.BeanRequest;
import com.jaf.bean.BeanRequestNearby;
import com.jaf.bean.BeanRequestQuestionList;
import com.jaf.bean.BeanRequestTopic;
import com.jaf.bean.BeanRequestUser;
import com.jaf.jcore.Application;
import com.jaf.jcore.JacksonWrapper;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by jarrah on 2015/4/15.
 */
public class U implements Constant{
    public static JSONObject buildRequest(int cmd, double lat, double lon) {
        BeanRequest br = new BeanRequest();
        br.setAppVersion(VER);
        br.setCmd(cmd);
        br.setDvcId(Device.getId(Application.getInstance().getApplicationContext()));
        br.setLatitude(lat);
        br.setLongitude(lon);
        return JacksonWrapper.bean2Json(br);
    }

    public static JSONObject buildNearby(double lat, double lon, boolean fresh, int lastId) {
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
        byte[] data = Base64.decode(str, Base64.DEFAULT);
        try {
            String text = new String(data, "UTF-8");
            return text;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "N/A";
        }
    }

    public static JSONObject buildTopic(boolean fresh, int lastId, int type) {
        BeanRequestTopic brt = new BeanRequestTopic();
        brt.setAppVersion(VER);
        brt.setCmd(CMD.LIST_TOPIC);
        brt.setDvcId(Device.getId(Application.getInstance().getApplicationContext()));
        brt.setLatitude(Application.getInstance().getAppExtraInfo().lat);
        brt.setLongitude(Application.getInstance().getAppExtraInfo().lon);
        brt.setIdType(fresh ? 0 : 1);
        brt.setLastId(lastId);
        brt.setType(type);
        return JacksonWrapper.bean2Json(brt);
    }

    public static JSONObject buildUser() {
        BeanRequestUser bru = new BeanRequestUser();
        bru.setDvcId(Device.getId(Application.getInstance().getApplicationContext()));
        bru.setCmd(CMD.USER_INFO);
        bru.setAppVersion(VER);
        return JacksonWrapper.bean2Json(bru);
    }

    public static JSONObject buildQuestion(boolean fresh, int lastId, int questId) {
        BeanRequestQuestionList brq = new BeanRequestQuestionList();
        brq.setAppVersion(VER);
        brq.setCmd(CMD.LIST_QUESTION);
        brq.setDvcId(Device.getId(Application.getInstance().getApplicationContext()));
        brq.setIdType(fresh ? 0 : 1);
        brq.setLastId(lastId);
        brq.setQuestId(questId);
        return JacksonWrapper.bean2Json(brq);
    }

    public static BeanRequestQuestionList buildQuestionArg(boolean fresh, int lastId, int questId) {
        BeanRequestQuestionList brq = new BeanRequestQuestionList();
        brq.setAppVersion("3.1");
        brq.setCmd(CMD.LIST_QUESTION);
        brq.setDvcId(Device.getId(Application.getInstance().getApplicationContext()));
        brq.setIdType(fresh ? 0 : 1);
        brq.setLastId(lastId);
        brq.setQuestId(questId);
        return brq;
    }
}

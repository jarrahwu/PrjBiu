package com.jaf.bean;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Created by jarrah on 2015/4/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseTopic {

    private String statusInfo;
    private int statusCode;
    private ReturnData returnData;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReturnData {
        private ArrayList<BeanTopicItem> contData;

        public ArrayList<BeanTopicItem> getContData() {
            return contData;
        }

        public void setContData(ArrayList<BeanTopicItem> contData) {
            this.contData = contData;
        }
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public ReturnData getReturnData() {
        return returnData;
    }

    public void setReturnData(ReturnData returnData) {
        this.returnData = returnData;
    }
}

package com.jaf.biubiu;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.jaf.bean.BeanRequestQuestionList;
import com.jaf.jcore.BindableActivity;

import java.io.Serializable;


public class ActivityQuestion extends BindableActivity {

    private static final String KEY_ARGS = "question_data";
    private FragmentQuestionList mFragmentQuestionList;
    private Extra extra;

    @Override
    protected int onLoadViewResource() {
        return R.layout.activity_question;
    }

    @Override
    protected void onViewDidLoad(Bundle savedInstanceState) {
        extra = getData();
        if(extra == null) {
            L.dbg("activity question getData is null!");
            return;
        }else {
            BeanRequestQuestionList arg = U.buildQuestionArg(true, 0, extra.questId);
            mFragmentQuestionList = FragmentQuestionList.newInstance(arg);
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.container, mFragmentQuestionList);
            trans.commit();
        }
    }

    private Extra getData() {
        return (Extra) getIntent().getSerializableExtra(KEY_ARGS);
    }

    public static void start(Activity activity, final Extra extra) {
        Intent intent = new Intent();
        intent.putExtra(KEY_ARGS, extra);
        intent.setClass(activity, ActivityQuestion.class);
        activity.startActivity(intent);
    }

    public static class Extra implements Serializable{
        int questId;
    }

}

package com.jaf.biubiu;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jaf.bean.BeanAnswerItem;
import com.jaf.bean.BeanRequest;
import com.jaf.jcore.BindView;
import com.jaf.jcore.BindableView;

/**
 * Created by jarrah on 2015/4/17.
 */
public class ViewAnswerItem extends BindableView {

    @BindView(id = R.id.likeCheck)
    public CheckBox mLike;

    @BindView(id = R.id.unlikeCheck)
    public CheckBox mUnLike;

    @BindView(id = R.id.content)
    private TextView mContent;

    @BindView(id = R.id.time)
    private TextView mTime;

    public ViewAnswerItem(Context context) {
        super(context);
    }

    @Override
    public void onViewDidLoad() {
    }

    @Override
    public int onLoadViewResource() {
        return R.layout.view_answer_item;
    }

    public void setData(BeanAnswerItem beanAnswerItem) {
        if (beanAnswerItem != null) {
            mContent.setText(beanAnswerItem.getAns());
            mTime.setText(String.valueOf(beanAnswerItem.getPubTime()));
            mLike.setText(String.valueOf(beanAnswerItem.getLikeNum()));
            mUnLike.setText(String.valueOf(beanAnswerItem.getLikeNum()));
        }
    }

}

package com.jaf.biubiu;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaf.bean.BeanMsgItem;
import com.jaf.jcore.BindView;
import com.jaf.jcore.BindableView;

/**
 * Created by jarrah on 2015/4/21.
 */
public class ViewMsgItem extends BindableView {

    @BindView(id = R.id.msgTypeIcon)
    private ImageView mMsgIcon;

    @BindView(id = R.id.msgTypeDesc)
    private TextView mMsgTypeDesc;

    @BindView(id = R.id.time)
    private TextView mTime;

    @BindView(id = R.id.msgContent)
    private TextView mMsgContent;


    public ViewMsgItem(Context context) {
        super(context);
    }

    @Override
    public void onViewDidLoad() {

    }

    public void setData(BeanMsgItem data) {
        mTime.setText("" + data.getPubTime());
    }

    @Override
    public int onLoadViewResource() {
        return R.layout.view_msg_item;
    }


}

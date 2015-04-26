package com.jaf.biubiu;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.jaf.bean.BeanMsgItem;
import com.jaf.jcore.Application;
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
        loadImage(data.getTypePic());

        String title = getResources().getString(R.string.noRelText);
        String content = getResources().getString(R.string.noRelText);
        if (data.getType() == 1) { //reply
            title = data.getAns();
            content = data.getQuest();
        } else if (data.getType() == 2) {// like
            title = data.getAnsId() == 0 ? getResources().getString(R.string.qLike) : getResources().getString(R.string.commentLike);
            content = data.getAnsId() == 0 ? data.getQuest() : data.getAns();
        } else if (data.getType() == 4) {//union
            title = data.getIsPass() == 1 ? getResources().getString(R.string.unionPass) : getResources().getString(R.string.unionBlock);
            content = data.getUnionName();
        } else if (data.getType() == 5) {//comment reply
            title = data.getSecAns();
            content = data.getAns();
        }

        mMsgTypeDesc.setText(title);
        mMsgContent.setText(content);
    }

    @Override
    public int onLoadViewResource() {
        return R.layout.view_msg_item;
    }


    private void loadImage(String url) {
        Application.getInstance().getAQuery().id(mMsgIcon).image(url, false, true, 500, 0, new BitmapAjaxCallback() {
            @Override
            protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                super.callback(url, iv, bm, status);
            }
        });
    }

}

package com.xinyi_tech.comm.adapter;


import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyi_tech.comm.R;
import com.xinyi_tech.comm.log.XinYiLog;
import com.xinyi_tech.comm.model.ImageTitleModel;
import com.xinyi_tech.comm.util.ImageLoaderUtils;
import com.xinyi_tech.comm.util.StringUtils2;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;


/**
 * Created by Fracesuit on 2017/7/21.
 */

public class ImageTitleAdapter<T extends ImageTitleModel> extends BaseQuickAdapter<T, BaseViewHolder> {

    public void addAllDataOnlyVisible(List<T> list) {
        getData().clear();
        ArrayList<T> objects = new ArrayList<>();
        for (T t : list) {
            if (t.isVisible()) objects.add(t);
        }
        addData(objects);
    }

    public ImageTitleAdapter() {
        super(R.layout.comm_item_image_title);
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageTitleModel item) {
        helper.setText(R.id.tv_icon_name, item.getTitle());
        helper.setImageResource(R.id.img_icon, item.getDrawableId());
        ImageView img = helper.getView(R.id.img_icon);
        if (StringUtils2.isEmpty(item.getIconPath())) {
            img.setImageResource(item.getDrawableId());
        } else {
            ImageLoaderUtils.showImage(img, item.getIconPath());
        }

        Badge badge = new QBadgeView(mContext).bindTarget(img);
        badge.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
            @Override
            public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                XinYiLog.e("dragStatedragState=" + dragState);
            }
        });
        badge.setBadgeGravity(Gravity.TOP | Gravity.END);
        badge.setBadgeTextSize(12, true);
        badge.setBadgePadding(4, true);
        badge.setExactMode(true);
        badge.setBadgeBackgroundColor(Color.RED);
        badge.setBadgeNumber(item.getBudgeCount());
    }
}

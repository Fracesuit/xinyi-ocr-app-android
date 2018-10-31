package com.xinyi_tech.freedom.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyi_tech.comm.help.recycleview.RecyclerViewHelper;
import com.xinyi_tech.freedom.R;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhiren.zhang on 2018/7/30.
 */

public class TestPopupWindow extends PopupWindow {


    @BindView(R.id.rlv_hourse)
    RecyclerView rlvHourse;
    @BindView(R.id.rlv_room)
    RecyclerView rlvRoom;
    private final BuildingAdapter buildingAdapter;
    private final HourseAdapter hourseAdapter;

    private ArrayList<BuildingModel> ff() {
        ArrayList<BuildingModel> buildingModels = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            buildingModels.add(new BuildingModel().setBuildingName("hh" + i).setSelect(i == 1));
        }
        return buildingModels;
    }

    public TestPopupWindow(Context context) {
        super(context);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(SizeUtils.dp2px(300));
        View view = LayoutInflater.from(context).inflate(R.layout.popup_fd_hourse, null, false);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
        setTouchable(true);
        setContentView(view);

        ButterKnife.bind(this, view);
        buildingAdapter = new BuildingAdapter();
        buildingAdapter.setNewData(ff());
        RecyclerViewHelper.initRecyclerViewV(rlvHourse, false, buildingAdapter);

        hourseAdapter = new HourseAdapter();
      /*  ArrayList<String> strings = new ArrayList<>();
        strings.add("1");
        strings.add("2");
        strings.add("3");
        strings.add("4");
        strings.add("5");
        strings.add("6");
        strings.add("7");
        strings.add("8");
        strings.add("9");
        strings.add("1");
        strings.add("2");
        strings.add("3");
        strings.add("4");
        strings.add("5");
        strings.add("6");
        strings.add("7");
        strings.add("8");
        strings.add("9");
        strings.add("1");
        strings.add("2");
        strings.add("3");
        strings.add("4");
        strings.add("5");
        strings.add("6");
        strings.add("7");
        strings.add("8");
        strings.add("9");
        strings.add("1");
        strings.add("2");
        strings.add("3");
        strings.add("4");
        strings.add("5");
        strings.add("6");
        strings.add("7");
        strings.add("8");
        strings.add("9");
        hourseAdapter.setNewData(strings);*/
        hourseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
        RecyclerViewHelper.initRecyclerViewV(rlvRoom, true, hourseAdapter);


    }


}

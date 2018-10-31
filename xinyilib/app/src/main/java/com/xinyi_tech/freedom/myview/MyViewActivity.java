package com.xinyi_tech.freedom.myview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyi_tech.comm.adapter.ImageTitleAdapter;
import com.xinyi_tech.comm.base.BaseActivity;
import com.xinyi_tech.comm.base.BasePresenter;
import com.xinyi_tech.comm.help.recycleview.RecyclerViewHelper;
import com.xinyi_tech.comm.model.ImageTitleModel;
import com.xinyi_tech.comm.util.OverridePendingTransitionUtls;
import com.xinyi_tech.comm.util.ToolbarUtils;
import com.xinyi_tech.freedom.R;
import com.xinyi_tech.freedom.myview.hencoderpracticedraw1.DrawActivity;
import com.xinyi_tech.freedom.myview.hencoderpracticedraw2.PaintActivity;
import com.xinyi_tech.freedom.myview.hencoderpracticedraw3.TextActivity;
import com.xinyi_tech.freedom.myview.hencoderpracticedraw4.CanvasActivity;
import com.xinyi_tech.freedom.myview.hencoderpracticedraw5.CanvasOrderActivity;
import com.xinyi_tech.freedom.myview.hencoderpracticedraw6.PropertyAnimator1Activity;
import com.xinyi_tech.freedom.myview.hencoderpracticedraw7.PropertyAnimator2Activity;
import com.xinyi_tech.freedom.myview.weiget.channel.ChannelActivity;
import com.xinyi_tech.freedom.myview.weiget.qqpoint.QQPointActivity;
import com.xinyi_tech.freedom.myview.weiget.recycleview.RecycleViewActivity;
import com.xinyi_tech.freedom.viewpager.ViewpagerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MyViewActivity extends BaseActivity<BasePresenter> implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    private ImageTitleAdapter<ImageTitleModel> mImageTitleAdapter;


    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        ToolbarUtils.with(this, mToolBar).setTitle(getIntent().getStringExtra("title"), true).build();
        mImageTitleAdapter = new ImageTitleAdapter();
        mImageTitleAdapter.setOnItemClickListener(this);
        mImageTitleAdapter.addAllDataOnlyVisible(getMainItem());
        GridLayoutManager gridLayoutManager = RecyclerViewHelper.initRecyclerViewG(mRecyclerview, true, mImageTitleAdapter, 5);
    }

    private ImageTitleModel generateImageTitleModel(int drawableId, String title, int orderIndex, Class clazz) {
        ImageTitleModel model = new ImageTitleModel(drawableId, title);
        model.setClazz(clazz);
        model.setOrderIndex(orderIndex);
        return model;
    }

    public List<ImageTitleModel> getMainItem() {
        ArrayList<ImageTitleModel> mainModeItems = new ArrayList<>();

        mainModeItems.add(generateImageTitleModel(R.mipmap.icon2, "Draw相关", 1, DrawActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon4, "Paint相关", 2, PaintActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon5, "Text相关", 2, TextActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon6, "Canvas相关", 2, CanvasActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon9, "绘制顺序相关", 2, CanvasOrderActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon15, "属性动画1", 2, PropertyAnimator1Activity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon16, "属性动画1", 2, PropertyAnimator2Activity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon2, "测试的view", -1, SuperCoverActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon2, "尺子", 0, SuperCoverActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon2, "水平伸缩菜单", 1, SuperCoverActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon2, "水位上升", 2, SuperCoverActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon2, "蚂蚁森林", 3, SuperCoverActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon18, "频道管理", 0, ChannelActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon19, "Viewpager高级用法", 0, ViewpagerActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon2, "游动的鱼", 4, SuperCoverActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon2, "粘性小红点", 0, QQPointActivity.class).setBudgeCount(10));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon2, "饼状图", 5, SuperCoverActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon2, "RecycleView相关", 5, RecycleViewActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon2, "进度条", 6, SuperCoverActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon2, "水波纹", 7, SuperCoverActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon2, "弹性侧边栏", 7, SpringViewActivity.class));

        //mainModeItems.add(generateImageTitleModel(R.mipmap.icon2, "和我相关", 5, SuperCoverActivity.class));
        // Collections.sort(mainModeItems);
        return mainModeItems;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new BasePresenter();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ImageTitleModel imageTitleModel = mImageTitleAdapter.getData().get(position);
        Intent intent = new Intent(this, imageTitleModel.getClazz());
        intent.putExtra("title", imageTitleModel.getTitle());
        intent.putExtra("viewIndex", imageTitleModel.getOrderIndex());
        startActivity(intent);
        OverridePendingTransitionUtls.slideRightEntry(this);

        //
    }


}

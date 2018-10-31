package com.xinyi_tech.freedom;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.xinyi_tech.comm.adapter.ImageTitleAdapter;
import com.xinyi_tech.comm.base.BaseActivity;
import com.xinyi_tech.comm.base.BasePresenter;
import com.xinyi_tech.comm.help.recycleview.RecyclerViewHelper;
import com.xinyi_tech.comm.model.ImageTitleModel;
import com.xinyi_tech.comm.util.ToolbarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ImageTitleActivity extends BaseActivity<BasePresenter> {
    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;


    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        ToolbarUtils.with(this, mToolBar).setSupportBack(true).setTitle(getIntent().getStringExtra("title"), true).setInflateMenu(R.menu.menu_tree);

        ImageTitleAdapter imageTitleAdapter = new ImageTitleAdapter();
        imageTitleAdapter.getData().addAll(getMainItem());
        RecyclerViewHelper.initRecyclerViewG(mRecyclerview, true, imageTitleAdapter, 3);
    }

    public List<ImageTitleModel> getMainItem() {
        ArrayList<ImageTitleModel> mainModeItems = new ArrayList<>();
        mainModeItems.add(new ImageTitleModel(R.mipmap.icon_nbgl, "内部管理"));
        mainModeItems.add(new ImageTitleModel(R.mipmap.icon_zfbaq, "执法办案区"));
        mainModeItems.add(new ImageTitleModel(R.mipmap.icon_fkww, "反恐维稳"));
        mainModeItems.add(new ImageTitleModel(R.mipmap.icon_zxdc, "专项督察"));
        mainModeItems.add(new ImageTitleModel(R.mipmap.icon_suishoupai, "随手拍"));
        mainModeItems.add(new ImageTitleModel(R.mipmap.icon_12389, "12389"));
        mainModeItems.add(new ImageTitleModel(R.mipmap.icon_dctb, "督察通报"));
        mainModeItems.add(new ImageTitleModel(R.mipmap.icon_lsjl, "督察记录"));
        mainModeItems.add(new ImageTitleModel(R.mipmap.icon_dctj, "督察统计"));
        mainModeItems.add(new ImageTitleModel(R.mipmap.icon_dcts, "督察消息"));
        mainModeItems.add(new ImageTitleModel(R.mipmap.icon_dclx, "督察立项"));
        mainModeItems.add(new ImageTitleModel(R.mipmap.icon_gahc, "个案核查"));
        mainModeItems.add(new ImageTitleModel(R.mipmap.icon_tzjb, "停职禁闭"));
        mainModeItems.add(new ImageTitleModel(R.mipmap.icon_mjwq, "民警维权"));
        return mainModeItems;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_title;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new BasePresenter();
    }

    @Override
    public void doParseData(int requestCode, Object data) {

    }
}

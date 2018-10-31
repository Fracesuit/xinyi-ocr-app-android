package com.xinyi_tech.freedom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyi_tech.comm.util.ToastyUtil;
import com.xinyi_tech.comm.util.ToolbarUtils;
import com.xinyi_tech.comm.widget.seacher.SuperSeacherModel;
import com.xinyi_tech.comm.widget.seacher.SuperSearchView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SeacherViewActivity extends AppCompatActivity {

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.ss)
    SuperSearchView mSs;
    @BindView(R.id.ss1)
    SuperSearchView mSs1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seacher_view);
        ButterKnife.bind(this);

        ToolbarUtils.with(this, mToolBar).setInflateMenu(R.menu.comm_menu_file_seacher)
                .setTitle("ceshi", true)
                .setSupportBack(true).build();
        MenuItem item = mToolBar.getMenu().findItem(R.id.action_search);
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final int itemId = item.getItemId();
                if (itemId == R.id.action_clear) {
                    mSs.showSeacherView(true);
                }
                return true;
            }
        });


        mSs1.withToolBarMenu(item, true);
        mSs1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ToastyUtil.successLong("position===" + position);
            }
        });
        mSs.setSearchViewListener(new SuperSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                ToastyUtil.successShort("onSearchViewShown");
            }

            @Override
            public void onSearchViewClosed() {
                ToastyUtil.successShort("onSearchViewClosed==");
            }
        });

        mSs.setOnQueryChangeListener(new SuperSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ToastyUtil.successShort("onQueryTextSubmit==" + query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // ToastyUtil.successShort("newText==" + newText);
                final ArrayList<SuperSeacherModel> superSeacherModels = new ArrayList<>();
                superSeacherModels.add(new SuperSeacherModel(R.mipmap.icon_search, newText));
                mSs.setSupportSuggestData(superSeacherModels);
                return false;
            }
        });


    }

    @OnClick(R.id.btn)
    public void onViewClicked() {
        final ArrayList<SuperSeacherModel> list = new ArrayList<>();
        list.add(new SuperSeacherModel(R.mipmap.ic_launcher_round, "测试1"));
        list.add(new SuperSeacherModel(R.mipmap.ic_launcher_round, "测试2"));
        list.add(new SuperSeacherModel(R.mipmap.ic_launcher_round, "测试3"));
        list.add(new SuperSeacherModel(R.mipmap.ic_launcher_round, "测试4"));
      /*  list.add(new SeacherItemModel(R.mipmap.ic_launcher, "测试5"));
        list.add(new SeacherItemModel(R.mipmap.ic_launcher, "测试6"));
        list.add(new SeacherItemModel(R.mipmap.ic_launcher, "测试1"));
        list.add(new SeacherItemModel(R.mipmap.ic_launcher, "测试2"));
        list.add(new SeacherItemModel(R.mipmap.ic_launcher, "测试3"));
        list.add(new SeacherItemModel(R.mipmap.ic_launcher, "测试4"));
        list.add(new SeacherItemModel(R.mipmap.ic_launcher, "测试5"));
        list.add(new SeacherItemModel(R.mipmap.ic_launcher, "测试6"));
        list.add(new SeacherItemModel(R.mipmap.ic_launcher, "测试1"));
        list.add(new SeacherItemModel(R.mipmap.ic_launcher, "测试2"));
        list.add(new SeacherItemModel(R.mipmap.ic_launcher, "测试3"));
        list.add(new SeacherItemModel(R.mipmap.ic_launcher, "测试4"));
        list.add(new SeacherItemModel(R.mipmap.ic_launcher, "测试5"));
        list.add(new SeacherItemModel(R.mipmap.ic_launcher, "测试6"));*/
        mSs1.setSupportSuggestData(list);
    }
}

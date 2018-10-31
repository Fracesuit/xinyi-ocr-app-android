package com.xinyi_tech.freedom.tree;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.xinyi_tech.comm.adapter.TreeAdapter;
import com.xinyi_tech.comm.base.BaseListFragment;
import com.xinyi_tech.comm.base.ListSetupModel;
import com.xinyi_tech.comm.model.TreeModel;
import com.xinyi_tech.comm.util.ToastyUtil;
import com.xinyi_tech.comm.util.ToolbarUtils;
import com.xinyi_tech.freedom.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by zhiren.zhang on 2018/3/27.
 */

public class TreeFragment extends BaseListFragment<TreeAdapter, TreeModel, TreePresent> {
    @Override
    protected TreePresent getPresenter() {
        return new TreePresent();
    }

    @Override
    protected void onCreateViewAfter(View view, Bundle savedInstanceState) {
        super.onCreateViewAfter(view, savedInstanceState);
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = ButterKnife.findById(activity, R.id.tool_bar);
        ToolbarUtils.with(activity, toolbar).setSupportBack(true).setTitle(activity.getIntent().getStringExtra("title"), true).setInflateMenu(R.menu.menu_tree).build();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.action:
                        List<TreeModel> data = baseListAdapter.getAllSelectedList();
                        ToastyUtil.normalShort("所有选中的个数:" + data.size());
                        break;
                    case R.id.action1:
                        List<TreeModel> data1 = baseListAdapter.getLastSelectedList();
                        ToastyUtil.normalShort("最终节点选中的个数:" + data1.size());
                        break;
                }


                return true;
            }
        });
    }

    @Override
    protected ListSetupModel setupParam() {
        return new ListSetupModel.Builder()
                .isEnableLoadMore(false)
                .isEnableRefresh(false)
                .build();
    }

    @Override
    protected TreeAdapter getAdapter() {
        TreeAdapter treeAdapter = new TreeAdapter();
        treeAdapter.addData(generateData());
        return treeAdapter;
    }

    private ArrayList<TreeModel> generateData() {
        //String[] groups = {"常规模式", "check模式"};
        String[] groups = {"常规模式"};
        ArrayList<TreeModel> res = new ArrayList<>();
        for (int i = 0; i < groups.length; i++) {
            TreeModel groupTree = new TreeModel(null, groups[i], TreeAdapter.TYPE_LEVEL_NORMAL).setIconId(R.mipmap.icon_dczj);
            res.add(groupTree);
            chlid(res, i == 0 ? TreeAdapter.TYPE_LEVEL_NORMAL : TreeAdapter.TYPE_LEVEL_NORMAL);
        }
        return res;
    }

    private void chlid(ArrayList<TreeModel> res, int itemType) {
        int lv0Count = 2;
        int lv1Count = 2;
        int lv2Count = 2;
        for (int i = 0; i < lv0Count; i++) {
            TreeModel lv0 = new TreeModel(null, "龙岗卡口" + i, itemType).setIconId(R.mipmap.icon_cy);
            for (int j = 0; j < lv1Count; j++) {
                TreeModel lv1 = new TreeModel(lv0, "卡口" + i, itemType).setIconId(R.mipmap.icon_dczj);
                for (int k = 0; k < lv2Count; k++) {
                    TreeModel lv2 = new TreeModel(lv1, "卡口子类" + k, itemType).setIconId(R.mipmap.icon_cy);
                    lv1.addSubItem(lv2);
                }
                lv0.addSubItem(lv1);
            }
            res.add(lv0);
        }
    }

    @Override
    protected void requestData(int requestCode, int pageIndex, int pageSize) {

    }

    @Override
    protected void onItemClick(TreeModel treeModel, int position) {

    }
}

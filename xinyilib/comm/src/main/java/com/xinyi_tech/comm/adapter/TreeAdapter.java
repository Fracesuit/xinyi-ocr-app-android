package com.xinyi_tech.comm.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyi_tech.comm.R;
import com.xinyi_tech.comm.model.TreeModel;

import java.util.ArrayList;
import java.util.List;

public class TreeAdapter extends BaseMultiItemQuickAdapter<TreeModel, BaseViewHolder> {
    public static final int TYPE_LEVEL_NORMAL = 0;
    public static final int TYPE_LEVEL_GROUP = 1;
    public static final int TYPE_LEVEL_CHECK = 2;
    private boolean modeSelectGroup = false;

    public void setModeSelectGroup(boolean modeSelectGroup) {
        this.modeSelectGroup = modeSelectGroup;
    }

    @Override
    public void addItemType(int type, @LayoutRes int layoutResId) {
        super.addItemType(type, layoutResId);
    }

    public TreeAdapter() {
        super(null);
        addItemType(TYPE_LEVEL_NORMAL, R.layout.comm_item_tree_normal);
        addItemType(TYPE_LEVEL_GROUP, R.layout.comm_item_tree_group);
        addItemType(TYPE_LEVEL_CHECK, R.layout.comm_item_tree_check);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final TreeModel item) {
        int itemViewType = helper.getItemViewType();
        switch (itemViewType) {
            case TYPE_LEVEL_NORMAL:
                normal(helper, item);
                break;
            case TYPE_LEVEL_GROUP:
                group(helper, item);
                break;
            case TYPE_LEVEL_CHECK:
                check(helper, item);
                break;
        }
    }


    private void check(final BaseViewHolder helper, final TreeModel item) {
        RelativeLayout rl_tree_root = helper.getView(R.id.rl_tree_root);
        AppCompatCheckBox cb_tree = helper.getView(R.id.cb_tree);
        final ImageView img_arrow = helper.getView(R.id.img_arrow);

        //chechbox
        if (item.getIconId() != -1) {
            Drawable drawable = ContextCompat.getDrawable(Utils.getApp(), item.getIconId());
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            cb_tree.setCompoundDrawables(drawable, null, null, null);
        } else {
            cb_tree.setCompoundDrawables(null, null, null, null);
        }
        cb_tree.setChecked(item.isSelected());
        cb_tree.setText(item.getText());
        //设置缩进距离
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) rl_tree_root.getLayoutParams();
        params.leftMargin = SizeUtils.dp2px(20) * (item.getIndex());
        rl_tree_root.setLayoutParams(params);
        cb_tree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //子类
                TreeAdapter.this.select(item);
            }
        });

        //是否展示扩展按钮以及按钮展示的形状
        img_arrow.setVisibility(item.hasSubItem() ? View.VISIBLE : View.INVISIBLE);
        img_arrow.setRotation(item.isExpanded() ? 90 : 0);

        //点击事件
        rl_tree_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.hasSubItem()) {
                    //扩展或者收缩
                    int pos = helper.getAdapterPosition();
                    if (item.isExpanded()) {
                        collapse(pos, false);
                    } else {
                        expand(pos, false);
                    }
                } else {
                    TreeAdapter.this.select(item);
                }

            }
        });
    }

    private void group(BaseViewHolder helper, TreeModel item) {
        AppCompatTextView tv_group = helper.getView(R.id.tv_group);
        tv_group.setText(item.getText());
    }

    private void normal(final BaseViewHolder helper, final TreeModel item) {
        RelativeLayout rl_tree_root = helper.getView(R.id.rl_tree_root);
        AppCompatTextView tv_tree = helper.getView(R.id.tv_tree);
        final ImageView img_arrow = helper.getView(R.id.img_arrow);
        //tv_tree
        if (item.getIconId() != -1) {
            Drawable drawable = ContextCompat.getDrawable(Utils.getApp(), item.getIconId());
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_tree.setCompoundDrawables(drawable, null, null, null);
        } else {
            tv_tree.setCompoundDrawables(null, null, null, null);
        }

        tv_tree.setText(item.getText());

        //设置缩进距离
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) rl_tree_root.getLayoutParams();
        params.leftMargin = SizeUtils.dp2px(20) * (item.getIndex());
        rl_tree_root.setLayoutParams(params);

        //是否展示扩展按钮以及按钮展示的形状
        img_arrow.setVisibility(item.hasSubItem() ? View.VISIBLE : View.INVISIBLE);
        img_arrow.setRotation(item.isExpanded() ? 90 : 0);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.hasSubItem()) {
                    int pos = helper.getAdapterPosition();
                    if (item.isExpanded()) {
                        collapse(pos, true);
                    } else {
                        expand(pos, true);
                    }
                } else {
                    if (getOnItemClickListener() != null) {
                        getOnItemClickListener().onItemClick(TreeAdapter.this, v, helper.getLayoutPosition() - getHeaderLayoutCount());
                    }
                }
            }
        };

        if (modeSelectGroup) {
            //父类也能单击
            img_arrow.setOnClickListener(onClickListener);
        } else {
            //点击事件
            rl_tree_root.setOnClickListener(onClickListener);
        }


    }


    //选中子类
    private void setSubSelected(TreeModel item) {
        if (item.hasSubItem()) {
            for (TreeModel m : item.getSubItems()) {
                m.setSelected(item.isSelected());
                setSubSelected(m);
            }
        }
    }

    //选中父类
    private void setParentSelected(TreeModel parent) {
        if (parent != null) {
            parent.setSelected(isSubAllSelected(parent));
            setParentSelected(parent.getParent());
        }
    }

    private boolean isSubAllSelected(TreeModel parent) {
        if (parent != null && parent.hasSubItem()) {
            for (TreeModel m : parent.getSubItems()) {
                if (!m.isSelected()) {
                    return false;
                } else {
                    isSubAllSelected(m);
                }
            }
        }
        return true;
    }

    private void select(TreeModel item) {
        //子类
        item.setSelected(!item.isSelected());
        setSubSelected(item);
        //父类
        setParentSelected(item.getParent());
        notifyDataSetChanged();
    }


    public List<TreeModel> getAllSelectedList() {
        ArrayList<TreeModel> temp = new ArrayList<>();
        List<TreeModel> data = getAllRootT();
        getAllSelectedByParent(data, temp);
        return temp;
    }

    private List<TreeModel> getAllRootT() {
        ArrayList<TreeModel> temp = new ArrayList<>();
        List<TreeModel> data = getData();
        for (TreeModel tree : data) {
            if (tree.getParent() == null) {
                temp.add(tree);
            }
        }
        return temp;
    }


    //获取最低节点
    public List<TreeModel> getLastSelectedList() {
        ArrayList<TreeModel> temp = new ArrayList<>();
        List<TreeModel> data = getAllRootT();
        getLastSelectedByParent(data, temp);
        return temp;
    }

    private void getAllSelectedByParent(List<TreeModel> from, List<TreeModel> to) {
        for (TreeModel m : from) {
            if (m.isSelected()) {
                to.add(m);
            }
            if (m.hasSubItem()) {
                getAllSelectedByParent(m.getSubItems(), to);
            }
        }
    }

    private void getLastSelectedByParent(List<TreeModel> from, List<TreeModel> to) {
        for (TreeModel m : from) {
            if (m.hasSubItem()) {
                getLastSelectedByParent(m.getSubItems(), to);
            } else {
                if (m.isSelected()) {
                    to.add(m);
                }
            }
        }
    }
}
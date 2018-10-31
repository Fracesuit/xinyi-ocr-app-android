package com.xinyi_tech.comm.help.recycleview;

import android.graphics.Rect;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.Utils;
import com.xinyi_tech.comm.R;
import com.xinyi_tech.comm.help.recycleview.divider.HorizontalDividerItemDecoration;
import com.xinyi_tech.comm.help.recycleview.divider.VerticalDividerItemDecoration;


/**
 * Created by long on 2016/3/30.
 * 视图帮助类
 */
public class RecyclerViewHelper {

    private RecyclerViewHelper() {
        throw new RuntimeException("RecyclerViewHelper cannot be initialized!");
    }


    public static LinearLayoutManager initRecyclerViewV(RecyclerView view, boolean isDivided, RecyclerView.Adapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(Utils.getApp());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        if (isDivided) {
            view.addItemDecoration(new HorizontalDividerItemDecoration.Builder(Utils.getApp())
                    .colorResId(R.color.line)
                    .showLastDivider()
                    .build());
        }
        view.setAdapter(adapter);
        return layoutManager;
    }

    /**
     * 配置水平列表RecyclerView
     */
    public static LinearLayoutManager initRecyclerViewH(RecyclerView view, boolean isDivided, RecyclerView.Adapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(Utils.getApp());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        if (isDivided) {
            view.addItemDecoration(new VerticalDividerItemDecoration.Builder(Utils.getApp()).colorResId(R.color.line).build());
        }
        view.setAdapter(adapter);
        return layoutManager;
    }

    /**
     * 配置网格列表RecyclerView
     */
    public static GridLayoutManager initRecyclerViewG(RecyclerView view, boolean isDivided, RecyclerView.Adapter adapter, final int column) {
        GridLayoutManager layoutManager = new GridLayoutManager(Utils.getApp(), column, LinearLayoutManager.VERTICAL, false);
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        if (isDivided) {
            view.addItemDecoration(new HorizontalDividerItemDecoration.Builder(Utils.getApp())
                    .colorResId(R.color.line).build());
            view.addItemDecoration(new VerticalDividerItemDecoration.Builder(Utils.getApp())
                    .colorResId(R.color.line).build());
        }
        view.setAdapter(adapter);
        return layoutManager;
    }

    /**
     * 配置瀑布流列表RecyclerView
     */
    public static StaggeredGridLayoutManager initRecyclerViewSV(RecyclerView view, boolean isDivided,
                                                                RecyclerView.Adapter adapter, int column) {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(column, StaggeredGridLayoutManager.VERTICAL);
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        if (isDivided) {
            view.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.set(4, 4, 4, 4);
                }
            });
        }
        view.setAdapter(adapter);
        return layoutManager;
    }

}

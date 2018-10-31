package com.xinyi_tech.freedom.myview.weiget.qqpoint;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.xinyi_tech.comm.base.BaseListFragment;
import com.xinyi_tech.comm.base.BasePresenter;
import com.xinyi_tech.comm.log.XinYiLog;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by zhiren.zhang on 2018/5/15.
 */

public class QQPointFragment extends BaseListFragment<QQPointAdapter, Integer, BasePresenter> {

    @Override
    protected void onCreateViewAfter(View view, Bundle savedInstanceState) {
        super.onCreateViewAfter(view, savedInstanceState);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = 0;//ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                int swipeFlags = ItemTouchHelper.LEFT;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                XinYiLog.e("onMove");
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                XinYiLog.e("onSwiped"+direction);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerview);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected QQPointAdapter getAdapter() {
        ArrayList<Integer> strings = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            strings.add(new Random().nextInt(200));
        }
        return new QQPointAdapter(strings);
    }

    @Override
    protected void requestData(int requestCode, int pageIndex, int pageSize) {

    }

    @Override
    protected void onItemClick(Integer s, int position) {

    }
}

package com.xinyi_tech.freedom.myview.weiget.recycleview;

import android.os.Bundle;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xinyi_tech.comm.base.BaseListFragment;
import com.xinyi_tech.comm.base.BasePresenter;
import com.xinyi_tech.comm.base.ListSetupModel;
import com.xinyi_tech.comm.constant.LayoutStyleCode;
import com.xinyi_tech.comm.log.XinYiLog;

import java.util.ArrayList;

/**
 * Created by zhiren.zhang on 2018/5/16.
 */

public class RecycleViewFragment extends BaseListFragment<RecycleViewAdapter, String, BasePresenter> {

    @Override
    protected ListSetupModel setupParam() {
        return ListSetupModel.newBuilder().isEnableLoadMore(false).isEnableRefresh(false).layoutStyle(LayoutStyleCode.LAYOUT_LINEAR_H).build();
    }

    @Override
    protected void onCreateViewAfter(View view, Bundle savedInstanceState) {
        super.onCreateViewAfter(view, savedInstanceState);

        //  PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();//和viewpager  智能滑动一个item  不能fling  item居中
        // pagerSnapHelper.attachToRecyclerView(recyclerview);
        final LinearSnapHelper linearSnapHelper = new LinearSnapHelper(); //item居中 可以fling
        linearSnapHelper.attachToRecyclerView(recyclerview);
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //  super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View snapView = linearSnapHelper.findSnapView(recyclerView.getLayoutManager());
                    int childAdapterPosition = recyclerview.getChildAdapterPosition(snapView);
                    int computeHorizontalScrollExtent = recyclerview.computeHorizontalScrollExtent();//返回当前屏幕显示的区域高度
                    int computeHorizontalScrollRange = recyclerview.computeHorizontalScrollRange();//返回之前已经滑动过的高度
                    int computeHorizontalScrollOffset = recyclerview.computeHorizontalScrollOffset();//返回整个控件的高度

                    XinYiLog.e("newStatenewState==" + newState + "  位置==" + childAdapterPosition + "  computeHorizontalScrollExtent==" + computeHorizontalScrollExtent
                            + "  computeHorizontalScrollRange==" + computeHorizontalScrollRange
                            + "  computeHorizontalScrollOffset==" + computeHorizontalScrollOffset);
                }


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // super.onScrolled(recyclerView, dx, dy);
            }
        });

        recyclerview.canScrollVertically(1);//的值表示是否能向上滚动，false表示已经滚动到底部
        recyclerview.canScrollVertically(-1);//的值表示是否能向下滚动，false表示已经滚动到顶部
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected RecycleViewAdapter getAdapter() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("0");
        strings.add("1");
        strings.add("2");
        strings.add("3");
        return new RecycleViewAdapter(strings);
    }

    @Override
    protected void requestData(int requestCode, int pageIndex, int pageSize) {

    }

    @Override
    protected void onItemClick(String s, int position) {

    }
}

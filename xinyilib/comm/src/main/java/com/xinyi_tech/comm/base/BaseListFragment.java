package com.xinyi_tech.comm.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyi_tech.comm.R;
import com.xinyi_tech.comm.constant.LayoutStyleCode;
import com.xinyi_tech.comm.help.recycleview.RecyclerViewHelper;

import java.util.List;

import butterknife.ButterKnife;


/**
 * Created by Fracesuit on 2017/8/7.
 */

public abstract class BaseListFragment<A extends BaseQuickAdapter<T, ? extends BaseViewHolder>, T, P extends BasePresenter>
        extends BaseFragment<P> implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    //请求
    protected static final int REQUEST_STATE_REFRESH = 5678;//下拉刷新
    protected static final int REQUEST_STATE_LOADMORE = 5679;//上拉加载更多

    //返回
    private static final int RESPONSE_STATE_START = 10;//开始状态
    private static final int RESPONSE_STATE_ERROR = 11;//错误状态
    private static final int RESPONSE_STATE_COMPLETED = 13;//完成状态
    //private static final int RESPONSE_STATE_PARSEING = 14;//解析数据

    //参数
    protected ListSetupModel setupModel;
    private int pageIndex = 1;

    protected A baseListAdapter;

    protected RecyclerView.LayoutManager layoutManager;
    protected SwipeRefreshLayout mRefreshLayout;//这个用来控制下拉刷新的头部
    protected RecyclerView recyclerview;

    protected View notdataview;
    protected View errorview;


    @Override
    protected int getLayoutId() {
        return R.layout.comm_fragment_baselist;
    }


    @Override
    @CallSuper
    protected void onCreateViewAfter(View view, Bundle savedInstanceState) {
        setupModel = setupParam();
        pageIndex = setupModel.getPageIndex();
        isLasy = setupModel.isLasy();
        isRefreshAlways = setupModel.isRefreshAlways();
        initView(view);
        initListener();
    }


    protected ListSetupModel setupParam() {
        //设置初始化参数
        return new ListSetupModel.Builder().build();
    }

    protected void initView(View view) {
        recyclerview = view.findViewById(R.id.recyclerview);
        mRefreshLayout = view.findViewById(R.id.swiperefreshlayout);
        initRefreshLayout();
        initRecyclerView();

        notdataview = activity.getLayoutInflater().inflate(R.layout.comm_empty_view, (ViewGroup) recyclerview.getParent(), false);
        if (!StringUtils.isEmpty(setupModel.getEmptyMsg())) {
            TextView empty_retry_view = ButterKnife.findById(notdataview, R.id.empty_retry_view);
            empty_retry_view.setText(setupModel.getEmptyMsg());
        }
        errorview = activity.getLayoutInflater().inflate(R.layout.comm_error_view, (ViewGroup) recyclerview.getParent(), false);
    }

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.comm_pink, R.color.comm_green, R.color.comm_amber,
                R.color.comm_red, R.color.comm_blue, R.color.comm_yellow);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setEnabled(setupModel.isEnableRefresh());
    }

    private void initRecyclerView() {
        baseListAdapter = getAdapter();
        baseListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BaseListFragment.this.onItemClick(baseListAdapter.getItem(position), position);
            }
        });
        int layoutStyle = setupModel.getLayoutStyle();
        if (layoutStyle == LayoutStyleCode.LAYOUT_STAGGERED) {
            layoutManager = RecyclerViewHelper.initRecyclerViewSV(recyclerview, setupModel.isDivided(), baseListAdapter, setupModel.getSpanCount());
        } else if (layoutStyle == LayoutStyleCode.LAYOUT_GRID) {
            layoutManager = RecyclerViewHelper.initRecyclerViewG(recyclerview, setupModel.isDivided(), baseListAdapter, setupModel.getSpanCount());
        } else if (layoutStyle == LayoutStyleCode.LAYOUT_LINEAR_H) {
            layoutManager = RecyclerViewHelper.initRecyclerViewH(recyclerview, setupModel.isDivided(), baseListAdapter);
        } else {
            layoutManager = RecyclerViewHelper.initRecyclerViewV(recyclerview, setupModel.isDivided(), baseListAdapter);
        }
        baseListAdapter.setEnableLoadMore(setupModel.isEnableLoadMore());
        baseListAdapter.setOnLoadMoreListener(baseListAdapter.isLoadMoreEnable() ? this : null, recyclerview);
    }

    protected void initListener() {
        notdataview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestListData(REQUEST_STATE_REFRESH);
            }
        });

        errorview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestListData(REQUEST_STATE_REFRESH);
            }
        });


    }


    protected abstract A getAdapter();

    @Override
    protected void requestData() {
        requestListData(REQUEST_STATE_REFRESH);
    }

    protected void requestListData(final int requestCode) {
        if (requestCode == REQUEST_STATE_REFRESH) {
            pageIndex = setupModel.getPageIndex();
        }
        requestData(requestCode, pageIndex, setupModel.getPageSize());
    }


    protected abstract void requestData(int requestCode, int pageIndex, int pageSize);


    @Override
    public void doOnStart(int requestCode) {
        if (requestCode == REQUEST_STATE_REFRESH || requestCode == REQUEST_STATE_LOADMORE) {
            refreshui(requestCode, RESPONSE_STATE_START);
        } else {
            super.doOnStart(requestCode);
        }
        LogUtils.d("doOnStart");
    }


    @Override
    public void doOnError(int requestCode, String msg, Throwable e) {
        refreshui(requestCode, RESPONSE_STATE_ERROR);
        super.doOnError(requestCode, msg, e);
        LogUtils.d("doOnError");
    }


    @Override
    public void doOnCompleted(int requestCode) {
        if (requestCode == REQUEST_STATE_REFRESH || requestCode == REQUEST_STATE_LOADMORE) {
            refreshui(requestCode, RESPONSE_STATE_COMPLETED);
            LogUtils.d("doOnCompleted");
        } else {
            super.doOnCompleted(requestCode);
        }
    }


    protected abstract void onItemClick(T t, int position);


    private void refreshui(int requestcode, int state) {
        if (requestcode == REQUEST_STATE_REFRESH) {
            //下拉  刷新
            switch (state) {
                case RESPONSE_STATE_START:
                    mRefreshLayout.setRefreshing(true);
                    break;
                case RESPONSE_STATE_ERROR:
                    mRefreshLayout.setRefreshing(false);
                    baseListAdapter.setEmptyView(errorview);
                    break;

                case RESPONSE_STATE_COMPLETED:
                    pageIndex++;
                    int size = baseListAdapter.getData().size();
                    if (size == 0) {
                        baseListAdapter.setEmptyView(notdataview);
                    }
                    mRefreshLayout.setRefreshing(false);
                    break;

            }
        } else if (requestcode == REQUEST_STATE_LOADMORE) {
            //上拉 加载更多
            switch (state) {
                case RESPONSE_STATE_START:
                    break;
                case RESPONSE_STATE_ERROR:
                    baseListAdapter.loadMoreFail();
                    break;
                case RESPONSE_STATE_COMPLETED:
                    pageIndex++;
                    baseListAdapter.loadMoreComplete();
                    break;

            }

        }

    }

    @Override
    public void onRefresh() {
        //下拉刷新监听
        requestListData(REQUEST_STATE_REFRESH);
    }

    @Override
    public void onLoadMoreRequested() {
        //加载更多监听
        if (baseListAdapter.getData().size() < (pageIndex - setupModel.getPageIndex()) * setupModel.getPageSize()) {
            baseListAdapter.loadMoreEnd();
        } else {
            requestListData(REQUEST_STATE_LOADMORE);
        }
    }

    @Override
    public void doParseData(int requestCode, Object data) {
        //返回的数据
        if (requestCode == REQUEST_STATE_REFRESH) {
            baseListAdapter.setNewData((List<T>) data);
        } else if (requestCode == REQUEST_STATE_LOADMORE) {
            baseListAdapter.addData((List<T>) data);
        }
        LogUtils.d("doParseData");
    }
}

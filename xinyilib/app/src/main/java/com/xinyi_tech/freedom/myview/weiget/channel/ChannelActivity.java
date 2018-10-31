package com.xinyi_tech.freedom.myview.weiget.channel;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xinyi_tech.comm.base.BaseActivity;
import com.xinyi_tech.comm.base.BasePresenter;
import com.xinyi_tech.comm.help.recycleview.RecyclerViewHelper;
import com.xinyi_tech.comm.log.XinYiLog;
import com.xinyi_tech.freedom.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;

public class ChannelActivity extends BaseActivity<BasePresenter> {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private String select[] = {"要闻", "体育", "新时代", "汽车", "时尚", "国际", "电影", "财经", "游戏", "科技", "房产", "政务", "图片", "独家"};
    private String recommend[] = {"娱乐", "军事", "文化", "视频", "股票", "动漫", "理财", "电竞", "数码", "星座", "教育", "美容", "旅游"};
    private ChannelAdapter mChannelAdapter;


    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        mChannelAdapter = new ChannelAdapter();
        ArrayList<MultiItemEntity> multiItemEntities = new ArrayList<>();
        multiItemEntities.add(new GroupTextModel("已选频道"));
        for (String c : select) {
            multiItemEntities.add(new ChannelModel(true, c));
        }
        multiItemEntities.add(new GroupTextModel("推荐频道"));
        for (String c : recommend) {
            multiItemEntities.add(new ChannelModel(false, c));
        }
        mChannelAdapter.setNewData(multiItemEntities);
        mChannelAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int itemViewType = mChannelAdapter.getItemViewType(position);
                if (itemViewType == 0) {
                    ChannelModel channelModel = (ChannelModel) mChannelAdapter.getItem(position);
                    boolean select = channelModel.isSelect();
                    channelModel.setSelect(!select);
                    if (select) {
                        //删除选择，到推荐的位置
                        moveItem(position, mChannelAdapter.getSelectedCount() + 2);
                    } else {
                        //删除推荐 到选择的位置
                        moveItem(position, mChannelAdapter.getSelectedCount());
                    }

                }
            }
        });

        GridLayoutManager gridLayoutManager = RecyclerViewHelper.initRecyclerViewG(mRecyclerView, false, mChannelAdapter, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = mChannelAdapter.getItemViewType(position);
                return itemViewType == 1 ? 4 : 1;
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                XinYiLog.e("getMovementFlags");
                int adapterPosition = viewHolder.getAdapterPosition();
                if (adapterPosition > mChannelAdapter.getSelectedCount() || adapterPosition < 1) {
                    return 0;
                }

                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();   //拖动的position
                int toPosition = target.getAdapterPosition();     //释放的position
                //固定位置及tab下面的channel不能拖动
                if (toPosition > mChannelAdapter.getSelectedCount() || toPosition < 1) {
                    return false;
                }
                moveItem(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                XinYiLog.e("onSwiped");
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                XinYiLog.e("onSelectedChanged");
                super.onSelectedChanged(viewHolder, actionState);

            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                XinYiLog.e("clearView");
                super.clearView(recyclerView, viewHolder);

            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return super.isItemViewSwipeEnabled();
            }
        });
        //ItemTouchHelper.SimpleCallback
        //  itemTouchHelper.startDrag();
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void moveItem(int fromPosition, int toPosition) {
        if (fromPosition > toPosition) {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mChannelAdapter.getData(), i, i - 1);
            }
        } else {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mChannelAdapter.getData(), i, i + 1);
            }
        }
        mChannelAdapter.notifyItemMoved(fromPosition, toPosition);
        mChannelAdapter.notifyItemRangeChanged(toPosition, 1);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_channel;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }
}

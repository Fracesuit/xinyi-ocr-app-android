package com.xinyi_tech.freedom.myview.weiget.qqpoint;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyi_tech.freedom.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhiren.zhang on 2018/5/15.
 */

public class QQPointAdapter extends BaseItemDraggableAdapter<Integer, QQPointAdapter.QQPointViewHolder> {
    public QQPointAdapter(@Nullable List<Integer> data) {
        super(R.layout.item_qq_point, data);
    }

    @Override
    protected void convert(QQPointViewHolder helper, Integer item) {
        helper.mQqPoint.setText(item);
        int adapterPosition = helper.getAdapterPosition();
        int layoutPosition = helper.getLayoutPosition();
        helper.mTvContent.setText("这是第条" + adapterPosition + " （adapterPosition）条数据" + " layoutPosition==" + layoutPosition);
    }

    class QQPointViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_content)
        TextView mTvContent;
        @BindView(R.id.qq_point)
        StickView mQqPoint;
        @BindView(R.id.ll_layout)
        RelativeLayout mLlLayout;
        @BindView(R.id.tv_to_top)
        TextView mTvToTop;
        @BindView(R.id.tv_to_unread)
        TextView mTvToUnread;
        @BindView(R.id.tv_to_delete)
        TextView mTvToDelete;
        @BindView(R.id.ll_right_menu)
        LinearLayout mLlRightMenu;

        public QQPointViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

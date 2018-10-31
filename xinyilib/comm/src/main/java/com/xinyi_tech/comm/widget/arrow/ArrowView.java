package com.xinyi_tech.comm.widget.arrow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyi_tech.comm.help.recycleview.RecyclerViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fracesuit on 2017/12/27.
 */

public class ArrowView extends RecyclerView {
    private List<ArrowModel> arrowName = new ArrayList<>();
    private ArrowAdapter arrowAdapter;
    ArrowModel firstArrowModel;

    public ArrowView(@NonNull Context context, @NonNull ArrowModel arrowModel) {
        super(context);
        firstArrowModel = arrowModel;
        init();
    }

    public ArrowView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        if (firstArrowModel != null) {
            arrowName.add(firstArrowModel);
        }
        arrowAdapter = new ArrowAdapter(arrowName);
        arrowAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                final List<ArrowModel> data = arrowAdapter.getData();
                final int size = data.size();
                for (int i = size - 1; i > position; i--) {
                    data.remove(i);
                }
                arrowAdapter.notifyDataSetChanged();
                if (onArrowItemClickListener != null) {
                    onArrowItemClickListener.onArrowClick(arrowAdapter.getItem(position));
                }
            }
        });
        RecyclerViewHelper.initRecyclerViewH(this, false, arrowAdapter);
    }

    public void setFirstArrowModel(@NonNull ArrowModel firstArrowModel) {
        this.firstArrowModel = firstArrowModel;
        arrowName.add(firstArrowModel);
        arrowAdapter.notifyDataSetChanged();
    }

    //退一格
    public boolean onBackPressed() {
        final List<ArrowModel> data = arrowAdapter.getData();
        if (data.size() > 1) {
            data.remove(data.size() - 1);
            arrowAdapter.notifyDataSetChanged();
            if (onArrowItemClickListener != null) {
                onArrowItemClickListener.onArrowClick(getLastModel());
            }
            return true;
        }
        return false;
    }

    public void addLastData(ArrowModel model) {
        addLastData(model, true);
    }

    public void addLastData(ArrowModel model, boolean isClick) {
        arrowAdapter.addData(model);
        arrowAdapter.notifyDataSetChanged();
        if (onArrowItemClickListener != null && isClick) {
            onArrowItemClickListener.onArrowClick(model);
        }
    }

    public ArrowModel getLastModel() {
        final int size = arrowAdapter.getData().size();
        return arrowAdapter.getItem(size - 1);
    }


    public ArrowModel getFirstArrowModel() {
        return firstArrowModel;
    }

    public interface OnArrowItemClickListener {
        void onArrowClick(ArrowModel txlDeptModel);
    }

    OnArrowItemClickListener onArrowItemClickListener;

    public void setOnArrowItemClickListener(OnArrowItemClickListener onArrowItemClickListener) {
        this.onArrowItemClickListener = onArrowItemClickListener;
    }
}

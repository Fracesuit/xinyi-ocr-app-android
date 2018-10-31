package com.xinyi_tech.freedom.baserecycleviewadapterhelper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinyi_tech.freedom.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhiren.zhang on 2018/9/6.
 */

public class HelperAdapter2 extends RecyclerView.Adapter<HelperAdapter2.DViewHolder> {

    List<String> list = new ArrayList<>();

    @NonNull
    @Override
    public DViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_base_adapter, parent, false);
        // 实例化viewholder
        DViewHolder viewHolder = new DViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DViewHolder holder, int position) {
        holder.ctvHourseName.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class DViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ctv_hourse_name)
        TextView ctvHourseName;
        @BindView(R.id.ll_hourse_root)
        RelativeLayout llHourseRoot;

        DViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setData(List<String> list)
    {
        this.list=list;
        notifyDataSetChanged();
    }
}

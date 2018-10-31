package com.xinyi_tech.comm.picker.file;


import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyi_tech.comm.R;
import com.xinyi_tech.comm.util.ToastyUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Fracesuit on 2017/7/21.
 */

public class FileSelectAdapter extends BaseQuickAdapter<FileModel, BaseViewHolder> {

    ArrayList<FileModel> selectedList = new ArrayList<FileModel>();

    private int maxSelectNum = 5;//最大选择的数量

    public FileSelectAdapter() {
        super(R.layout.comm_item_file);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FileModel item) {
        final View rl_file_root = helper.getView(R.id.rl_file_root);
        final AppCompatCheckBox fileCb = helper.getView(R.id.cb_file);
        fileCb.setVisibility(item.isDir() ? View.GONE : View.VISIBLE);
        helper.setImageResource(R.id.img_file_type, item.getRes());
        helper.setText(R.id.tv_file_name, item.getFileName());
        fileCb.setChecked(item.isSelect() || selectedList.contains(item));
        rl_file_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.isDir()) {
                    getOnItemClickListener().onItemClick(FileSelectAdapter.this, v, helper.getLayoutPosition() - getHeaderLayoutCount());
                } else {
                    if (selectedList.size() < maxSelectNum) {
                        item.setSelect(!fileCb.isChecked());
                        if (item.isSelect()) {
                            selectedList.add(item);
                        } else {
                            selectedList.remove(item);
                        }
                        FileSelectAdapter.this.notifyItemChanged(helper.getAdapterPosition());
                    } else {
                        ToastyUtil.warningShort("最多选择" + maxSelectNum + "个文件");
                    }

                }
            }
        });
    }

    public void setMaxSelectNum(int maxSelectNum) {
        this.maxSelectNum = maxSelectNum;
    }

    public ArrayList<FileModel> getSelectedList() {
        return selectedList;
    }

    public void clearAllSelect() {
        final List<FileModel> data = getData();
        for (FileModel f : data) {
            f.setSelect(false);
        }
        selectedList.clear();
        notifyDataSetChanged();
    }
}

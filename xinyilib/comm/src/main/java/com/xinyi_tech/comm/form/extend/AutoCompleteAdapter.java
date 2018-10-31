package com.xinyi_tech.comm.form.extend;

import android.support.annotation.Nullable;
import android.widget.Filter;
import android.widget.Filterable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyi_tech.comm.R;
import com.xinyi_tech.comm.form.DictField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiren.zhang on 2018/6/15.
 */

public class AutoCompleteAdapter extends BaseQuickAdapter<DictField, BaseViewHolder> implements Filterable {
    private List<DictField> mSourceList = new ArrayList<>();
    private List<DictField> mFilterList = new ArrayList<>();

    public AutoCompleteAdapter() {
        super(R.layout.comm_item_auto_complete_edittext);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final DictField item) {
        helper.setText(R.id.tv_text, item.getName());
    }

    @Override
    public void setNewData(@Nullable List<DictField> data) {
        mSourceList = data;
        super.setNewData(data);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilterList = mSourceList;
                } else {
                    List<DictField> filteredList = new ArrayList<>();
                    for (DictField dictField : mSourceList) {
                        //这里根据需求，添加匹配规则
                        if (dictField.getName().contains(charString)) {
                            filteredList.add(dictField);
                        }
                    }
                    mFilterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilterList = (ArrayList<DictField>) filterResults.values;
                AutoCompleteAdapter.super.setNewData(mFilterList);
            }
        };
    }
}

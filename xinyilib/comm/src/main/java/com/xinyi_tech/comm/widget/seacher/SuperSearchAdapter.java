package com.xinyi_tech.comm.widget.seacher;

import com.allen.library.SuperTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyi_tech.comm.R;

/**
 * Suggestions Adapter.
 *
 * @author Miguel Catalan Bañuls
 */
public class SuperSearchAdapter extends BaseQuickAdapter<SuperSeacherModel, BaseViewHolder> {
    public SuperSearchAdapter() {
        super(R.layout.comm_item_seacher);
    }

    @Override
    protected void convert(BaseViewHolder helper, SuperSeacherModel item) {

        final SuperTextView suggestion_text = helper.getView(R.id.stv_seacher);
        suggestion_text.setLeftString(item.getTitle())
                .setLeftIcon(item.getDrawable());
    }

}
package com.xinyi_tech.comm.form.extend;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyi_tech.comm.CommCallBackListener;
import com.xinyi_tech.comm.R;
import com.xinyi_tech.comm.form.DictField;
import com.xinyi_tech.comm.help.recycleview.RecyclerViewHelper;

import java.util.List;

import butterknife.ButterKnife;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by Fracesuit on 2017/11/7.
 */

public class AutoCompletePopopWindow extends BasePopupWindow {
    RecyclerView rlv;
    View popupAnima;

    private final AutoCompleteAdapter adapter;

    CommCallBackListener<DictField> commCallBackListener;

    public void setCommCallBackListener(CommCallBackListener<DictField> commCallBackListener) {
        this.commCallBackListener = commCallBackListener;
    }

    public AutoCompletePopopWindow(Activity context) {
        this(context, true);
    }

    public AutoCompletePopopWindow(Activity context, boolean dismissWhenTouchOutside) {
        super(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setAutoLocatePopup(true);
        setDismissWhenTouchOutside(dismissWhenTouchOutside);
        getPopupWindow().setFocusable(false);
        //设置弹出窗体需要软键盘，
        getPopupWindow().setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        //再设置模式，和Activity的一样，覆盖，调整大小。
        getPopupWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        adapter = new AutoCompleteAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter a, View view, int position) {
                if (commCallBackListener != null) {
                    commCallBackListener.callBack(adapter.getItem(position));
                }
            }
        });
        RecyclerViewHelper.initRecyclerViewV(rlv, true, adapter);
    }

    @Override
    protected Animation initShowAnimation() {
        return getDefaultAlphaAnimation();
    }


    @Override
    public View getClickToDismissView() {
        return null;
    }


    @Override
    public View onCreatePopupView() {
        View popupView = createPopupById(R.layout.comm_popup_auto_complete);
        rlv = ButterKnife.findById(popupView, R.id.rlv);
        popupAnima = ButterKnife.findById(popupView, R.id.popup_anima);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupAnima;
    }

    public void setData(List<DictField> data) {
        adapter.setNewData(data);
    }

    public void filter(String text)
    {
        adapter.getFilter().filter(text);
    }

    public List<DictField> getData()
    {
        return adapter.getData();
    }

}

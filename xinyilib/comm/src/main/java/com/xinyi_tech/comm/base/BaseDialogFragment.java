package com.xinyi_tech.comm.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.LogUtils;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.xinyi_tech.comm.CommException;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by Fracesuit on 2017/8/1.
 * <p>
 * 生命周期管理
 * p层初始化
 */

public abstract class BaseDialogFragment<P extends BasePresenter> extends DialogFragment implements LifecycleProvider<FragmentEvent>, BaseView {
    public final String TAG = this.getClass().getSimpleName();
    protected P mPresenter;
    Unbinder unbinder;

    protected boolean isActivityCreate = false;
    protected AppCompatActivity activity;
    protected boolean isFirstLoadData = true;

    protected boolean isRefreshAlways = false;
    protected boolean isCancelTaskWhenInvisible = false;
    protected boolean isLasy = false;//是否是懒加载


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setupMvp();
        activity = (AppCompatActivity) getActivity();
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = ((MaterialDialog) getDialog()).getCustomView();
        unbinder = ButterKnife.bind(this, view);
        onCreateViewAfter(view, savedInstanceState);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MaterialDialog.Builder(getActivity())
                .customView(getLayoutId(), true)
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .autoDismiss(false).build();
    }

    protected abstract void requestData();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isActivityCreate = true;
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected View getLayoutView() {
        return null;
    }

    protected abstract void onCreateViewAfter(View view, Bundle savedInstanceState);


    private void setupMvp() {
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attach(this);
        }

    }

    protected abstract P getPresenter();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(FragmentEvent.START);
        if (!isLasy && (isFirstLoadData || isRefreshAlways)) {//刷新
            isFirstLoadData = false;
            requestData();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    public void onPause() {
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        lifecycleSubject.onNext(FragmentEvent.STOP);
        if (isCancelTaskWhenInvisible) {
            if (mPresenter != null) {
                mPresenter.cancelAll();
            }
        }

        super.onStop();
    }


    @Override
    public void onDestroy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        if (mPresenter != null) {
            mPresenter.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        lifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isActivityCreate) {
            if (!hidden) {
                visible();
            } else {
                inVisible();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isActivityCreate) {
            if (isVisibleToUser) {
                visible();

            } else {
                inVisible();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }


    //相当于onStart
    protected void visible() {
        if ((isFirstLoadData && isLasy) || isRefreshAlways) {//刷新
            isFirstLoadData = false;
            requestData();
        }
    }

    //相当于onStop
    protected void inVisible() {
        if (isCancelTaskWhenInvisible) {
            if (mPresenter != null) {
                mPresenter.cancelAll();
            }
        }
    }

    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    @NonNull
    @CheckResult
    public final Observable<FragmentEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindFragment(lifecycleSubject);
    }


    //==========================等待框start==========================
    protected void showProgressBar(final int requestCode, String content, boolean cancel) {
        DialogInterface.OnCancelListener onCancelListener = null;
        if (cancel) {
            onCancelListener = new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    LogUtils.d(TAG + "cancelTask");
                    if (mPresenter != null) {
                        mPresenter.cancel(requestCode);
                    }
                }
            };
        }
        BaseUtils.showDialog(activity, content, onCancelListener);
    }

    protected void hideProgressBar() {
        BaseUtils.hideDialog();
    }
    //==========================等待框end==========================

    //==========================等待框end==========================

    //========================rx相关start===============================
    @Override
    public void doOnStart(int requestCode) {
        LogUtils.d("任务开始了");
        if (requestCode > 0) {
            showProgressBar(requestCode, "请稍等...", true);
        }
    }


    @Override
    public void doOnError(int requestCode, String msg, Throwable e) {
        hideProgressBar();
        if (!(e instanceof CommException)) {
            Toasty.error(activity, msg, Toast.LENGTH_SHORT).show();
        }
        LogUtils.d("任务出现错误了");
    }

    @Override
    public void doOnCompleted(int requestCode) {
        hideProgressBar();
        LogUtils.d("任务完成了");
    }

    @Override
    public void doParseData(int requestCode, Object data) {

    }
    //========================rx相关end===============================

    //========================activity相关start================R===============


    @Override
    public LifecycleTransformer doBindLifecycle(int requestCode) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, FragmentEvent.DESTROY_VIEW);
        //  return this.bindToLifecycle();
    }
}

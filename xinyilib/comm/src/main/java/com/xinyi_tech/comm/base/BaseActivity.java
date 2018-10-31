package com.xinyi_tech.comm.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.xinyi_tech.comm.CommException;

import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import rx.Observable;
import rx.subjects.BehaviorSubject;


public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements LifecycleProvider<ActivityEvent>, BaseView {
    public final String TAG = this.getClass().getSimpleName();
    protected P mPresenter;
    protected boolean isCancelTaskWhenStop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupMvp();
        onCreateBefore(savedInstanceState);
        super.onCreate(savedInstanceState);
        LogUtils.dTag(TAG, "onCreate");
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        //设置布局内容
        setContentView(getLayoutId());
        //初始化黄油刀控件绑定框架
        ButterKnife.bind(this);
        //初始化操作
        onCreateAfter(savedInstanceState);
    }

    protected void onCreateBefore(Bundle savedInstanceState) {
    }

    protected abstract void onCreateAfter(Bundle savedInstanceState);


    @LayoutRes
    protected abstract int getLayoutId();


    private void setupMvp() {
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attach(this);
        }

    }

    protected abstract P getPresenter();


    //rx生命周期管控
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
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
        BaseUtils.showDialog(this, content, onCancelListener);
    }

    protected void hideProgressBar() {
        BaseUtils.hideDialog();
    }
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
    public LifecycleTransformer doBindLifecycle(int requestCode) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, ActivityEvent.DESTROY);
        // return this.bindToLifecycle();
    }


    @Override
    public void doOnError(int requestCode, String msg, Throwable e) {
        hideProgressBar();
        if (!(e instanceof CommException)) {
            Toasty.error(this, msg, Toast.LENGTH_SHORT).show();
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

    //========================activity相关start===============================


    @Override
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
        LogUtils.d(TAG + "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
        LogUtils.d(TAG + "onResume");
    }

    @Override
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
        LogUtils.d(TAG + "onPause");
    }

    @Override
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        if (isCancelTaskWhenStop) {
            if (mPresenter != null) {
                mPresenter.cancelAll();
            }
        }
        super.onStop();
        LogUtils.d(TAG + "onStop");
    }

    @Override
    protected void onDestroy() {
        LogUtils.d(TAG + "onDestroy");
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        if (mPresenter != null) {
            mPresenter.destroy();
        }
        super.onDestroy();
    }

    //========================activity相关end===============================


}

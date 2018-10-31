package com.xinyi_tech.comm.base;


import com.trello.rxlifecycle.LifecycleTransformer;

/**
 * MVPPlugin
 */

public interface BaseView {
    void doOnStart(int requestCode);

    void doOnError(int requestCode, String msg, Throwable e);

    void doOnCompleted(int requestCode);

    LifecycleTransformer doBindLifecycle(int requestCode);

    void doParseData(int requestCode, Object data);

}

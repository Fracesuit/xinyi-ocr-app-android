package com.xinyi_tech.comm.permission;

/**
 * Created by zhiren.zhang on 2017/6/21.
 */

public interface OnRequestPermissionsListener {
    void grant();//授予结果

    void completed();//完成，此处有可能是同意，有可能是拒绝

    void error(Throwable e);//错误

    void refuse();//拒绝
}

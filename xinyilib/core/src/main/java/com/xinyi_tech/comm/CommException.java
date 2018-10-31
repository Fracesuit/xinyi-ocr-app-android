package com.xinyi_tech.comm;

/**
 * Created by zhiren.zhang on 2018/3/28.
 */

public class CommException extends Exception {
    public CommException() {
    }

    public CommException(String messager) {
        super(messager);
    }
}

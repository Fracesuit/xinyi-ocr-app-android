package com.xinyi_tech.comm.widget.seacher;

/**
 * Created by zhiren.zhang on 2018/1/10.
 */

public class SuperSeacherModel {
    private int drawable;
    private String title;
    private Object data;

    public SuperSeacherModel(int drawable, String title) {
        this.drawable = drawable;
        this.title = title;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

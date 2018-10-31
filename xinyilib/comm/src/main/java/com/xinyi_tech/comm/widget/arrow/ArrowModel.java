package com.xinyi_tech.comm.widget.arrow;

/**
 * Created by Fracesuit on 2018/1/8.
 */

public class ArrowModel {
    private String name;
    private String value;
    private Object data;

    public ArrowModel(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

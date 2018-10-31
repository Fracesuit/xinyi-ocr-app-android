package com.xinyi_tech.comm.form;

/**
 * Created by Fracesuit on 2017/8/17.
 */

public class DictField {
    private String name;
    private String value;
    private String type;
    private Object data;

    public Object getData() {
        return data;
    }

    public DictField setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return name;
    }

    public DictField() {
    }

    public DictField(String name, String value) {
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

    public String getType() {
        return type;
    }

    public DictField setType(String type) {
        this.type = type;
        return this;
    }
}

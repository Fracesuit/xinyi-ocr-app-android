package com.xinyi_tech.comm.picker.file;


import com.camera.internal.utils.SDCardUtils2;

import java.io.Serializable;

/**
 * Created by Fracesuit on 2018/1/8.
 */

public class FileSelectBuild implements Serializable {
    private int maxSelectCount;
    private String[] mFileTypes;//你要选择的文件
    private String rootPath;//开始的路径
    private boolean isRecursive;//是否要递归文件夹

    private FileSelectBuild(Builder builder) {
        maxSelectCount = builder.maxSelectCount;
        mFileTypes = builder.mFileTypes;
        rootPath = builder.rootPath;
        isRecursive = builder.isRecursive;
    }

    public int getMaxSelectCount() {
        return maxSelectCount;
    }

    public String[] getmFileTypes() {
        return mFileTypes;
    }

    public String getRootPath() {
        return rootPath;
    }

    public boolean isRecursive() {
        return isRecursive;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private int maxSelectCount = 5;
        private String[] mFileTypes;
        private String rootPath = SDCardUtils2.getExternalPublic(null).getAbsolutePath();
        private boolean isRecursive = false;
        private int requestCode;

        private Builder() {
        }

        public Builder maxSelectCount(int val) {
            maxSelectCount = val;
            return this;
        }

        public Builder requestCode(int val) {
            requestCode = val;
            return this;
        }

        public Builder mFileTypes(String[] val) {
            mFileTypes = val;
            return this;
        }

        public Builder rootPath(String val) {
            rootPath = val;
            return this;
        }

        public Builder isRecursive(boolean val) {
            isRecursive = val;
            return this;
        }

        public FileSelectBuild build() {
            return new FileSelectBuild(this);
        }


    }
}

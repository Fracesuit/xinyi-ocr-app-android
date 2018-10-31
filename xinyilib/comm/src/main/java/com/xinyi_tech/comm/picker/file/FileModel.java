package com.xinyi_tech.comm.picker.file;

import android.os.Parcel;
import android.os.Parcelable;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.StringUtils;
import com.xinyi_tech.comm.R;

/**
 * Created by Fracesuit on 2018/1/8.
 */

public class FileModel implements Parcelable {
    public static final String FILE_DOC = "doc";
    public static final String FILE_XLS = "xls";
    public static final String FILE_PPT = "ppt";
    public static final String FILE_PDF = "pdf";
    public static final String FILE_TXT = "txt";

    private String fileName;
    private String filePath;//绝对路径
    private int res;
    private boolean isSelect;//是否选中
    private boolean isDir;//是否是文件夹

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getRes() {
        if (isDir()) {
            res = R.mipmap.icon_wjj;
        } else {
            final String fileExtension = FileUtils.getFileExtension(filePath);
            if (StringUtils.isEmpty(fileExtension)) {
                res = R.mipmap.icon_file;
            } else if (fileExtension.contains(FILE_DOC)) {
                res = R.mipmap.icon_word;
            } else if (fileExtension.contains(FILE_XLS)) {
                res = R.mipmap.icon_excel;
            } else if (fileExtension.contains(FILE_PPT)) {
                res = R.mipmap.icon_ppt;
            } else if (fileExtension.contains(FILE_PDF)) {
                res = R.mipmap.icon_pdf;
            } else if (fileExtension.contains(FILE_TXT)) {
                res = R.mipmap.icon_txt;
            } else {
                res = R.mipmap.icon_file;
            }
        }
        return res;
    }


    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileModel fileModel = (FileModel) o;

        return filePath.equals(fileModel.filePath);

    }

    @Override
    public int hashCode() {
        return 0;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fileName);
        dest.writeString(this.filePath);
        dest.writeInt(this.res);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isDir ? (byte) 1 : (byte) 0);
    }

    public FileModel() {
    }

    protected FileModel(Parcel in) {
        this.fileName = in.readString();
        this.filePath = in.readString();
        this.res = in.readInt();
        this.isSelect = in.readByte() != 0;
        this.isDir = in.readByte() != 0;
    }

    public static final Creator<FileModel> CREATOR = new Creator<FileModel>() {
        @Override
        public FileModel createFromParcel(Parcel source) {
            return new FileModel(source);
        }

        @Override
        public FileModel[] newArray(int size) {
            return new FileModel[size];
        }
    };
}

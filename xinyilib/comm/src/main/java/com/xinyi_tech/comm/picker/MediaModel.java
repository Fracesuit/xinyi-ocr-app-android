package com.xinyi_tech.comm.picker;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiren.zhang on 2018/3/27.
 */

public class MediaModel implements MultiItemEntity, Parcelable {
    private String mediaName;
    private boolean mustSelect;
    private String path;
    private int itemType;//文件类型
    private long duration;//如果是音频或者视频就要有时间
    private int placeHolderRes = -1;
    @DrawableRes
    private int drawableId;
    private boolean isDelete;
    private String id;

    public MediaModel(int itemType) {
        this.itemType = itemType;
    }


    public int getPlaceHolderRes() {
        return placeHolderRes;
    }

    public MediaModel setPlaceHolderRes(int placeHolderRes) {
        this.placeHolderRes = placeHolderRes;
        return this;
    }

    public long getDuration() {
        return duration;
    }

    public MediaModel setDuration(long duration) {
        this.duration = duration;
        return this;
    }


    public String getPath() {
        return path;
    }

    public MediaModel setPath(String path) {
        this.path = path;
        return this;
    }


    public boolean isMustSelect() {
        return mustSelect;
    }

    public MediaModel setMustSelect(boolean mustSelect) {
        this.mustSelect = mustSelect;
        return this;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public MediaModel setDelete(boolean delete) {
        isDelete = delete;
        return this;
    }

    public String getId() {
        return id;
    }

    public MediaModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getMediaName() {
        return mediaName;
    }

    public MediaModel setMediaName(String mediaName) {
        this.mediaName = mediaName;
        return this;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaModel that = (MediaModel) o;

        return getPath().equals(that.getPath());

    }

    @Override
    public int hashCode() {
        return 0;
    }

    public static LocalMedia mediaModelLocalMedia(MediaModel mediaModel) {
        LocalMedia localMedia = new LocalMedia();
        localMedia.setPath(mediaModel.getPath());
        localMedia.setDuration(mediaModel.getDuration());
        localMedia.setMimeType(mediaModel.getItemType());
        return localMedia;
    }

    public static List<LocalMedia> mediaModelsLocalMedias(List<MediaModel> mediaModels) {
        ArrayList<LocalMedia> localMedias = new ArrayList<>();
        for (MediaModel mediaModel : mediaModels) {
            localMedias.add(mediaModelLocalMedia(mediaModel));
        }
        return localMedias;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mediaName);
        dest.writeByte(this.mustSelect ? (byte) 1 : (byte) 0);
        dest.writeString(this.path);
        dest.writeInt(this.itemType);
        dest.writeLong(this.duration);
        dest.writeInt(this.placeHolderRes);
        dest.writeInt(this.drawableId);
    }

    protected MediaModel(Parcel in) {
        this.mediaName = in.readString();
        this.mustSelect = in.readByte() != 0;
        this.path = in.readString();
        this.itemType = in.readInt();
        this.duration = in.readLong();
        this.placeHolderRes = in.readInt();
        this.drawableId = in.readInt();
    }

    public static final Parcelable.Creator<MediaModel> CREATOR = new Parcelable.Creator<MediaModel>() {
        @Override
        public MediaModel createFromParcel(Parcel source) {
            return new MediaModel(source);
        }

        @Override
        public MediaModel[] newArray(int size) {
            return new MediaModel[size];
        }
    };
}

package com.xinyi_tech.comm.picker.file;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.camera.internal.utils.SDCardUtils2;
import com.xinyi_tech.comm.picker.MediaModel;
import com.xinyi_tech.comm.picker.MediaType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fracesuit on 2018/1/8.
 */

public class FileSelectHelp {
    private int maxSelectCount = 5;
    private int requestCode;
    private String[] mFileTypes;//你要选择的文件
    private Activity activity;
    private Fragment fragment;
    private String rootPath = SDCardUtils2.getExternalPublic(null).getAbsolutePath();//开始的路径
    private boolean isRecursive = false;//是否要递归文件夹


    public FileSelectHelp withActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public FileSelectHelp withFragment(Fragment fragment) {
        this.fragment = fragment;
        return this;
    }

    public FileSelectHelp setMaxSelectCount(int maxSelectCount) {
        this.maxSelectCount = maxSelectCount;
        return this;
    }

    public FileSelectHelp setmFileTypes(String... mFileTypes) {
        this.mFileTypes = mFileTypes;
        return this;
    }

    public FileSelectHelp setRootPath(String rootPath) {
        if (rootPath != null) {
            this.rootPath = rootPath;
        }

        return this;
    }

    public FileSelectHelp setRecursive(boolean recursive) {
        isRecursive = recursive;
        return this;
    }

    public FileSelectHelp setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public void start() {
        final FileSelectBuild build = FileSelectBuild.newBuilder()
                .maxSelectCount(maxSelectCount)
                .mFileTypes(mFileTypes)
                .isRecursive(isRecursive)
                .requestCode(requestCode)
                .rootPath(rootPath)
                .build();
        if (fragment != null) {
            activity = fragment.getActivity();
        }
        final Intent intent = new Intent(activity, FileSelectActivity.class);
        intent.putExtra("fileSelectBuilder", build);
        activity.startActivityForResult(intent, requestCode);
    }

    public static List<MediaModel> onActivityResult(Intent data) {
        final ArrayList<MediaModel> localMedias = new ArrayList<>();
        if (data != null) {
            final ArrayList<FileModel> selectedList = data.getParcelableArrayListExtra("selectedList");
            if (selectedList != null) {
                for (FileModel fileModel : selectedList) {
                    final MediaModel localMedia = new MediaModel(MediaType.ofAttachment());
                    localMedia.setMediaName(fileModel.getFileName());
                    localMedia.setPlaceHolderRes(fileModel.getRes());
                    localMedia.setPath(fileModel.getFilePath());
                    localMedias.add(localMedia);
                }
            }

        }
        return localMedias;
    }

}

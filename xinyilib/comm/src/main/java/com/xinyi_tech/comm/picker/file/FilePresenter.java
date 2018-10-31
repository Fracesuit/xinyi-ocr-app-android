package com.xinyi_tech.comm.picker.file;

import com.blankj.utilcode.util.FileUtils;
import com.xinyi_tech.comm.base.BasePresenter;
import com.xinyi_tech.comm.util.FileUtils2;

import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Fracesuit on 2018/1/8.
 */

public class FilePresenter extends BasePresenter {

    public void getFileByDir(final String dirPath, final FileFilter fileFilter, final boolean isRecursive, int requestCode) {
        execute(Observable.just(dirPath)
                .map(new Func1<String, List<File>>() {
                    @Override
                    public List<File> call(String dirPath) {
                        final List<File> files = FileUtils.listFilesInDirWithFilter(dirPath, fileFilter, isRecursive);
                        Collections.sort(files, new FileComparator());
                        return files;
                    }
                })
                .flatMap(new Func1<List<File>, Observable<File>>() {
                    @Override
                    public Observable<File> call(List<File> files) {
                        return Observable.from(files);
                    }
                })
                .map(new Func1<File, FileModel>() {
                    @Override
                    public FileModel call(File file) {
                        final FileModel fileModel = new FileModel();
                        fileModel.setFilePath(file.getAbsolutePath());
                        fileModel.setSelect(false);
                        fileModel.setDir(FileUtils.isDir(file));
                        fileModel.setFileName(fileModel.isDir() ? FileUtils2.getLastDirName(file.getAbsolutePath()) : FileUtils.getFileName(file));
                        return fileModel;
                    }
                })
                .toList(), requestCode);
    }
}

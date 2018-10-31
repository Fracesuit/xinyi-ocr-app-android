package com.xinyi_tech.comm.picker;

import android.app.Activity;

import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xinyi_tech.comm.picker.file.FileSelectHelp;
import com.xinyi_tech.comm.util.FileUtils2;

import java.util.List;

/**
 * Created by Fracesuit on 2018/1/17.
 */

public class PickUtils {

    public static PictureSelectionModel camera(Activity activity, int mineType) {
        return comm(PictureSelector.create(activity).openCamera(mineType));//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
    }

    public static PictureSelectionModel gallery(Activity activity, int mineType) {
        return comm(PictureSelector.create(activity).openGallery(mineType));//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
    }

    //FileModel.FILE_DOC,FileModel.FILE_XLS,FileModel.FILE_PDF
    public static void attachment(Activity activity, int maxSelectCount, String rootPath, int requestCode, String... mFileTypes) {
        new FileSelectHelp()
                .withActivity(activity)
                .setMaxSelectCount(maxSelectCount)
                .setRequestCode(requestCode)
                .setRootPath(rootPath)
                .setmFileTypes(mFileTypes).start();
    }


    public static void pick(PictureSelectionModel pictureSelectionModel,
                            int maxSelectCount, List<LocalMedia> list, int requestCode) {
        pictureSelectionModel//  .theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(maxSelectCount)// 最大图片选择数量 int
                .selectionMode(maxSelectCount == 1 ? PictureConfig.SINGLE : PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .selectionMedia(list)// 是否传入已选图片 List<LocalMedia> list
                .forResult(requestCode);//结果回调onActivityResult code
    }

    public static PictureSelectionModel crop(PictureSelectionModel pictureSelectionModel,
                                             int aspect_ratio_x, int aspect_ratio_y,
                                             boolean isRoundness) {
        pictureSelectionModel.enableCrop(true)// 是否裁剪 true or false
                .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .circleDimmedLayer(isRoundness)// 是否圆形裁剪 true or false
                .showCropFrame(!isRoundness)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(!isRoundness);// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
        return pictureSelectionModel;
    }


    private static PictureSelectionModel comm(PictureSelectionModel pictureSelectionModel) {
        pictureSelectionModel
                .setRootPathDir(FileUtils2.getPhotoDir().getPath())// 自定义拍照保存路径,可不填
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(true)// 是否可预览视频 true or false
                .enablePreviewAudio(true) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .compress(true)// 是否压缩 true or false
                // .glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(100)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //  .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .videoQuality(1)// 视频录制质量 0 or 1 int
                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                .recordVideoSecond(60);//视频秒数录制 默认60s int
        // .isDragFrame(false)// 是否可拖动裁剪框(固定)

        return pictureSelectionModel;
    }

}

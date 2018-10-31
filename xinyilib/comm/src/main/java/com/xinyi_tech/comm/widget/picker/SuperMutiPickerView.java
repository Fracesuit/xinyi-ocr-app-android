package com.xinyi_tech.comm.widget.picker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.StringUtils;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xinyi_tech.comm.form.IFormField;
import com.xinyi_tech.comm.help.recycleview.RecyclerViewHelper;
import com.xinyi_tech.comm.picker.MediaModel;
import com.xinyi_tech.comm.picker.MediaType;
import com.xinyi_tech.comm.picker.PickUtils;
import com.xinyi_tech.comm.picker.PickerPopupWindow;
import com.xinyi_tech.comm.picker.file.FileSelectFragment;
import com.xinyi_tech.comm.picker.file.FileSelectHelp;
import com.xinyi_tech.comm.util.ToastyUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Fracesuit on 2017/10/21.
 */

public class SuperMutiPickerView extends FrameLayout implements IFormField {
    private PickerPopupWindow popupWindow;
    private MutiPickerAdapter gridImageAdapter;
    Builder builder;

    public SuperMutiPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setupParams(Builder builder) {
        this.builder = builder;
        initPickView();
    }

    private void initPickView() {
        popupWindow = PickerPopupWindow.newBuilder(builder.getActivity())
                .supportAttachment(builder.supportAttachment)
                .supportGallery(builder.supportGallery)
                .supportAudio(builder.supportAudio)
                .supportCamera(builder.supportCamera)
                .supportVideo(builder.supportVideo)
                .pickerListener(new PickerPopupWindow.PickerListener() {
                    @Override
                    public void camera() {
                        PictureSelectionModel selectionModel = PickUtils.camera(builder.getActivity(), PictureMimeType.ofImage());
                        PickUtils.pick(builder.supportCrop ? PickUtils.crop(selectionModel, 0, 0, false)
                                : selectionModel, 1, null, builder.getRequestCode());
                    }

                    @Override
                    public void gallery() {
                        goPickerGrallery(PictureMimeType.ofImage());
                    }

                    @Override
                    public void attachment() {
                        goPickerAttachment();
                    }

                    @Override
                    public void video() {
                        goPickerGrallery(PictureMimeType.ofVideo());
                    }

                    @Override
                    public void audio() {
                        goPickerGrallery(PictureMimeType.ofAudio());
                    }
                }).build();
        gridImageAdapter.withActivity(builder.getActivity())
                .setSelectMax(builder.getMaxSelectCount())
                .setLookMode(builder.lookMode)
                .setRequestCode(builder.getRequestCode())
                .setPlaceHolderMode(builder.isPlaceHolderMode())
                .setOnPlaceHolderClickListener(new MutiPickerAdapter.OnPlaceHolderClickListener() {
                    @Override
                    public void onClick(int reqestCode, int itemType) {
                        if (itemType == MediaType.ofAttachment()) {
                            goPickerAttachment();
                        } else {
                            if (builder.isOnlyCamera) {
                                PickUtils.camera(builder.getActivity(), reqestCode);
                            } else {
                                final List<MediaModel> data = gridImageAdapter.getData();
                                final ArrayList<MediaModel> localMedias = new ArrayList<>();
                                for (MediaModel m : data) {
                                    if (m.getItemType() == itemType && !StringUtils.isEmpty(m.getPath())) {
                                        localMedias.add(m);
                                    }
                                }
                                PictureSelectionModel selectionModel = PickUtils.gallery(builder.getActivity(), itemType);
                                PickUtils.pick(builder.supportCrop ? PickUtils.crop(selectionModel, 0, 0, false) : selectionModel, 1, MediaModel.mediaModelsLocalMedias(localMedias), reqestCode);
                            }

                        }
                    }
                });

    }


    private void init() {
        final RecyclerView recyclerView = new RecyclerView(getContext());
        final LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        gridImageAdapter = new MutiPickerAdapter();
        gridImageAdapter.setmOnAddPicClickListener(new MutiPickerAdapter.OnAddPicClickListener() {
            @Override
            public void onAddPicClick() {
                if (builder.isOnlyCamera) {
                    PickUtils.camera(builder.getActivity(), builder.getRequestCode());
                } else if (builder.isOnlyPicture) {
                    goPickerGrallery(PictureMimeType.ofImage());
                } else if (builder.isOnlyAttachment) {
                    goPickerAttachment();
                } else {
                    popupWindow.showPopupWindow();
                }

            }
        });

        RecyclerViewHelper.initRecyclerViewH(recyclerView, false, gridImageAdapter);
        addView(recyclerView, layoutParams);
    }


    //动态设置查看模式
  /*  public void setLookMode(boolean lookMode) {
        gridImageAdapter.setLookMode(lookMode)
                .notifyDataSetChanged();
    }*/
    //如果设置非查看模式的时候，那么http的图片也会显示一个差号，此时可以通过属性判断，是否http也要显示差号，默认显示。
    //删除的时候，一个是弹框，一个是不删除，给一个标记就行了。这个需要设置属性。
    //占位模式


    @Override
    public Object getValue() {
        if (gridImageAdapter.getData().size() == 0) {
            return null;
        } else if (builder.getMaxSelectCount() == 1) {
            return gridImageAdapter.getData().get(0).getPath();
        } else {
            final List<String> strings = new ArrayList<>();
            for (MediaModel m : gridImageAdapter.getData()) {
                if (m.isMustSelect() && StringUtils.isEmpty(m.getPath())) {
                    //这是针对占位模式的判断
                    ToastyUtil.warningShort(m.getMediaName() + "图片不得为空");
                    return null;
                }
                strings.add(m.getPath());
            }
            return strings;
        }

    }

    @Override
    public void setVaule(Object value) {//传入的是List<String>
        if (value != null) {
            final ArrayList<MediaModel> localMedias = new ArrayList<>();
            if (value instanceof List) {
                final List<String> strs = (List<String>) value;
                for (String s : strs) {
                    final MediaModel localMedia = new MediaModel(MediaType.getMediaTypeByPath(s));
                    localMedia.setPath(s);
                    localMedias.add(localMedia);
                }
            } else if (value instanceof String) {
                String tempValue = (String) value;
                final MediaModel localMedia = new MediaModel(MediaType.getMediaTypeByPath(tempValue));
                localMedia.setPath(tempValue);
                localMedias.add(localMedia);
            }
            gridImageAdapter.setNewData(localMedias);
        }

    }


    public List<MediaModel> getDatas() {
        final List<MediaModel> data = gridImageAdapter.getData();
        for (MediaModel m : data) {
            if (m.isMustSelect() && StringUtils.isEmpty(m.getPath())) {
                ToastyUtil.warningShort(m.getMediaName() + "图片不得为空");
                return null;
            }
        }
        return data;
    }

    public List<String> getDataPaths() {
        final ArrayList<String> strings = new ArrayList<>();
        List<MediaModel> datas = getDatas();
        if (datas == null) {
            return null;
        }
        for (MediaModel m : datas) {
            strings.add(m.getPath());
        }
        return strings;
    }

    public void setDatas(List<MediaModel> list) {
        gridImageAdapter.setNewData(list);
        gridImageAdapter.notifyDataSetChanged();
    }

    public void setDataPaths(List<String> pic) {
        final ArrayList<MediaModel> localMedias = new ArrayList<>();
        for (String s : pic) {
            final MediaModel localMedia = new MediaModel(MediaType.getMediaTypeByPath(s));
            localMedia.setPath(s);
            localMedias.add(localMedia);
        }
        gridImageAdapter.setNewData(localMedias);
    }


    /**
     * 选择附件
     */
    private void goPickerAttachment() {
        final List<MediaModel> data = gridImageAdapter.getData();
        int maxCount = builder.getMaxSelectCount();
        PickUtils.attachment(builder.getActivity(), maxCount - data.size(), builder.selectAttachmentRootPath, builder.getRequestCode(), builder.attachmentType);
    }

    /**
     * 相册---照片  视频  录音
     *
     * @param type
     */
    private void goPickerGrallery(int type) {
        final List<MediaModel> data = gridImageAdapter.getData();
        int maxCount = builder.getMaxSelectCount();
        PictureSelectionModel selectionModel = PickUtils.gallery(builder.getActivity(), type);
        PickUtils.pick(builder.supportCrop ? PickUtils.crop(selectionModel, 0, 0, false)
                : selectionModel, maxCount - data.size(), null, builder.getRequestCode());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final List<MediaModel> oldLocalMedia = gridImageAdapter.getData();
        final int realCode = requestCode - builder.getRequestCode();//requestCode
        if (builder.isPlaceHolderMode() && realCode < oldLocalMedia.size() && realCode >= 0 && data != null) {
            //占位模式
            final MediaModel localMedia = oldLocalMedia.get(realCode);
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null && selectList.size() > 0) {
                final LocalMedia selectData = selectList.get(0);
                localMedia.setPath(selectData.gerRealPath());
                localMedia.setDuration(selectData.getDuration());
                gridImageAdapter.notifyItemChanged(realCode);
            }

        } else if (requestCode == builder.getRequestCode() && data != null) {
            List<MediaModel> localMedias = new ArrayList<>();
            if (FileSelectFragment.RESULT_FILE_SELECT == resultCode) {
                List<MediaModel> mediaModels = FileSelectHelp.onActivityResult(data);
                if (mediaModels != null && mediaModels.size() > 0) {
                    localMedias.addAll(mediaModels);
                }
            } else {
                List<LocalMedia> tempList = PictureSelector.obtainMultipleResult(data);
                if (tempList != null && tempList.size() > 0) {
                    for (LocalMedia m : tempList) {
                        final MediaModel localMedia = new MediaModel(m.getMimeType());
                        localMedia.setPath(m.gerRealPath());
                        localMedia.setDuration(m.getDuration());
                        localMedias.add(localMedia);
                    }
                }
            }
            for (MediaModel m : localMedias) {
                if (oldLocalMedia.size() == 0 || !oldLocalMedia.contains(m)) {
                    oldLocalMedia.add(m);
                }
            }
            gridImageAdapter.notifyDataSetChanged();
        }
    }


    public static final class Builder {
        private int maxSelectCount = 9;
        private boolean lookMode = true;
        private String[] attachmentType;
        private String selectAttachmentRootPath;
        private boolean supportCamera = true;
        private boolean supportGallery = true;
        private boolean supportAttachment;
        private boolean supportVideo;
        private boolean supportAudio;
        private boolean supportCrop;
        private boolean isOnlyCamera = false;
        private MutiPickerAdapter adapter;
        private boolean isOnlyPicture = false;
        private boolean isOnlyAttachment = false;
        private boolean isPlaceHolderMode = false;
        private int requestCode;
        private Activity activity;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder maxSelectCount(int val) {
            maxSelectCount = val;
            return this;
        }

        public Builder selectAttachmentRootPath(String val) {
            selectAttachmentRootPath = val;
            return this;
        }


        public Builder lookMode(boolean val) {
            lookMode = val;
            return this;
        }

        public Builder lookMode(MutiPickerAdapter val) {
            adapter = val;
            return this;
        }

        public Builder attachmentType(String... val) {
            attachmentType = val;
            return this;
        }

        public Builder supportCamera(boolean val) {
            supportCamera = val;
            return this;
        }

        public Builder supportGallery(boolean val) {
            supportGallery = val;
            return this;
        }

        public Builder supportAttachment(boolean val) {
            supportAttachment = val;
            return this;
        }

        public Builder supportVideo(boolean val) {
            supportVideo = val;
            return this;
        }

        public Builder supportAudio(boolean val) {
            supportAudio = val;
            return this;
        }

        public Builder supportCrop(boolean val) {
            supportCrop = val;
            return this;
        }

        public Builder isOnlyCamera(boolean val) {
            isOnlyCamera = val;
            return this;
        }

        public Builder isOnlyPicture(boolean val) {
            isOnlyPicture = val;
            return this;
        }

        public Builder isOnlyAttachment(boolean val) {
            isOnlyAttachment = val;
            return this;
        }

        public Builder isPlaceHolderMode(boolean val) {
            isPlaceHolderMode = val;
            return this;
        }

        public Builder requestCode(int val) {
            requestCode = val;
            return this;
        }

        public Builder activity(Activity val) {
            activity = val;
            return this;
        }

        public int getMaxSelectCount() {
            return maxSelectCount;
        }

        public boolean isLookMode() {
            return lookMode;
        }

        public String[] getAttachmentType() {
            return attachmentType;
        }

        public String getSelectAttachmentRootPath() {
            return selectAttachmentRootPath;
        }

        public boolean isSupportCamera() {
            return supportCamera;
        }

        public boolean isSupportGallery() {
            return supportGallery;
        }

        public boolean isSupportAttachment() {
            return supportAttachment;
        }

        public boolean isSupportVideo() {
            return supportVideo;
        }

        public boolean isSupportAudio() {
            return supportAudio;
        }

        public boolean isOnlyCamera() {
            return isOnlyCamera;
        }

        public boolean isOnlyPicture() {
            return isOnlyPicture;
        }

        public boolean isOnlyAttachment() {
            return isOnlyAttachment;
        }

        public boolean isPlaceHolderMode() {
            return isPlaceHolderMode;
        }

        public int getRequestCode() {
            return requestCode;
        }

        public Activity getActivity() {
            return activity;
        }
    }
}

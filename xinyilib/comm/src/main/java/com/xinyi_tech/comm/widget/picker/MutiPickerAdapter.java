package com.xinyi_tech.comm.widget.picker;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.tools.DateUtils;
import com.luck.picture.lib.tools.StringUtils;
import com.xinyi_tech.comm.R;
import com.xinyi_tech.comm.picker.MediaModel;
import com.xinyi_tech.comm.picker.MediaType;
import com.xinyi_tech.comm.util.ToastyUtil;


public class MutiPickerAdapter extends BaseMultiItemQuickAdapter<MediaModel, BaseViewHolder> {
    private int selectMax = 9;
    private boolean isLookMode = false;
    private OnAddPicClickListener mOnAddPicClickListener;
    private Activity activity;
    private int requestCode;
    private boolean isPlaceHolderMode = false;//是否是占位模式


    public MutiPickerAdapter() {
        super(null);
        addItemType(MediaType.ofAdd(), R.layout.comm_item_super_select_image);
        addItemType(MediaType.ofImage(), R.layout.comm_item_super_select_image);
        addItemType(MediaType.ofVideo(), R.layout.comm_item_super_select_video);
        addItemType(MediaType.ofAudio(), R.layout.comm_item_super_select_video);
        addItemType(MediaType.ofAttachment(), R.layout.comm_item_super_select_file);
    }


    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return MediaType.ofAdd();
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        if (!isLookMode && getData().size() < selectMax) {
            return getData().size() + 1;
        } else {
            return getData().size();
        }
    }


    @Override
    protected void convert(final BaseViewHolder helper, final MediaModel model) {
        //处理add情况
        final int itemViewType = helper.getItemViewType();
        final SuperImageView mImg = helper.getView(R.id.fiv);
        if (itemViewType == MediaType.TYPE_ADD) {
            mImg.setOnImageViewClickListener(new SuperImageView.OnImageViewClickListener() {
                @Override
                public boolean onClick(boolean hasDrawable) {
                    mOnAddPicClickListener.onAddPicClick();
                    return true;
                }
            }).setImageResource(R.mipmap.icon_add_pic);
            return;
        }
        final String path = model.getPath();
        final int layoutPosition = helper.getLayoutPosition();

        //textview
        final TextView img_title = helper.getView(R.id.img_title);
        img_title.setVisibility(isPlaceHolderMode || itemViewType == MediaType.TYPE_ATTACHMENT ? View.VISIBLE : View.GONE);
        img_title.setText(model.getMediaName());
        if (model.isMustSelect()) {
            Drawable mustDrawable = ContextCompat.getDrawable(Utils.getApp(), R.mipmap.ic_red_star);
            StringUtils.modifyTextViewDrawable(img_title, mustDrawable, 0);
        }

     /*   mImg.setOnEditClickListener(new SuperImageView.OnEditClickListener() {
            @Override
            public boolean onClick() {
                if (onPlaceHolderClickListener != null) {
                    onPlaceHolderClickListener.onClick(requestCode + layoutPosition, model.getItemType());
                } else {
                    ToastyUtil.warningShort("请设置onPlaceHolderClickListener事件");
                }
                return true;
            }
        });*/

        //superimageview
        mImg.with(activity)
                .setLookMode(isLookMode);
        if (isPlaceHolderMode) {
            mImg.setImageHolder(model.getPlaceHolderRes() == -1 ? R.mipmap.icon_add_pic : model.getPlaceHolderRes())
                    .setRequestCode(requestCode + layoutPosition)
                    .setOnDeleteClickListener(new SuperImageView.OnDeleteClickListener() {
                        @Override
                        public boolean onClick() {
                            int index = helper.getLayoutPosition();
                            if (index != RecyclerView.NO_POSITION) {
                                final MediaModel mediaModel = getData().get(index);
                                mediaModel.setPath(null);
                            }
                            return false;
                        }
                    });
        } else {
            mImg.setOnDeleteClickListener(new SuperImageView.OnDeleteClickListener() {
                @Override
                public boolean onClick() {
                    int index = helper.getLayoutPosition();
                    if (index != RecyclerView.NO_POSITION) {
                        getData().remove(index);
                        notifyItemRemoved(index);
                        notifyItemRangeChanged(index, getData().size());
                    }
                    return true;
                }
            });
        }
        switch (itemViewType) {
            case MediaType.TYPE_VIDEO:
                final TextView tv_duration = helper.getView(R.id.tv_duration);
                tv_duration.setText(DateUtils.timeParse(model.getDuration()));
                Drawable drawable = ContextCompat.getDrawable(Utils.getApp(), R.drawable.video_icon);
                StringUtils.modifyTextViewDrawable(tv_duration, drawable, 0);
                mImg
                        .setOnImageViewClickListener(new SuperImageView.OnImageViewClickListener() {
                            @Override
                            public boolean onClick(boolean hasDrawable) {
                                if (hasDrawable) {
                                    PictureSelector.create(activity).externalPictureVideo(path);
                                } else {
                                    if (onPlaceHolderClickListener != null) {
                                        onPlaceHolderClickListener.onClick(requestCode + layoutPosition, model.getItemType());
                                    } else {
                                        ToastyUtil.warningShort("请设置onPlaceHolderClickListener事件");
                                    }
                                }
                                return true;
                            }
                        }).show(path);
                ;
                break;
            case MediaType.TYPE_AUDIO:
                final TextView audioDuration = helper.getView(R.id.tv_duration);
                audioDuration.setText(DateUtils.timeParse(model.getDuration()));
                Drawable audioDrawable = ContextCompat.getDrawable(Utils.getApp(), R.drawable.picture_audio);
                StringUtils.modifyTextViewDrawable(audioDuration, audioDrawable, 0);
                mImg
                        .setOnImageViewClickListener(new SuperImageView.OnImageViewClickListener() {
                            @Override
                            public boolean onClick(boolean hasDrawable) {
                                if (hasDrawable) {
                                    PictureSelector.create(activity).externalPictureAudio(path);
                                } else {
                                    if (onPlaceHolderClickListener != null) {
                                        onPlaceHolderClickListener.onClick(requestCode + layoutPosition, model.getItemType());
                                    } else {
                                        ToastyUtil.warningShort("请设置onPlaceHolderClickListener事件");
                                    }
                                }
                                return true;
                            }
                        })
                        .setImgPath(path)
                        .setImageResource(R.drawable.audio_placeholder);
                break;
            case MediaType.TYPE_ATTACHMENT:
                mImg
                        .setOnImageViewClickListener(new SuperImageView.OnImageViewClickListener() {
                            @Override
                            public boolean onClick(boolean hasDrawable) {
                                if (hasDrawable) {
                                    ToastyUtil.successLong("查看附件文档,这个功能待开发");
                                } else {
                                    if (onPlaceHolderClickListener != null) {
                                        onPlaceHolderClickListener.onClick(requestCode + layoutPosition, model.getItemType());
                                    } else {
                                        ToastyUtil.warningShort("请设置onPlaceHolderClickListener事件");
                                    }
                                }
                                return true;
                            }
                        })
                        .setImgPath(path)
                        .setImageResource(model.getPlaceHolderRes());
                break;
            case PictureConfig.TYPE_IMAGE:
                mImg
                        .setOnImageViewClickListener(new SuperImageView.OnImageViewClickListener() {
                            @Override
                            public boolean onClick(boolean hasDrawable) {
                                if (hasDrawable) {
                                    PictureSelector.create(activity).externalPicturePreview(layoutPosition, MediaModel.mediaModelsLocalMedias(getData()));
                                } else {
                                    if (onPlaceHolderClickListener != null) {
                                        onPlaceHolderClickListener.onClick(requestCode + layoutPosition, model.getItemType());
                                    } else {
                                        ToastyUtil.warningShort("请设置onPlaceHolderClickListener事件");
                                    }
                                }
                                return true;
                            }
                        })
                        .show(path);
                break;
        }

    }

    private boolean isShowAddItem(int position) {
        int size = getData().size() == 0 ? 0 : getData().size();
        return !isLookMode && position == size;
    }

    public MutiPickerAdapter setLookMode(boolean lookMode) {
        isLookMode = lookMode;
        return this;
    }

    public MutiPickerAdapter setPlaceHolderMode(boolean placeHolderMode) {
        isPlaceHolderMode = placeHolderMode;
        return this;
    }

    public MutiPickerAdapter setSelectMax(int selectMax) {
        this.selectMax = selectMax;
        return this;
    }

    public MutiPickerAdapter setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public MutiPickerAdapter withActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public interface OnAddPicClickListener {
        void onAddPicClick();
    }

    public void setmOnAddPicClickListener(OnAddPicClickListener mOnAddPicClickListener) {
        this.mOnAddPicClickListener = mOnAddPicClickListener;
    }

    OnPlaceHolderClickListener onPlaceHolderClickListener;

    public void setOnPlaceHolderClickListener(OnPlaceHolderClickListener onPlaceHolderClickListener) {
        this.onPlaceHolderClickListener = onPlaceHolderClickListener;
    }

    public interface OnPlaceHolderClickListener {
        void onClick(int reqestCode, int itemType);
    }
}

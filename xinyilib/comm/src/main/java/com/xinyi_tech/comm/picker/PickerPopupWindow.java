package com.xinyi_tech.comm.picker;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.xinyi_tech.comm.R;

import butterknife.ButterKnife;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by zhiren.zhang on 2017/8/3.
 */

public class PickerPopupWindow extends BasePopupWindow implements View.OnClickListener {
    private PickerListener pickerListener;
    private boolean supportCamera;
    private boolean supportGallery;
    private boolean supportAttachment;
    private boolean supportVideo;
    private boolean supportAudio;

    private View popupView;

    private PickerPopupWindow(Builder builder) {
        super(builder.activity);
        setPopupWindowFullScreen(true);
        pickerListener = builder.pickerListener;
        supportCamera = builder.supportCamera;
        supportGallery = builder.supportGallery;
        supportAttachment = builder.supportAttachment;
        supportVideo = builder.supportVideo;
        supportAudio = builder.supportAudio;
        initView();
    }

    public static Builder newBuilder(@NonNull Activity activity) {
        return new Builder(activity);
    }

    public interface PickerListener {
        void camera();

        void gallery();

        void attachment();

        void video();

        void audio();
    }

    @Override
    protected Animation initShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 500);
    }

    @Override
    protected Animation initExitAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 500);
    }


    @Override
    public View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.comm_popup_select_image, null);
        return popupView;
    }

    private void initView() {
        final View tv_camera = ButterKnife.findById(popupView, R.id.tv_camera);
        final View tv_gallery = ButterKnife.findById(popupView, R.id.tv_gallery);
        final View tv_attachment = ButterKnife.findById(popupView, R.id.tv_attachment);
        final View tv_video = ButterKnife.findById(popupView, R.id.tv_video);
        final View tv_audio = ButterKnife.findById(popupView, R.id.tv_audio);
        final View tv_cancel = ButterKnife.findById(popupView, R.id.tv_cancel);
        tv_camera.setOnClickListener(this);
        tv_gallery.setOnClickListener(this);
        tv_attachment.setOnClickListener(this);
        tv_video.setOnClickListener(this);
        tv_audio.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

        tv_camera.setVisibility(supportCamera ? View.VISIBLE : View.GONE);
        tv_gallery.setVisibility(supportGallery ? View.VISIBLE : View.GONE);
        tv_attachment.setVisibility(supportAttachment ? View.VISIBLE : View.GONE);
        tv_video.setVisibility(supportVideo ? View.VISIBLE : View.GONE);
        tv_audio.setVisibility(supportAudio ? View.VISIBLE : View.GONE);
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    @Override
    public void onClick(View v) {
        if (pickerListener != null) {
            final int id = v.getId();
            if (id == R.id.tv_camera) {
                pickerListener.camera();
            } else if (id == R.id.tv_gallery) {
                pickerListener.gallery();
            } else if (id == R.id.tv_attachment) {
                pickerListener.attachment();
            } else if (id == R.id.tv_video) {
                pickerListener.video();
            } else if (id == R.id.tv_audio) {
                pickerListener.audio();
            }
        }

        this.dismissWithOutAnima();

    }

    public static final class Builder {
        private PickerListener pickerListener;
        private boolean supportCamera;
        private boolean supportGallery;
        private boolean supportAttachment;
        private boolean supportVideo;
        private boolean supportAudio;
        private Activity activity;

        private Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder pickerListener(PickerListener val) {
            pickerListener = val;
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

        public PickerPopupWindow build() {
            return new PickerPopupWindow(this);
        }
    }
}

package com.xinyi_tech.freedom.pick;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.camera.internal.utils.SDCardUtils2;
import com.xinyi_tech.comm.log.XinYiLog;
import com.xinyi_tech.comm.picker.MediaModel;
import com.xinyi_tech.comm.picker.MediaType;
import com.xinyi_tech.comm.picker.file.FileModel;
import com.xinyi_tech.comm.widget.picker.SuperMutiPickerView;
import com.xinyi_tech.freedom.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MutiSelectImageViewActivity extends AppCompatActivity {


    @BindView(R.id.simg)
    SuperMutiPickerView simg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muti_select_image_view);
        ButterKnife.bind(this);

        final ArrayList<MediaModel> localMedias = new ArrayList<>();

        String[] ss = {"测试1", "测试2", "测试3", "测试4", "测试5", "测试6"};
        for (String s : ss) {
            final MediaModel localMedia = new MediaModel(MediaType.ofImage());
            localMedia.setPlaceHolderRes(R.drawable.ic_camera);
            localMedia.setMediaName(s);
            localMedia.setMustSelect(true);
            localMedias.add(localMedia);
        }


        final SuperMutiPickerView.Builder builder = new SuperMutiPickerView.Builder(this)
                .supportAttachment(true)
                .supportAudio(true)
                .isPlaceHolderMode(false)
                .lookMode(false)
                .maxSelectCount(6)
                .selectAttachmentRootPath(SDCardUtils2.getExternalPublic(null).getAbsolutePath())
                .attachmentType(FileModel.FILE_DOC, FileModel.FILE_XLS, FileModel.FILE_PDF, FileModel.FILE_PPT)
                .supportVideo(true);
        simg.setupParams(builder);
        //simg.setDatas(localMedias);
    }

    @OnClick(R.id.btn)
    public void onViewClicked() {
        final List<MediaModel> selectImage = simg.getDatas();
        if (selectImage != null) {
            for (MediaModel l : selectImage) {
                XinYiLog.e(l.getMediaName() + "===========" + l.getPath());
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        simg.onActivityResult(requestCode, resultCode, data);
    }
}

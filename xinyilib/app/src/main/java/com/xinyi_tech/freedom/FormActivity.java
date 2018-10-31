package com.xinyi_tech.freedom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.blankj.utilcode.util.AppUtils;
import com.bumptech.glide.Glide;
import com.camera.internal.utils.SDCardUtils2;
import com.xinyi_tech.comm.form.FormLayout;
import com.xinyi_tech.comm.log.XinYiLog;
import com.xinyi_tech.comm.picker.MediaModel;
import com.xinyi_tech.comm.picker.MediaType;
import com.xinyi_tech.comm.picker.file.FileModel;
import com.xinyi_tech.comm.widget.picker.SuperImageView;
import com.xinyi_tech.comm.widget.picker.SuperMutiPickerView;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FormActivity extends AppCompatActivity {

    @BindView(R.id.form)
    FormLayout mForm;
    /*    @BindView(R.id.img)
        SuperImageView img;*/
    @BindView(R.id.img1)
    SuperImageView img1;
    @BindView(R.id.muti)
    SuperMutiPickerView muti;

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    private static Uri resourceIdToUri(int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + AppUtils.getAppPackageName() + FOREWARD_SLASH + resourceId);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        ButterKnife.bind(this);
        img1.with(this).setLookMode(false);
        //img1.show(String.valueOf(R.mipmap.test));
       // String path = resourceIdToUri(R.mipmap.test).getLastPathSegment();
        Glide.with(this).load(R.mipmap.test).into(img1);

        //mForm.addFieldView(FieldView.newBuilder("测试labe2111", "hh").mustInput(false).textIcon(R.mipmap.icon_test).fieldDivided(false).fieldIndex(5).valueViewType(1));
        //mForm.addFieldView(FieldView.newBuilder("测试labe277", "hh").mustInput(false).valueInitContent("ddddd").textIcon(R.mipmap.icon_test).fieldIndex(7).fieldDivided(false).valueViewType(2));
        //mForm.addFieldView(FieldView.newBuilder("测试labe2", "hh").mustInput(false).textIcon(R.mipmap.icon_test).fieldDivided(false).valueViewType(3));
        //mForm.addFieldView(FieldView.newBuilder().fieldName("test").mustInput(true).textIcon(R.mipmap.icon_test).fieldDivided(false).fieldIndex(1).valueViewType(1));
       // mForm.addFieldView(FieldView.newBuilder().fieldName("test").mustInput(true).textIcon(R.mipmap.icon_test).fieldDivided(false).fieldIndex(20).dataView(new FieldSpinner(null, this)));
        //mForm.addFieldView(FieldView.newBuilder("测试labe2", "hh").mustInput(false).textIcon(R.mipmap.icon_test).fieldDivided(false).valueViewType(5));
        //mForm.addFieldView(FieldView.newBuilder("TextView", "textview").mustInput(false).textIcon(R.mipmap.icon_test).fieldDivided(false).dataView(new TextView(this)).valueInitContent("nihao"));
        // mForm.addFieldView(FieldView.newBuilder("TextView", "textview").mustInput(false).textIcon(R.mipmap.icon_test).fieldDivided(false).dataView(new EditText(this)).valueInitContent("nihao").edittextLine(1));
        //  mForm.addFieldView(FieldView.newBuilder("测试labe2eee", "hrh").mustInput(false).textIcon(R.mipmap.icon_test).fieldDivided(false).fieldIndex(1).valueViewType(FieldView.TYPE_HIDDENVIEW));
        //  mForm.addFieldView(FieldView.newBuilder("测试labe233", "hh222").mustInput(false).textIcon(R.mipmap.icon_test).fieldDivided(false).fieldIndex(1).valueViewType(1));
        //XinYiLog.e(JSON.toJSONString(mForm.getParams(true)));
        // tag.setTags(new String[]{"测试1", "测试2", "测试3", "测试4", "测试5", "测试6", "测试1", "测试2", "测试3", "测试4", "测试5", "测试6"});
        //  FieldView textview = mForm.getFieldViewByName("textview", FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE);
        //  textview.setValue("textviewtextviewtextviewtextviewte");


        final ArrayList<MediaModel> localMedias = new ArrayList<>();
        String[] ss = {"测试1", "测试2"};
        for (String s : ss) {
            final MediaModel localMedia = new MediaModel(MediaType.ofImage());
            localMedia.setMediaName(s);
            localMedia.setPlaceHolderRes(R.drawable.ic_camera);
            localMedia.setMustSelect(true);
            localMedias.add(localMedia);
        }


        final SuperMutiPickerView.Builder builder = new SuperMutiPickerView.Builder(this)
                .supportAttachment(true)
                .supportAudio(true)
             //   .isPlaceHolderMode(true)
                .lookMode(false)
                .maxSelectCount(3)
                .selectAttachmentRootPath(SDCardUtils2.getExternalPrivateCache().getAbsolutePath())
                .attachmentType(FileModel.FILE_DOC, FileModel.FILE_XLS, FileModel.FILE_PDF, FileModel.FILE_PPT)
                .supportVideo(true);
        muti.setupParams(builder);
     //   muti.setDatas(localMedias);

    }

    public void getParams(View view) {
        if (mForm.checkForm(FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE)) {
            Map<String, Object> params = mForm.getParams(FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE);
            XinYiLog.e(params.toString());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //img.onActivityResult(requestCode, resultCode, data);
        img1.onActivityResult(requestCode, resultCode, data);
        muti.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}

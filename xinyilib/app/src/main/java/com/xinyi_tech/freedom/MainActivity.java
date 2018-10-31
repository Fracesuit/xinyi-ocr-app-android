package com.xinyi_tech.freedom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyi_tech.comm.adapter.ImageTitleAdapter;
import com.xinyi_tech.comm.base.BaseActivity;
import com.xinyi_tech.comm.base.BasePresenter;
import com.xinyi_tech.comm.help.recycleview.RecyclerViewHelper;
import com.xinyi_tech.comm.model.ImageTitleModel;
import com.xinyi_tech.comm.picker.file.FileModel;
import com.xinyi_tech.comm.picker.file.FileSelectHelp;
import com.xinyi_tech.comm.util.OverridePendingTransitionUtls;
import com.xinyi_tech.comm.util.ToastyUtil;
import com.xinyi_tech.comm.util.ToolbarUtils;
import com.xinyi_tech.freedom.advanced.AdvancedActivity;
import com.xinyi_tech.freedom.baserecycleviewadapterhelper.BaseRecycleViewAdapterHelperActivity;
import com.xinyi_tech.freedom.facebookrebound.FacebookReboundActivity;
import com.xinyi_tech.freedom.image.glide.GlideActivity;
import com.xinyi_tech.freedom.myview.MyViewActivity;
import com.xinyi_tech.freedom.pick.MutiImageActivity;
import com.xinyi_tech.freedom.pick.MutiSelectImageViewActivity;
import com.xinyi_tech.freedom.pick.SuperImageViewActivity;
import com.xinyi_tech.freedom.pick.SysFileSelectActivity;
import com.xinyi_tech.freedom.pick.pictureselector.SelectPhotoActivity;
import com.xinyi_tech.freedom.popup.PopupWindowActivity;
import com.xinyi_tech.freedom.rxjava1.Rxjava1Activity;
import com.xinyi_tech.freedom.tree.TreeActivity;
import com.xinyi_tech.freedom.xinyiduty.XinyiDutyActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity<BasePresenter> implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    private ImageTitleAdapter<ImageTitleModel> mImageTitleAdapter;


    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        ToolbarUtils.with(this, mToolBar).setTitle("main", true).build();
        mImageTitleAdapter = new ImageTitleAdapter();
        mImageTitleAdapter.setOnItemClickListener(this);
        mImageTitleAdapter.addAllDataOnlyVisible(getMainItem());
        RecyclerViewHelper.initRecyclerViewG(mRecyclerview, true, mImageTitleAdapter, 5);
    }

    private ImageTitleModel generateImageTitleModel(int drawableId, String title, String uniquenessId, Class clazz) {
        ImageTitleModel model = new ImageTitleModel(drawableId, title);
        model.setClazz(clazz);
        model.setUniquenessId(uniquenessId);
        return model;
    }

    public List<ImageTitleModel> getMainItem() {
        ArrayList<ImageTitleModel> mainModeItems = new ArrayList<>();
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon1, "树形TreeActivity", "1", TreeActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon2, "图片配合title显示", "2", ImageTitleActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon3, "KeyValue显示", "3", KeyValueActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon4, "多种状态切换", "4", StateActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon5, "表单提交", "5", FormActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon7, "多种多样的tab", "6", TablayoutActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon9, "Glide使用操作", "7", GlideActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon11, "超级imageview", "8", SuperImageViewActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon12, "多种类型选择", "9", MutiSelectImageViewActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon12, "多图片选择2", "9", MutiImageActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon13, "自定义文件选择", "10", null));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon14, "查询搜索", "11", SeacherViewActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon16, "系统文件选择", "12", SysFileSelectActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon17, "自定义view", "13", MyViewActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon18, "选择图片视频录音文件", "14", SelectPhotoActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon18, "不用onActivityResult", "15", AdvancedActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon18, "Rxjava1Activity", "15", Rxjava1Activity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon18, "PopupWindowActivity", "15", PopupWindowActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon18, "BaseRecycleViewAdapterHelperActivity", "15", BaseRecycleViewAdapterHelperActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon18, "FaceBook的弹簧效果", "15", FacebookReboundActivity.class));
        mainModeItems.add(generateImageTitleModel(R.mipmap.icon18, "信义日志", "15", XinyiDutyActivity.class));
        Collections.sort(mainModeItems);
        return mainModeItems;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new BasePresenter();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ToastyUtil.warningShort(position + "");
        ImageTitleModel imageTitleModel = mImageTitleAdapter.getData().get(position);
        if ("10".equals(imageTitleModel.getUniquenessId())) {
            new FileSelectHelp()
                    .withActivity(this)
                    .setMaxSelectCount(3)
                    .setmFileTypes(FileModel.FILE_DOC,
                            FileModel.FILE_XLS,
                            FileModel.FILE_PDF).start();
            return;
        }
        Intent intent = new Intent(this, imageTitleModel.getClazz());
        intent.putExtra("title", imageTitleModel.getTitle());
        startActivity(intent);
        OverridePendingTransitionUtls.slideRightEntry(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* final List<FileModel> fileModels = FileSelectHelp.onActivityResult(requestCode, resultCode, data);
        for (FileModel model : fileModels) {
            XinYiLog.e("model===" + model.getFilePath());
        }*/
    }

    @Override
    public void doParseData(int requestCode, Object data) {

    }
}

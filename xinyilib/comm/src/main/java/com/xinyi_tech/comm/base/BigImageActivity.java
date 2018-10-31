package com.xinyi_tech.comm.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;
import com.xinyi_tech.comm.R;
import com.xinyi_tech.comm.util.ImageLoaderUtils;
import com.xinyi_tech.comm.util.OverridePendingTransitionUtls;
import com.xinyi_tech.comm.util.ToolbarUtils;

import butterknife.ButterKnife;

public class BigImageActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String DATA_PIC_PATH = "data_pic_path";
    Toolbar toolBar;
    PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comm_activity_big_image);
        ButterKnife.bind(this);
        toolBar = ButterKnife.findById(this, R.id.tool_bar);
        photoView = ButterKnife.findById(this, R.id.photo_view);
        initToolbar();
        int intPath = getIntent().getIntExtra(DATA_PIC_PATH, -1);
        if (intPath != -1) {
            ImageLoaderUtils.showImage(photoView, intPath);
        } else {
            ImageLoaderUtils.showImage(photoView, getIntent().getStringExtra(DATA_PIC_PATH));
        }

        photoView.setOnClickListener(this);
    }

    private void initToolbar() {
        ToolbarUtils.with(this, toolBar).setSupportBack(true).build();
    }


    @Override
    public void onClick(View v) {
        exit();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        finish();
        OverridePendingTransitionUtls.zoomInExit(this);
    }
}

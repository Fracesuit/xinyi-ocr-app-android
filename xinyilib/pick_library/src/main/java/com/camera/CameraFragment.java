package com.camera;

import android.Manifest;
import android.support.annotation.RequiresPermission;

import com.camera.configuration.Configuration;
import com.camera.internal.ui.BaseAnncaFragment;


public class CameraFragment extends BaseAnncaFragment {

    @RequiresPermission(Manifest.permission.CAMERA)
    public static CameraFragment newInstance(Configuration configuration) {
        return (CameraFragment) BaseAnncaFragment.newInstance(new CameraFragment(), configuration);
    }
}

package com.xinyi_tech.comm.advanced;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by zhiren.zhang on 2018/4/25.
 */

public class OnActResultEventDispatcherFragment extends Fragment {
    ActResultRequest.Callback callback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    public void startForResult(Intent intent, @NonNull ActResultRequest.Callback callback) {
        this.callback = callback;
        startActivityForResult(intent, 60000);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 60000) {
            callback.onActivityResult(requestCode, data);
        }
    }
}

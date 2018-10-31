package com.xinyi_tech.comm.advanced;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by zhiren.zhang on 2018/4/25.
 */

public class ActResultRequest {
    private static final String TAG = "on_act_result_event_dispatcher";
    private OnActResultEventDispatcherFragment fragment;

    private ActResultRequest(AppCompatActivity activityCompat) {
        withFragment(activityCompat);
    }

    public static ActResultRequest getInstance(AppCompatActivity activityCompat) {
        ActResultRequest actResultRequest = new ActResultRequest(activityCompat);
        return actResultRequest;
    }

    private void withFragment(AppCompatActivity activityCompat) {
        FragmentManager supportFragmentManager = activityCompat.getSupportFragmentManager();
        fragment = (OnActResultEventDispatcherFragment) supportFragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new OnActResultEventDispatcherFragment();
            supportFragmentManager.beginTransaction()
                    .add(fragment, TAG)
                    .commitAllowingStateLoss();
            supportFragmentManager.executePendingTransactions();
        }
    }

    public void startForResult(Intent intent,  Callback callback) {
        fragment.startForResult(intent, callback);
    }


    public interface Callback {
        void onActivityResult(int resultCode, Intent data);
    }
}


package com.xinyi_tech.freedom.advanced;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xinyi_tech.comm.advanced.ActResultRequest;
import com.xinyi_tech.comm.log.XinYiLog;
import com.xinyi_tech.freedom.R;

public class AdvancedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced);

        Intent intent = new Intent(this, AdvancedResultActivity.class);
        ActResultRequest.getInstance(this).startForResult(intent, new ActResultRequest.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                XinYiLog.e("ActResultRequest");
            }
        });
    }

}

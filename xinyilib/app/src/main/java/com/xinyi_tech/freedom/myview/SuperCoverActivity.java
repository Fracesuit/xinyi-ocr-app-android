package com.xinyi_tech.freedom.myview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xinyi_tech.freedom.R;

import butterknife.ButterKnife;

public class SuperCoverActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int viewIndex = getIntent().getIntExtra("viewIndex", -1);
        switch (viewIndex) {
            case -1:
                setContentView(R.layout.view_test);
                break;
            case 0:
                setContentView(R.layout.view_ruler);
                break;
            case 1:
                setContentView(R.layout.view_horizontal_expand_menu);
                break;
            case 2:
                setContentView(R.layout.view_wave);
                break;
            case 3:
                setContentView(R.layout.view_ant_forest);
                break;
            case 4:
                setContentView(R.layout.view_fish);
                break;
            case 5:
                setContentView(R.layout.view_piechart);
                break;
            case 6:
                setContentView(R.layout.view_progress);
                break;
            case 7:
                setContentView(R.layout.view_water_ripple);
                break;
            default:
                setContentView(R.layout.activity_super_cover);
        }

        ButterKnife.bind(this);
    }


/*    @OnClick(R.id.waveview)
    public void onViewClicked() {
        mWaveview.setProgress(50);
    }*/
}

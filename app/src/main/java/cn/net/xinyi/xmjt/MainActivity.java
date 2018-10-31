package cn.net.xinyi.xmjt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.kernal.passportreader.sdk.IdcardOrcActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //ArrowView arrowView=new ArrowView()
    }

    @OnClick({R.id.tv_zjsb, R.id.tv_cpsb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_zjsb:
                ActivityUtils.startActivity(this, IdcardOrcActivity.class);
                break;
            case R.id.tv_cpsb:
                break;
        }
    }
}

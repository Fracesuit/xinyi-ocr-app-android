package cn.net.xinyi.xmjt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.blankj.utilcode.util.ActivityUtils;
import com.camera.picturelib.CamersActivity;
import com.kernal.passport.sdk.utils.Devcode;
import com.kernal.passport.sdk.utils.SharedPreferencesHelper;
import com.kernal.passportreader.sdk.CameraActivity;
import com.kernal.plateid.PlateOcrActivity;

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

    @OnClick({R.id.tv_zjsb, R.id.tv_cpsb,R.id.tv_camera})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_zjsb:
                /**
                 * intent = new Intent(IdCardMainActivity.this,
                 CameraActivity.class);
                 intent.putExtra("nMainId", SharedPreferencesHelper.getInt(
                 getApplicationContext(), "nMainId", 2));
                 intent.putExtra("devcode", devcode);
                 intent.putExtra("flag", 0);
                 intent.putExtra("nCropType", 0);
                 intent.putExtra("VehicleLicenseflag", 2);
                 */
                Intent intent = new Intent(this,
                        CameraActivity.class);
                intent.putExtra("nMainId", SharedPreferencesHelper.getInt(
                        getApplicationContext(), "nMainId", 2));//
                intent.putExtra("devcode", Devcode.devcode);
                intent.putExtra("flag", 0);
                intent.putExtra("nCropType", 0);
                intent.putExtra("VehicleLicenseflag", 2);
                ActivityUtils.startActivity(intent);
                //ActivityUtils.startActivity(this, IdcardOrcActivity.class);
                break;
            case R.id.tv_cpsb:
                ActivityUtils.startActivity(this, PlateOcrActivity.class);
                break;
            case R.id.tv_camera:
                ActivityUtils.startActivity(this, CamersActivity.class);
                break;
        }
    }
}

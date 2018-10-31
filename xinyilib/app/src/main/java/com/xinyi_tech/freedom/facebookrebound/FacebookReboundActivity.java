package com.xinyi_tech.freedom.facebookrebound;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringChain;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.xinyi_tech.freedom.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FacebookReboundActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.tv2)
    TextView tv2    ;
    private Spring spring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_rebound);
        ButterKnife.bind(this);

        //针对单个元素
      /*  spring = SpringSystem.create().createSpring();
        spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(100,1));
        spring.addListener(new SpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                float scale = 1f - (value * 0.5f);
                tv.setScaleX(scale);
                tv.setScaleY(scale);
            }

            @Override
            public void onSpringAtRest(Spring spring) {

            }

            @Override
            public void onSpringActivate(Spring spring) {

            }

            @Override
            public void onSpringEndStateChange(Spring spring) {

            }
        });*/

      //创建对象
        SpringChain mSpringChain = SpringChain.create(60,2,40,2);

        //给子类添加弹簧
        mSpringChain.addSpring(new SimpleSpringListener(){
            @Override
            public void onSpringUpdate(Spring spring) {
                tv.setTranslationY((float) spring.getCurrentValue());
            }
        });
        mSpringChain.addSpring(new SimpleSpringListener(){
            @Override
            public void onSpringUpdate(Spring spring) {
                tv2.setTranslationY((float) spring.getCurrentValue());
            }
        });

        //设置一个主弹簧
        mSpringChain.setControlSpringIndex(0);

        //给弹簧设置初始值
        List<Spring> springs = mSpringChain.getAllSprings();
        for (Spring s : springs) {
            s.setCurrentValue(400);
        }

        //启动弹簧
        mSpringChain.getControlSpring().setEndValue(0);

    }

    @OnClick(R.id.btn)
    public void onViewClicked() {
       // spring.setCurrentValue(0);
       // spring.setEndValue(1);
    }
}

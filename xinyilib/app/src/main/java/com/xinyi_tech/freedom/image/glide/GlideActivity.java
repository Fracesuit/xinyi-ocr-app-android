package com.xinyi_tech.freedom.image.glide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.xinyi_tech.freedom.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GlideActivity extends AppCompatActivity {

    @BindView(R.id.img)
    ImageView mImg;

    String path = "http://192.168.1.144:8080/pic/2.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);
        ButterKnife.bind(this);
        GlideApp.with(this).load(path).into(mImg);
        //Glide.with(this).load(path).into(mImg);
    }
}

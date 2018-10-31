package com.xinyi_tech.freedom.pick;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xinyi_tech.comm.widget.picker.SuperImageView;
import com.xinyi_tech.freedom.R;

public class SuperImageViewActivity extends AppCompatActivity {

    private SuperImageView simg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_image_view);
        simg = (SuperImageView) findViewById(R.id.simg);
        ImageView circle = (ImageView) findViewById(R.id.circle);
        final RequestOptions requestOptions = new RequestOptions();
        requestOptions
                .apply(RequestOptions.circleCropTransform());
        Glide.with(this)
                .load(R.mipmap.test)
                .apply(requestOptions)
                .into(circle);
        simg.with(this);
      /*  simg.setOnDeleteClickListener(new SuperImageView.OnDeleteClickListener() {
            @Override
            public boolean onClick() {
                ToastyUtil.successLong("hhhh");
                return true;
            }
        });*/
        //simg.show(R.mipmap.test);
        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simg.clearImage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        simg.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}

package com.xinyi_tech.freedom.viewpager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xinyi_tech.comm.log.XinYiLog;
import com.xinyi_tech.freedom.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewpagerActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        ButterKnife.bind(this);
        mViewpager.setHorizontalFadingEdgeEnabled(false);
        mViewpager.setVerticalFadingEdgeEnabled(false);

        mViewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                XinYiLog.e("instantiateItem==" + position);
                TextView imageView = new TextView(ViewpagerActivity.this);
                imageView.setText(position + "  位置");
                container.addView(imageView);
                return imageView;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                XinYiLog.e("destroyItem==" + position);
                container.removeView((View) object);
                //super.destroyItem(container, position, object);
            }
           /* @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }*/
        });
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        int currentItem = mViewpager.getCurrentItem();
        currentItem++;
        if (currentItem == 3) currentItem = 0;
        mViewpager.setCurrentItem(currentItem,true);
    }

    //
}

package com.xinyi_tech.freedom.myview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.xinyi_tech.freedom.R;

public class SpringViewActivity extends AppCompatActivity {

    private SpringMenuView2 springMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spring_view);
        springMenuView = new SpringMenuView2(this, R.layout.menu_spring);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return  springMenuView.dispatchTouchEvent(ev);
    }
}

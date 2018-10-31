package com.xinyi_tech.comm.util;

import android.graphics.drawable.Drawable;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.xinyi_tech.comm.R;


public class ToolbarUtils {
    private AppCompatActivity appCompatActivity;
    private Toolbar toolbar;
    private int menuResId;
    private DrawerLayout drawerLayout;

    private boolean hasMenu = false;//menu
    private boolean back = false;//返回箭头
    private boolean drawlayout = false;//支持drawlayout侧边导航
    private boolean centerTitle = false;//标题栏在中间
    private String title;
    private int titleRes=-1;


    public Toolbar getToolbar() {
        return toolbar;
    }

    private ToolbarUtils(@NonNull AppCompatActivity appCompatActivity, @NonNull Toolbar toolbar) {
        this.appCompatActivity = appCompatActivity;
        this.toolbar = toolbar;
    }

    public static ToolbarUtils with(@NonNull AppCompatActivity appCompatActivity, @NonNull Toolbar toolbar) {
        ToolbarUtils toolbarUtils = new ToolbarUtils(appCompatActivity, toolbar);
        //不用支持actionbar
        ActionBar supportActionBar = appCompatActivity.getSupportActionBar();
        if (supportActionBar != null) {
            appCompatActivity.setSupportActionBar(null);
        }
        return toolbarUtils;
    }

    public void build() {
        if (back && drawlayout) {
            new IllegalArgumentException("返回箭头和导航栏仅仅支持一个");
        }

        if (back) {
            TintTypedArray a = TintTypedArray.obtainStyledAttributes(toolbar.getContext(), null, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
            Drawable mDefaultNavigationIcon = a.getDrawable(R.styleable.ActionBar_homeAsUpIndicator);
            toolbar.setNavigationIcon(mDefaultNavigationIcon);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appCompatActivity.finish();
                    OverridePendingTransitionUtls.slideRightExit(appCompatActivity);
                }
            });
        }
        if (hasMenu) {
            toolbar.inflateMenu(menuResId);
        }
        if (drawlayout) {
            ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(appCompatActivity,
                    drawerLayout,
                    toolbar,
                    android.R.string.ok,
                    android.R.string.cancel);
            mDrawerToggle.syncState();
            drawerLayout.addDrawerListener(mDrawerToggle);
        }
        if (title != null || titleRes != -1) {
            if (title != null) toolbar.setTitle(title);
            if (titleRes != -1) toolbar.setTitle(titleRes);
            toolbar.setContentInsetStartWithNavigation(0);
            toolbar.setContentInsetsRelative(0, 0);
            toolbar.setContentInsetsAbsolute(0, 0);
            if (centerTitle) {
                drawTitle();
            }
        }
    }


    public ToolbarUtils setInflateMenu(@MenuRes int resId) {
        hasMenu = true;
        menuResId = resId;
        return this;
    }

    public ToolbarUtils setInflateMenu(@MenuRes int resId, boolean hasMenu) {
        this.hasMenu = hasMenu;
        menuResId = resId;
        return this;
    }

    public ToolbarUtils setSupportBack(boolean isBack) {
        back = isBack;
        return this;
    }

    public ToolbarUtils setSupportDrawlayout(@NonNull DrawerLayout drawerLayout) {
        if (this.drawerLayout == null) {
            drawlayout = true;
            this.drawerLayout = drawerLayout;
        }

        return this;
    }

    public ToolbarUtils setTitle(@NonNull String title, boolean isCenter) {
        centerTitle = isCenter;
        this.title = title;
        return this;
    }

    public ToolbarUtils setTitle(@StringRes int titleRes, boolean isCenter) {
        centerTitle = isCenter;
        this.titleRes = titleRes;
        return this;
    }

    private ToolbarUtils drawTitle() {
        final AppCompatTextView textView = getTitleView();
        textView.setGravity(Gravity.CENTER);
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        layoutParams.width = Toolbar.LayoutParams.MATCH_PARENT;
        layoutParams.height = Toolbar.LayoutParams.MATCH_PARENT;
        textView.setLayoutParams(layoutParams);

        //如果没有menu的话,那么就给title设置成真正的中间
        if (!hasMenu) {
            textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    textView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int left = textView.getLeft();
                    int width = toolbar.getWidth();
                    ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
                    layoutParams.width = width - left * 2;
                    layoutParams.height = Toolbar.LayoutParams.MATCH_PARENT;
                    textView.setLayoutParams(layoutParams);
                }
            });
        }


        return this;
    }

    @Nullable
    private AppCompatTextView getTitleView() {
        int childCount = toolbar.getChildCount();
        AppCompatTextView textView = null;
        for (int i = 0; i < childCount; i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof AppCompatTextView) {
                textView = (AppCompatTextView) view;
                break;
            }
        }
        return textView;
    }

}

package com.xinyi_tech.comm.widget.seacher;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.xinyi_tech.comm.R;
import com.xinyi_tech.comm.help.recycleview.RecyclerViewHelper;
import com.xinyi_tech.comm.util.ResUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by zhiren.zhang on 2018/1/10.
 */

public class SuperSearchView extends FrameLayout {
    private AppCompatEditText mEt_seacher;
    private ImageView mImg_clear;
    private TextView mTv_action;
    private RecyclerView mSuggestionsListView;
    private View fl_suggest;//建议
    private View search_layout;//顶层布局
    private View search_top_bar;//整个搜索
    private View ll_seacher_edit;//搜索输入


    private boolean defaultOpen = true;//默认打开
    private boolean defaultRequestFocues = true;//默认获取焦点
    private boolean supportCancel = false;

    private boolean isQueryForFirstTextChange = false;//第一次是否执行查询
    private SuperSearchAdapter mAdapter;

    public SuperSearchView(@NonNull Context context) {
        this(context, null);
    }

    public SuperSearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.comm_view_seacher, this, true);
        search_layout = ButterKnife.findById(this, R.id.search_layout);
        search_top_bar = ButterKnife.findById(this, R.id.search_top_bar);
        ll_seacher_edit = ButterKnife.findById(this, R.id.ll_seacher_edit);
        mEt_seacher = ButterKnife.findById(this, R.id.et_seacher);
        mImg_clear = ButterKnife.findById(this, R.id.img_clear);
        mTv_action = ButterKnife.findById(this, R.id.tv_action);

        fl_suggest = ButterKnife.findById(this, R.id.fl_suggest);

        mSuggestionsListView = ButterKnife.findById(this, R.id.recyclerView);
    }

    public void init(AttributeSet attrs) {
        initView();
        initListener();
        initParamsData(attrs);
        initSuggestion();
        initSeacherView();
    }

    private void initSeacherView() {
        if (defaultOpen) {
            showSeacherView(false);
        } else {
            closeSearch();
        }
    }

    private void initSuggestion() {
        if (mAdapter == null) {
            mAdapter = new SuperSearchAdapter();
        }

        RecyclerViewHelper.initRecyclerViewV(mSuggestionsListView, true, mAdapter);
    }

    private void initParamsData(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SuperSearchView);
            if (ta.hasValue(R.styleable.SuperSearchView_searchViewBackground)) {
                search_layout.setBackground(ta.getDrawable(R.styleable.SuperSearchView_searchViewBackground));
            }
            if (ta.hasValue(R.styleable.SuperSearchView_searchTopbarBackground)) {
                search_top_bar.setBackground(ta.getDrawable(R.styleable.SuperSearchView_searchTopbarBackground));
            }
            if (ta.hasValue(R.styleable.SuperSearchView_searchEditViewBackground)) {
                ll_seacher_edit.setBackground(ta.getDrawable(R.styleable.SuperSearchView_searchEditViewBackground));
            }
            if (ta.hasValue(R.styleable.SuperSearchView_searchActionViewBackground)) {
                mTv_action.setBackground(ta.getDrawable(R.styleable.SuperSearchView_searchActionViewBackground));
            }
            if (ta.hasValue(R.styleable.SuperSearchView_android_hint)) {
                mEt_seacher.setHint(ta.getString(R.styleable.SuperSearchView_android_hint));
            }
            if (ta.hasValue(R.styleable.SuperSearchView_android_textColor)) {
                mEt_seacher.setTextColor(ta.getColor(R.styleable.SuperSearchView_android_textColor, 0));
            }

            if (ta.hasValue(R.styleable.SuperSearchView_android_textColorHint)) {
                mEt_seacher.setHintTextColor(ta.getColor(R.styleable.SuperSearchView_android_textColorHint, 0));
            }

            defaultOpen = ta.getBoolean(R.styleable.SuperSearchView_defaultOpen, true);
            defaultRequestFocues = ta.getBoolean(R.styleable.SuperSearchView_defaultRequestFocues, true);
            supportCancel = ta.getBoolean(R.styleable.SuperSearchView_supportCancel, false);
            ta.recycle();
        }
    }

    private void initListener() {
        mEt_seacher.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onSubmitQuery();
                return true;
            }
        });

        RxTextView.textChanges(mEt_seacher)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        onTextChanged(charSequence);
                    }
                });

        RxView.clicks(mTv_action)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if ("搜索".equals(mTv_action.getText().toString())) {
                            onSubmitQuery();
                        } else {
                            closeSearch();
                        }
                    }
                });


        mImg_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEt_seacher.setText(null);
            }
        });

        fl_suggest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissSuggestions();
            }
        });

        mEt_seacher.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    KeyboardUtils.showSoftInput(mEt_seacher);
                }
            }
        });
    }


    public void showSuggestions() {
        if (mAdapter != null && mAdapter.getData().size() > 0 && fl_suggest.getVisibility() == GONE) {
            fl_suggest.setVisibility(VISIBLE);
        }
    }


    public void setOnItemClickListener(BaseQuickAdapter.OnItemClickListener listener) {
        mAdapter.setOnItemClickListener(listener);
    }

    public void setAdapter(SuperSearchAdapter adapter) {
        mAdapter = adapter;
        mSuggestionsListView.setAdapter(adapter);
    }

    public void setSupportSuggestData(List<SuperSeacherModel> data) {
        mAdapter.setNewData(data);
        showSuggestions();
    }

    private void onSubmitQuery() {
        CharSequence query = mEt_seacher.getText();
        // if (query != null && TextUtils.getTrimmedLength(query) > 0) {
        if (mOnQueryChangeListener == null || !mOnQueryChangeListener.onQueryTextSubmit(query.toString())) {
            KeyboardUtils.hideSoftInput(mEt_seacher);
        }
        //  }
    }

    public String getText() {
        return mEt_seacher.getText().toString();
    }

    public void clearText() {
        mEt_seacher.setText(null);
    }

    public void setSearchHint(String searchHint) {
        mEt_seacher.setHint(searchHint);
    }

    public void setQueryForFirstTextChange(boolean queryForFirstTextChange) {
        isQueryForFirstTextChange = queryForFirstTextChange;
    }

    public void setQuery(CharSequence query, boolean submit) {
        mEt_seacher.setText(query);
        if (query != null) {
            mEt_seacher.setSelection(mEt_seacher.length());
        }
        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    private void onTextChanged(CharSequence newText) {
        CharSequence text = mEt_seacher.getText();
        boolean hasText = !TextUtils.isEmpty(text);
        if (hasText) {
            mImg_clear.setVisibility(VISIBLE);
            mTv_action.setText("搜索");
        } else {
            mImg_clear.setVisibility(GONE);
            if ((supportCancel && defaultOpen) || !defaultOpen)
                mTv_action.setText("取消");
        }

        if (mOnQueryChangeListener != null) {
            if (isQueryForFirstTextChange) {
                mOnQueryChangeListener.onQueryTextChange(newText.toString());
            } else {
                isQueryForFirstTextChange = true;
            }

        }
    }

    public void withToolBarMenu(MenuItem item, boolean isSupportBack) {
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showSeacherView(true);
                return true;
            }
        });

        //设置高度和是否支持返回
        TypedValue tv = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getContext().getResources().getDisplayMetrics());
        final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) search_top_bar.getLayoutParams();
        layoutParams.height = actionBarHeight;
        if (isSupportBack) {
            layoutParams.leftMargin = SizeUtils.dp2px(60);
        }

        search_top_bar.setLayoutParams(layoutParams);

        search_layout.setBackgroundResource(android.R.color.transparent);
        search_top_bar.setBackgroundResource(R.color.colorPrimaryDark);
        mTv_action.setTextColor(ResUtils.getColor(R.color.comm_white));
    }

    public TextView getActionTextView() {
        return mTv_action;
    }

    public void closeSearch() {
        if (search_layout.getVisibility() != VISIBLE) {
            return;
        }
        mEt_seacher.setText(null);
        dismissSuggestions();
        mEt_seacher.clearFocus();
        KeyboardUtils.hideSoftInput(mEt_seacher);
        search_layout.setVisibility(GONE);
        if (mSearchViewListener != null) {
            mSearchViewListener.onSearchViewClosed();
        }
    }

    public void dismissSuggestions() {
        if (fl_suggest.getVisibility() == VISIBLE) {
            fl_suggest.setVisibility(GONE);
        }
    }


    public void showSeacherView(boolean animate) {
        if (search_layout.getVisibility() == VISIBLE) {
            return;
        }
        mEt_seacher.setText(null);
        if (defaultRequestFocues) {
            mEt_seacher.requestFocus();
        }
        if (animate) {
            setVisibleWithAnimation();
        } else {
            search_layout.setVisibility(VISIBLE);
            if (mSearchViewListener != null) {
                mSearchViewListener.onSearchViewShown();
            }
        }
    }

    private void setVisibleWithAnimation() {
        AnimationUtil.AnimationListener animationListener = new AnimationUtil.AnimationListener() {
            @Override
            public boolean onAnimationStart(View view) {
                return false;
            }

            @Override
            public boolean onAnimationEnd(View view) {
                if (mSearchViewListener != null) {
                    mSearchViewListener.onSearchViewShown();
                }
                return false;
            }

            @Override
            public boolean onAnimationCancel(View view) {
                return false;
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            search_layout.setVisibility(View.VISIBLE);
            AnimationUtil.reveal(search_top_bar, animationListener);
        } else {
            AnimationUtil.fadeInView(search_layout, 0, animationListener);
        }
    }

    private SearchViewListener mSearchViewListener;
    private OnQueryTextListener mOnQueryChangeListener;


    public void setSearchViewListener(SearchViewListener searchViewListener) {
        mSearchViewListener = searchViewListener;
    }

    public void setOnQueryChangeListener(OnQueryTextListener onQueryChangeListener) {
        mOnQueryChangeListener = onQueryChangeListener;
    }

    public interface OnQueryTextListener {
        boolean onQueryTextSubmit(String query);

        boolean onQueryTextChange(String newText);
    }

    public interface SearchViewListener {
        void onSearchViewShown();

        void onSearchViewClosed();
    }


    public void setDefaultOpen(boolean defaultOpen) {
        this.defaultOpen = defaultOpen;
    }

    public void setDefaultRequestFocues(boolean defaultRequestFocues) {
        this.defaultRequestFocues = defaultRequestFocues;
    }
}

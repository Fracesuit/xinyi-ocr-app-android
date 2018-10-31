package com.xinyi_tech.comm.picker.file;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.allen.library.SuperButton;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.StringUtils;
import com.xinyi_tech.comm.R;
import com.xinyi_tech.comm.base.BaseListFragment;
import com.xinyi_tech.comm.base.ListSetupModel;
import com.xinyi_tech.comm.util.FileUtils2;
import com.xinyi_tech.comm.util.ToolbarUtils;
import com.xinyi_tech.comm.widget.arrow.ArrowModel;
import com.xinyi_tech.comm.widget.arrow.ArrowView;
import com.xinyi_tech.comm.widget.seacher.SuperSearchView;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by zhiren.zhang on 2018/3/27.
 */

public class FileSelectFragment extends BaseListFragment<FileSelectAdapter, FileModel, FilePresenter> {
    SuperSearchView search_view;
    ArrowView arrowView;
    private SuperButton superButton;
    private FileSelectBuild fileSelectBuild;
    public static final int RESULT_FILE_SELECT = 144;
    Toolbar toolbar;

    @Override
    protected void onCreateViewAfter(View view, Bundle savedInstanceState) {
        fileSelectBuild = (FileSelectBuild) activity.getIntent().getSerializableExtra("fileSelectBuilder");
        super.onCreateViewAfter(view, savedInstanceState);
        arrowView = ButterKnife.findById(view, R.id.arrowView);
        superButton = ButterKnife.findById(view, R.id.btn_ok);
        search_view = ButterKnife.findById(view, R.id.search_view);
        toolbar = ButterKnife.findById(view, R.id.tool_bar);
        arrowView.setFirstArrowModel(new ArrowModel(fileSelectBuild.getRootPath(), fileSelectBuild.getRootPath()));
        arrowView.setOnArrowItemClickListener(new ArrowView.OnArrowItemClickListener() {
            @Override
            public void onArrowClick(ArrowModel model) {
                requestListData(REQUEST_STATE_REFRESH);
            }
        });
        superButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<FileModel> selectedList = baseListAdapter.getSelectedList();
                final Intent intent = new Intent();
                intent.putParcelableArrayListExtra("selectedList", selectedList);
                activity.setResult(RESULT_FILE_SELECT, intent);
                ActivityUtils.finishActivity(activity);
            }
        });

        initToolbar();
    }


    private void initToolbar() {
        ToolbarUtils.with(activity, toolbar)
                .setSupportBack(true)
                .setInflateMenu(R.menu.comm_menu_file_seacher)
                .build();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final int itemId = item.getItemId();
                if (itemId == R.id.action_clear) {
                    baseListAdapter.clearAllSelect();
                }
                return true;
            }
        });
        if (fileSelectBuild.getmFileTypes() != null) {
            search_view.setSearchHint("查找" + Arrays.toString(fileSelectBuild.getmFileTypes()).replace("[", "").replace("]", ""));
        }
        search_view.withToolBarMenu(toolbar.getMenu().findItem(R.id.action_search), true);
        search_view.setQueryForFirstTextChange(false);
        search_view.setOnQueryChangeListener(new SuperSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPresenter.getFileByDir(arrowView.getLastModel().getValue(),
                        new LFileFilter(fileSelectBuild.getmFileTypes(), search_view.getText(), false),
                        true, REQUEST_STATE_REFRESH);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                requestListData(REQUEST_STATE_REFRESH);
                return true;
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.comm_activity_file_select;
    }


    @Override
    protected FilePresenter getPresenter() {
        return new FilePresenter();
    }

    @Override
    protected FileSelectAdapter getAdapter() {
        final FileSelectAdapter fileSelectAdapter = new FileSelectAdapter();
        fileSelectAdapter.registerAdapterDataObserver(observer);
        fileSelectAdapter.setMaxSelectNum(fileSelectBuild.getMaxSelectCount());
        return fileSelectAdapter;
    }

    @Override
    protected ListSetupModel setupParam() {
        return ListSetupModel.newBuilder().isEnableLoadMore(false).build();
    }

    @Override
    protected void requestData(int requestCode, int pageIndex, int pageSize) {
        mPresenter.getFileByDir(arrowView.getLastModel().getValue(), new LFileFilter(fileSelectBuild.getmFileTypes(), search_view.getText(), true),
                fileSelectBuild.isRecursive(), requestCode);

    }

    @Override
    protected void onItemClick(FileModel item, int position) {
        if (item.isDir()) {
            arrowView.addLastData(new ArrowModel(item.getFileName(), item.getFilePath()), false);
            search_view.clearText();
        }
    }


    class LFileFilter implements FileFilter {
        private String[] mTypes;
        private CharSequence keyword;
        private boolean includeDir;

        public LFileFilter(String[] types, CharSequence keyword, boolean includeDir) {
            this.mTypes = types;
            this.keyword = keyword;
            this.includeDir = includeDir;
        }

        @Override
        public boolean accept(File file) {
            if (!StringUtils.isEmpty(keyword)) {
                if (file.isDirectory() && includeDir) {
                    final String lastDirName = FileUtils2.getLastDirName(file.getAbsolutePath());
                    if (!lastDirName.contains(keyword.toString())) {
                        return false;
                    }
                } else {
                    if (!FileUtils.getFileName(file).contains(keyword.toString())) {
                        return false;
                    }
                }
            }

            if (file.isDirectory() && includeDir) {
                return true;
            }

            if (mTypes != null && mTypes.length > 0) {
                for (int i = 0; i < mTypes.length; i++) {
                    final String fileExtension = FileUtils.getFileExtension(file);
                    if (fileExtension.contains(mTypes[i].toLowerCase()) || fileExtension.contains(mTypes[i].toUpperCase())) {
                        return true;
                    }
                }
            } else {
                return true;
            }
            return false;
        }
    }

    RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            final List<FileModel> selectedList = baseListAdapter.getSelectedList();
            superButton.setText("选中(" + selectedList.size() + ")");
        }

        @Override
        public void onChanged() {
            final List<FileModel> selectedList = baseListAdapter.getSelectedList();
            superButton.setText("选中(" + selectedList.size() + ")");
        }
    };


    public boolean onBackPressed() {
        if (mPresenter.cancel(REQUEST_STATE_REFRESH) || arrowView.onBackPressed()) {
            return true;
        }
        baseListAdapter.unregisterAdapterDataObserver(observer);
        return false;
    }
}

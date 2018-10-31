package com.xinyi_tech.freedom.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by zhiren.zhang on 2018/5/14.
 */

public class MyFragment extends Fragment {

    /**
     * Fragment在viewpager中切换的时候被调用
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * 在使用show  和hide  方法的时候被调用
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        FragmentManager childFragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = childFragmentManager.beginTransaction();
       // fragmentTransaction.add
       // boolean b = childFragmentManager.executePendingTransactions();
    }
}

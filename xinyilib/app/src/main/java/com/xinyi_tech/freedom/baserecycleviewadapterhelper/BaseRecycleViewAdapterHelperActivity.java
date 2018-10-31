package com.xinyi_tech.freedom.baserecycleviewadapterhelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.xinyi_tech.comm.help.recycleview.RecyclerViewHelper;
import com.xinyi_tech.freedom.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BaseRecycleViewAdapterHelperActivity extends AppCompatActivity {

    List<String> list = new ArrayList<>();
    @BindView(R.id.rv)
    RecyclerView rv;
    HelperAdapter2 helperAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_recycle_view_adapter_helper);
        ButterKnife.bind(this);

         helperAdapter = new HelperAdapter2();
        list.add("old1");
        list.add("old2");
        list.add("old3");
        list.add("old4");
        list.add("old5");
        list.add("old6");
        helperAdapter.setData(list);
        RecyclerViewHelper.initRecyclerViewV(rv,false,helperAdapter);
    }

    @OnClick(R.id.btn_change)
    public void onViewClicked() {
        List<String> newList = new ArrayList<>();
        newList.add("new1");
        newList.add("new2");
        newList.add("new3");
        newList.add("new4");
        //List<String> data = helperAdapter.getData();
        //data=newList;
      //list=newList;
       // helperAdapter.notifyDataSetChanged();
       helperAdapter.setData(newList);
    }
}

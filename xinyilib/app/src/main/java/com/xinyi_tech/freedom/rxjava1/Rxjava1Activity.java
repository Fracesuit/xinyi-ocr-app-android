package com.xinyi_tech.freedom.rxjava1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xinyi_tech.comm.log.XinYiLog;
import com.xinyi_tech.freedom.R;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class Rxjava1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava1);

        Observable.just(0, 1, 2)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 3;
                    }
                })
                .toList()
                .subscribe(new Subscriber<List<Integer>>() {
                    @Override
                    public void onCompleted() {
                        XinYiLog.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        XinYiLog.e("onCompleted");
                        System.out.print("onError");
                    }

                    @Override
                    public void onNext(List<Integer> integers) {
                        XinYiLog.e(integers.toString());
                    }
                });

    }
}

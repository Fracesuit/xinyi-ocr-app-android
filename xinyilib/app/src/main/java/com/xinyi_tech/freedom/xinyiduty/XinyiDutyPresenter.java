package com.xinyi_tech.freedom.xinyiduty;

import com.blankj.utilcode.util.ActivityUtils;
import com.xinyi_tech.comm.base.BasePresenter;

import java.util.List;
import java.util.Map;

import retrofit2.HttpException;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zhiren.zhang on 2018/5/4.
 */

public class XinyiDutyPresenter extends BasePresenter {
    XinyiDutyRepository mXinyiDutyRepository = new XinyiDutyRepository();

    public void login(Map<String, Object> params) {
        execute(mXinyiDutyRepository.login(params), 1);
    }

    public void dutyList(int requestCode, int pageIndex, int pageSize) {

        execute(dealError(mXinyiDutyRepository.dutyList(pageIndex, pageSize)), requestCode);
    }

    public void saveDaily(int requestCode, List<DutyModel> dutyModels) {
        execute(dealError(mXinyiDutyRepository.saveDaily(dutyModels)), requestCode);
    }

    public void saveDaily(int requestCode, DutyModel dutyModel) {
        dutyModel.setCosttime("7.5");
        dutyModel.setDetail("界面功能优化");
        execute(dealError(mXinyiDutyRepository.saveDaily(dutyModel)), requestCode);
    }

    private Observable<?> dealError(Observable<?> observable) {
        return observable.retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Throwable> throwableObservable){

                return throwableObservable.flatMap(new Func1<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> call(Throwable throwable){
                        final boolean httpException = throwable instanceof HttpException && ((HttpException) throwable).response().code() == 302;
                        if (httpException) {
                            ActivityUtils.finishActivity(XinyiDutyListActivity.class);
                            ActivityUtils.startActivity(XinyiDutyActivity.class);
                        }
                        return Observable.error(throwable);//不会在执行
                    }
                });
            }
        });
    }

}

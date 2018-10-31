package com.xinyi_tech.freedom.xinyiduty;


import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.TimeUtils;
import com.xinyi_tech.comm.constant.CommConstant;
import com.xinyi_tech.freedom.app.FreedomApplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zhiren.zhang on 2018/5/4.
 */

public class XinyiDutyRepository {
    DateFormat DEFAULT_FORMAT_T = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

    public Observable<String> login(Map<String, Object> params) {
        return FreedomApplication.apiService.login(params);
    }

    public Observable<String> saveDaily(DutyModel dutyModel) {
        List<DutyModel> dutyModels = new ArrayList<>();
        dutyModels.add(dutyModel);

        final HashMap<String, Object> params = new HashMap<>();
        params.put("dailyDate", "\"" + dutyModel.getDutyTime() + "T00:00:00\"");//T00:00:00
        params.put("data", JSON.toJSONString(dutyModels));
        return FreedomApplication.apiService.saveDaily(params);
    }


    public Observable<List<String>> saveDaily(List<DutyModel> dutyModels) {
        return Observable.from(dutyModels)
                .flatMap(new Func1<DutyModel, Observable<String>>() {
                    @Override
                    public Observable<String> call(DutyModel dutyModel) {
                        return saveDaily(dutyModel);
                    }
                })
                .toList();


    }

    public Observable<List<DutyModel>> dutyList(int pageIndex, int pageSize) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        int weekIndex = TimeUtils.getWeekIndex(new Date()) - 1;
        if (weekIndex == 0) weekIndex = 7;
        final int finalWeekIndex = weekIndex;
        return Observable.range(1, weekIndex)
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer num) {
                        return TimeUtils.getString(new Date(), CommConstant.DeteFromat.DEFAULT_FORMAT_DAY, num - finalWeekIndex, TimeConstants.DAY);
                    }
                })
                .flatMap(new Func1<String, Observable<DutyModel>>() {
                    @Override
                    public Observable<DutyModel> call(final String time) {
                        params.put("dailydate", "\"" + time + "T00:00:00\"");//T00:00:00
                        return FreedomApplication.apiService.dutyList(params)
                              .map(new Func1<List<DutyModel>, DutyModel>() {
                                  @Override
                                  public DutyModel call(List<DutyModel> dutyModels){
                                      DutyModel dutyModel = dutyModels.get(0);
                                      dutyModel.setDutyTime(time);
                                      dutyModel.setWeekChinese(getWeekChinese(dutyModel));
                                      return dutyModel;
                                  }
                              });
                    }
                })
                .toList();

    }

    private String getWeekChinese(DutyModel item) {
        int weekIndex = TimeUtils.getWeekIndex(item.getDutyTime(), CommConstant.DeteFromat.DEFAULT_FORMAT_DAY) - 1;
        switch (weekIndex) {
            case 0:
                item.setNeedInput(false);
                return "星期日";
            case 1:
                item.setNeedInput(true);
                return "星期一";
            case 2:
                item.setNeedInput(true);
                return "星期二";
            case 3:
                item.setNeedInput(true);
                return "星期三";
            case 4:
                item.setNeedInput(true);
                return "星期四";
            case 5:
                item.setNeedInput(true);
                return "星期五";
            case 6:
                item.setNeedInput(false);
                return "星期六";
        }
        return null;
    }
}

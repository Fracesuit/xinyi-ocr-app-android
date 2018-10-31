package com.xinyi_tech.freedom.app;


import com.xinyi_tech.freedom.xinyiduty.DutyModel;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zhiren.zhang on 2017/9/26.
 */

public interface Api {

    @FormUrlEncoded
    @POST("a/login")
    Observable<String> login(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("a/project/daily_myDaily")
    Observable<List<DutyModel>> dutyList(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("a/project/saveDaily")
    Observable<String> saveDaily(@FieldMap Map<String, Object> params);

}

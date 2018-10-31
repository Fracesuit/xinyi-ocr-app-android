package com.xinyi_tech.freedom.xinyiduty;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by zhiren.zhang on 2018/5/4.
 */

public class DutyModel {
    /**
     * uid : 20171113112016261102374
     * postusername : 王晖
     * finish : 2018-05-31
     * business_leader : kelvin
     * taskStatus : 进行中
     * status : 1
     * postuser : wanghui
     * projectname : 龙岗视频门禁项目
     * project_leader : wanghui
     * taskname : 原生android app开发
     * work_actual : 778.5
     * _id : 1
     * _uid : 1
     * detail : 界面优化
     * _state : modified
     * costtime : 5
     */

    private String uid;
    private String postusername;
    private String finish;
    private String business_leader;
    private String taskStatus;
    private int status;
    private String postuser;
    private String projectname;
    private String project_leader;
    private String taskname;
    private double work_actual;
    private String detail;
    private String costtime;
    private String dutyTime;
    private String weekChinese;

    @JSONField(serialize = false)
    private boolean isNeedInput;//是否需要填写


    public String getWeekChinese() {
        return weekChinese;
    }

    public void setWeekChinese(String weekChinese) {
        this.weekChinese = weekChinese;
    }

    public String getDutyTime() {
        return dutyTime;
    }

    public void setDutyTime(String dutyTime) {
        this.dutyTime = dutyTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPostusername() {
        return postusername;
    }

    public void setPostusername(String postusername) {
        this.postusername = postusername;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getBusiness_leader() {
        return business_leader;
    }

    public void setBusiness_leader(String business_leader) {
        this.business_leader = business_leader;
    }

    public boolean isNeedInput() {
        return isNeedInput;
    }

    public void setNeedInput(boolean needInput) {
        isNeedInput = needInput;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPostuser() {
        return postuser;
    }

    public void setPostuser(String postuser) {
        this.postuser = postuser;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getProject_leader() {
        return project_leader;
    }

    public void setProject_leader(String project_leader) {
        this.project_leader = project_leader;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public double getWork_actual() {
        return work_actual;
    }

    public void setWork_actual(double work_actual) {
        this.work_actual = work_actual;
    }


    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }


    public String getCosttime() {
        return costtime;
    }

    public void setCosttime(String costtime) {
        this.costtime = costtime;
    }
}

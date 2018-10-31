package com.xinyi_tech.freedom.popup;

/**
 * Created by Fracesuit on 2017/12/15.
 */

public class HourseModel {
    private String hourseName;
    private String hoursePersonNum;
    private String hourseId;
    private boolean select = false;
    private BuildingModel buildingModel;


    public HourseModel(String hourseName, String hoursePersonNum, String houseId) {
        this.hourseName = hourseName;
        this.hoursePersonNum = hoursePersonNum;
        this.hourseId = houseId;
    }

    public HourseModel setSelect(boolean select) {
        this.select = select;
        return this;
    }

    public boolean isSelect() {
        return select;
    }

    public BuildingModel getBuildingModel() {
        return buildingModel;
    }

    public void setBuildingModel(BuildingModel buildingModel) {
        this.buildingModel = buildingModel;
    }

    public String getHourseName() {
        return hourseName;
    }

    public void setHourseName(String hourseName) {
        this.hourseName = hourseName;
    }

    public String getHoursePersonNum() {
        return hoursePersonNum;
    }

    public void setHoursePersonNum(String hoursePersonNum) {
        this.hoursePersonNum = hoursePersonNum;
    }

    public String getHourseId() {
        return hourseId;
    }

    public void setHourseId(String hourseId) {
        this.hourseId = hourseId;
    }
}

package com.xinyi_tech.freedom.popup;


import com.xinyi_tech.comm.util.StringUtils2;

public class BuildingModel {
    private String estateId;
    private String estateName;
    private String buildingId;
    private String buildingName;
    private String unitId;
    private String unitName;
    private String buildingAddr;

    private boolean select = false;//是否选中


    public String getBuildingAddr() {
        return buildingAddr;
    }

    public void setBuildingAddr(String buildingAddr) {
        this.buildingAddr = buildingAddr;
    }


    public boolean isSelect() {
        return select;
    }

    public BuildingModel setSelect(boolean select) {
        this.select = select;
        return this;
    }

    public String getEstateId() {
        return estateId;
    }

    public void setEstateId(String estateId) {
        this.estateId = estateId;
    }

    public String getEstateName() {
        return estateName;
    }

    public void setEstateName(String estateName) {
        this.estateName = estateName;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public BuildingModel setBuildingName(String buildingName) {
        this.buildingName = buildingName;
        return this;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getBuildingInfoName() {
        String info = "";
        if (!StringUtils2.isEmpty(buildingAddr)) {
            info += buildingAddr + "-";
        }
        if (!StringUtils2.isEmpty(buildingName)) {
            info += buildingName + "-";
        }
      /*  if (!StringUtils2.isEmpty(unitName)) {
            info += unitName + "-";
        }*/
        final int length = info.length();
        return info.substring(0, length - 1);

    }

}

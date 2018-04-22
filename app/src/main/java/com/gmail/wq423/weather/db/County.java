package com.gmail.wq423.weather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 13521838583@163.com on 2018-4-22.
 */

public class County extends DataSupport{

    private int id;
    private String countyName;  //县城名称
    private String weatherId;   //要查询县对应的天气id
    private int cityId;         //当前县所属的市id


    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

}

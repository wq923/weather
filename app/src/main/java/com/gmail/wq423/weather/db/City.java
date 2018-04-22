package com.gmail.wq423.weather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 13521838583@163.com on 2018-4-22.
 */

public class City extends DataSupport{

    private int id;
    private String cityName;    //城市名
    private int cityCode;       //城市代码
    private int provinceId;     //省份ID

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }
}

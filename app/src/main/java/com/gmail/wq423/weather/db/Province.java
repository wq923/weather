package com.gmail.wq423.weather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 13521838583@163.com on 2018-4-22.
 */

public class Province extends DataSupport{

    private int id;                 //实体类都应该有
    private String provinceName;    //省份名称
    private int provinceCode;    //省份代号


    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

}

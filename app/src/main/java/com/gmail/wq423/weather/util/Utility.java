package com.gmail.wq423.weather.util;

import android.text.TextUtils;

import com.gmail.wq423.weather.db.City;
import com.gmail.wq423.weather.db.County;
import com.gmail.wq423.weather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 13521838583@163.com on 2018-4-22.
 */

public class Utility {

    /**
     * describe：解析和处理省份的数据
     * paramter：请求省份响应的字符串
     * return：是否正确处理
     */
    public static boolean handleProvinceResponse(String response){

        if (!TextUtils.isEmpty(response)){

            try {
                JSONArray allProvince = new JSONArray(response);
                for (int i = 0; i < allProvince.length(); i++){
                    JSONObject provinceObj = allProvince.getJSONObject(i);

                    Province province = new Province();
                    province.setProvinceName(provinceObj.getString("name"));
                    province.setProvinceCode(provinceObj.getInt("id"));
                    province.save();
                }

                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * describe：解析并处理服务器返回的市级数据
     * paramter：市级数据的响应数据，省份Id
     * return：解析处理是否正确
     */
    public static boolean handleCityResponse(String response, int provinceId){

        if (!TextUtils.isEmpty(response)){

            try {
                JSONArray allCities = new JSONArray(response);

                for (int i = 0; i < allCities.length(); i++){
                    JSONObject cityObj = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObj.getString("name"));
                    city.setCityCode(cityObj.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }

                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static boolean handleCountyResponse(String response, int cityId){

        if (!TextUtils.isEmpty(response)){

            try {
                JSONArray allCounty = new JSONArray(response);

                for (int i = 0; i < allCounty.length(); i++){
                    JSONObject countyObj = allCounty.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObj.getString("name"));
                    county.setWeatherId(countyObj.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }

                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }


}

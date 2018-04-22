package com.gmail.wq423.weather;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.wq423.weather.db.City;
import com.gmail.wq423.weather.db.County;
import com.gmail.wq423.weather.db.Province;
import com.gmail.wq423.weather.util.HttpUtil;
import com.gmail.wq423.weather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 13521838583@163.com on 2018-4-22.
 */

public class ChooseAreaFragment extends Fragment {

    private static final String TAG = "ChooseAreaFragment";

    //控件
    private TextView mTitleView;
    private Button mBackBtn;
    private ListView mListView;

    private ProgressDialog mProgressDialog;



    private ArrayAdapter<String> mAdapter;
    private List<String> mDataList = new ArrayList<>();


    private List<Province> mProvinceList;
    private List<City> mCityList;
    private List<County> mCountyList;

    private City mSelectedCity;         //当前选中的城市
    private Province mSelectedProvince; //当前选中的省份
    private int mCurrentLevel;          //当前选中的级别
    //表示3种状态，到底当前页面展示的是哪种数据
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.e(TAG, "onCreateView: ");

        //初始化视图资源
        View v = inflater.inflate(R.layout.choose_areas, container, false);
        mTitleView = (TextView) v.findViewById(R.id.id_title_text);
        mBackBtn = (Button) v.findViewById(R.id.id_back_btn);
        mListView = (ListView) v.findViewById(R.id.id_list_view);
        mAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                mDataList);
        mListView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");

        //初始化监听事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mCurrentLevel == LEVEL_PROVINCE){
                    mSelectedProvince = mProvinceList.get(position);
                    queryCities();
                }else if (mCurrentLevel == LEVEL_CITY){
                    mSelectedCity = mCityList.get(position);
                    queryCounties();
                }
            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (mCurrentLevel == LEVEL_COUNTY){
                    queryCities();
                }else if (mCurrentLevel == LEVEL_CITY){
                    queryProvinces();
                }
            }
        });

        //初始化视图内容
        queryProvinces();
    }

    /**
     * describe：查询全国所有的省，先从数据库中查，没查到则通过网络向服务器上查
     * paramter：无
     * return：无
     */
    private void queryProvinces() {
        mTitleView.setText("中国");
        mBackBtn.setVisibility(View.GONE);
        mProvinceList = DataSupport.findAll(Province.class);

        if (mProvinceList.size() > 0){
            mDataList.clear();
            for (Province province:mProvinceList){
                mDataList.add(province.getProvinceName());
            }

            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mCurrentLevel = LEVEL_PROVINCE;
        }else{
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    private void queryFromServer(String address, final String type) {

        //访问网络，给用户等待提示
        showProgressDialog();

        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;

                if ("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);
                }else if ("city".equals(type)){
                    result = Utility.handleCityResponse(responseText,
                            mSelectedProvince.getId());
                }else if ("county".equals(type)){
                    result = Utility.handleCountyResponse(responseText,
                            mSelectedCity.getId());
                }

                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    private void closeProgressDialog() {
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null){
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("正在加载...");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }

        mProgressDialog.show();
    }


    /**
     * describe：查询全国所有的县，先从数据库中查，没查到则通过网络向服务器上查
     * paramter：无
     * return：无
     */
    private void queryCounties() {
        mTitleView.setText(mSelectedCity.getCityName());
        mBackBtn.setVisibility(View.VISIBLE);
        mCountyList = DataSupport.where("cityid = ?", String.valueOf(mSelectedCity.getId())).find(County.class);

        if (mCountyList.size() > 0){
            mDataList.clear();
            for (County c:mCountyList){
                mDataList.add(c.getCountyName());
            }

            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mCurrentLevel = LEVEL_COUNTY;
        }else{
            int provinceCode = mSelectedProvince.getProvinceCode();
            int cityCode = mSelectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "county");
        }
    }

    /**
     * describe：查询全国所有的城市，先从数据库中查，没查到则通过网络向服务器上查
     * paramter：无
     * return：无
     */
    private void queryCities() {
        mTitleView.setText(mSelectedProvince.getProvinceName());
        mBackBtn.setVisibility(View.VISIBLE);
        mCityList = DataSupport.where("provinceid = ?", String.valueOf(mSelectedProvince.getId())).find(City.class);

        if (mCityList.size() > 0){
            mDataList.clear();
            for (City city:mCityList){
                mDataList.add(city.getCityName());
            }

            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mCurrentLevel = LEVEL_CITY;
        }else{
            int provinceCode = mSelectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }


}

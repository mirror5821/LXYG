package com.lxyg.app.customer.utils;

import android.content.Context;
import android.util.Log;

import com.lxyg.app.customer.bean.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.utils.JsonUtils;

/**
 * Created by 王沛栋 on 2015/11/25.
 */
public class AreaUtil {

    private static List<City> mCityData;
    public static List<City> getCityList(Context context){
        if (mCityData == null){
            mCityData = new ArrayList<>();

            try {
                InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open("city.json"), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                inputStreamReader.close();

                mCityData = JsonUtils.parseList(stringBuilder.toString(),City.class);

//                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
//                Log.i("TESTJSON", "cat=" + jsonObject.getString("cat"));
//                JSONArray jsonArray = jsonObject.getJSONArray("language");
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject object = jsonArray.getJSONObject(i);
//                    Log.i("TESTJSON", "----------------");
//                    Log.i("TESTJSON", "id=" + object.getInt("id"));
//                    Log.i("TESTJSON", "name=" + object.getString("name"));
//                    Log.i("TESTJSON", "ide=" + object.getString("ide"));
//                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  mCityData;
    }
}

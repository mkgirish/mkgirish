package com.example.backbasecodetext;

import android.content.Context;

import com.example.backbasecodetext.Model.City;
import com.example.backbasecodetext.Util.LoadData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class UnitLoadData {

    private static UnitLoadData single_instance = null;

    // private constructor restricted to this class itself
    private UnitLoadData()
    { }

    // static method to create instance of Singleton class
    public static UnitLoadData getInstance()
    {
        if (single_instance == null)
            single_instance = new UnitLoadData();

        return single_instance;
    }

    public List<City> loadCitiesJsonFile(){

        String data = getAssetJsonData();

        List<City> cities = new Gson().fromJson(data, new TypeToken<List<City>>(){}.getType());
        Collections.sort(cities,City.COMPARE_BY_CITY );
        return cities;

    }


    public String getAssetJsonData() {
        String json = null;

        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("cities.json");;
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;

    }

    public String getAssetAboutInfoJsonData() {
        String json = null;

        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("aboutInfo.json");;
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;

    }
}

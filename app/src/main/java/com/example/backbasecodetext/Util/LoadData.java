package com.example.backbasecodetext.Util;

import android.content.Context;

import com.example.backbasecodetext.Model.City;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class LoadData {


    private static LoadData single_instance = null;

    // private constructor restricted to this class itself
    private LoadData()
    { }

    // static method to create instance of Singleton class
    public static LoadData getInstance()
    {
        if (single_instance == null)
            single_instance = new LoadData();

        return single_instance;
    }



    public List<City> loadCitiesJsonFile(Context context){

        String data = getAssetJsonData(context);
        // Reads json string in to java objects
        List<City> cities = new Gson().fromJson(data, new TypeToken<List<City>>(){}.getType());
        // sort the list based on city
        Collections.sort(cities,City.COMPARE_BY_CITY );
        return cities;

    }

    // Method read the assests json file in to string
    public String getAssetJsonData(Context context) {
        String json = null;


        try {
            InputStream is = context.getAssets().open("cities.json");
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

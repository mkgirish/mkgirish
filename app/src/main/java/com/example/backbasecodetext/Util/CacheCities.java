package com.example.backbasecodetext.Util;

import com.example.backbasecodetext.Model.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CacheCities {

    private static CacheCities single_instance = null;
    private List<City> cities;
    private HashMap<String, List<City>> mappedList = new HashMap<String, List<City>>();
    private String firstAlphabet = new String();

    // private constructor restricted to this class itself
    private CacheCities()
    { }

    // static method to create instance of Singleton class
    public static CacheCities getInstance()
    {
        if (single_instance == null)
            single_instance = new CacheCities();

        return single_instance;
    }



    // Method return key value pair for each english alphabet wise of cities list.
    // Key will english alphabet and value will list of cities which match to english alphabet
    public HashMap<String, List<City>> getSortListCities(List<City> cities){
        this.cities = cities;

             List<City> alphabetList = new ArrayList<>();

                for(City city: cities){
                    if(firstAlphabet.isEmpty()) {
                        firstAlphabet = Character.toString(city.getName().toLowerCase().charAt(0));
                    }

                    if(city.getName().toLowerCase().startsWith(firstAlphabet)){
                        alphabetList.add(city);

                    } else{
                        mappedList.put(firstAlphabet,alphabetList );
                        firstAlphabet = Character.toString(city.getName().toLowerCase().charAt(0));
                        if(mappedList.get(firstAlphabet)!= null){
                            alphabetList = mappedList.get(firstAlphabet);
                            alphabetList.add(city);
                        } else {
                            alphabetList = new ArrayList<>();
                            alphabetList.add(city);
                        }
                    }
            }

        return mappedList;
    }

    // Method to return filter list based on user typed character
    public CopyOnWriteArrayList<City> getFilterSearchList(List<City> mappedList, CopyOnWriteArrayList<City> filteredList, String charString ){

        // If user typed character length is 1, it return mapped list of items of first character
        if(charString.length() == 1){
            filteredList.addAll(mappedList);
        } else {

            // Linear search
            for (City city : mappedList) {
                if (city.getName().toLowerCase().startsWith(charString.toLowerCase())) {
                    filteredList.add(city);
                }
            }
        }
        return filteredList;
    }

}

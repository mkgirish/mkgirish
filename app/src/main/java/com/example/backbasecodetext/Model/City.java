package com.example.backbasecodetext.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class City  implements Comparable<City>{

    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("_id")
    @Expose
    private Integer id;
    @SerializedName("coord")
    @Expose
    private Coord coord;
    private CollationKey key;

    private static final Collator collator = Collator.getInstance(Locale.ITALY);

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        //this.key = collator.getCollationKey(this.name);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public static Comparator<City> COMPARE_BY_CITY = new Comparator<City>() {
        public int compare(City one, City other) {
            return one.name.compareTo(other.name);
        }
    };

    public int compareTo(City city) {
        return collator.compare(name, city.name);
        //return key.compareTo(city.key);
    }
}

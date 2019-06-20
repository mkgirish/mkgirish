package com.example.backbasecodetext.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.backbasecodetext.Constants.Consts;
import com.example.backbasecodetext.Model.City;
import com.example.backbasecodetext.R;
import com.example.backbasecodetext.Util.CacheCities;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<City> cityList;
    private CopyOnWriteArrayList<City> cityListFiltered = new CopyOnWriteArrayList();
    private CityAdapterListener listener;
    private HashMap<String, List<City>> mappedList;
    public CopyOnWriteArrayList<City> cacheFilterCityList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, country, lat, lon;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.cityname);
            country = view.findViewById(R.id.country);
            lat = view.findViewById(R.id.lat);
            lon = view.findViewById(R.id.lon);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                     Double latitude = cityListFiltered.get(getAdapterPosition()).getCoord().getLat();
                    Double longtitude = cityListFiltered.get(getAdapterPosition()).getCoord().getLon();
                    listener.onCitySelected(latitude, longtitude );
                }
            });


        }
    }


    public CitiesAdapter(Context context, List<City> cityList, CityAdapterListener listener, HashMap<String, List<City>> mappedList) {
        this.context = context;
        this.listener = listener;
        this.cityList = cityList;
        this.cityListFiltered.addAll(cityList);
        this.mappedList = mappedList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cities_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if(position != cityListFiltered.size()) {

            City city = cityListFiltered.get(position);
             holder.name.setText(city.getName());
            holder.country.setText(city.getCountry());
            holder.lat.setText(Double.toString(city.getCoord().getLat()));
            holder.lon.setText(Double.toString(city.getCoord().getLon()));
            if (position % 2 == 1) {
                // Set a background color for ListView regular row/item
                holder.itemView.setBackgroundColor(Color.parseColor("#FFCCCB4C"));
            } else {
                // Set the background color for alternate row/item
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }

    }

    @Override
    public int getItemCount() {
         return cityListFiltered.size();
    }

    public void setDataList(List<City> dataList){
        this.cityListFiltered.addAll(dataList);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (cacheFilterCityList != null) {
                    cacheFilterCityList.clear();
                }
                cityListFiltered.clear();
                CopyOnWriteArrayList<City> filteredList = new CopyOnWriteArrayList<>() ;


                if (charString.isEmpty()) {

                    filteredList.addAll(cityList);
                } else {
                    String firstchar = Character.toString(charString.charAt(0));

                    List<City> searchedList = mappedList.get(firstchar.toLowerCase());


                    filteredList = CacheCities.getInstance().getFilterSearchList(searchedList, filteredList, charString );

                }
                cacheFilterCityList = filteredList;
                FilterResults filterResults = new FilterResults();
                if (filteredList.size() >=Consts.endPaginationIndex) {
                    filterResults.values = (List<City>) filteredList.subList(Consts.startPaginationIndex, Consts.endPaginationIndex);
                } else{
                    filterResults.values = (List<City>) filteredList.subList(Consts.startPaginationIndex, filteredList.size());
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                  cityListFiltered.addAll((List<City>)filterResults.values);
                  notifyDataSetChanged();
            }
        };
    }

    public List<City> getCacheFilterCityList(){
        return cacheFilterCityList;
    }
    public interface CityAdapterListener {
        void onCitySelected(Double latitude, Double longtitude);

    }
}

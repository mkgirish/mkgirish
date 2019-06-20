package com.example.backbasecodetext.Fragment;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.backbasecodetext.Adapter.CitiesAdapter;
import com.example.backbasecodetext.Constants.Consts;
import com.example.backbasecodetext.MainActivity;
import com.example.backbasecodetext.Model.City;
import com.example.backbasecodetext.R;
import com.example.backbasecodetext.Util.CacheCities;
import com.example.backbasecodetext.Util.ListDividerItemDecoration;
import com.example.backbasecodetext.Util.LoadData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CitiesListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CitiesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CitiesListFragment extends Fragment implements CitiesAdapter.CityAdapterListener{

    private RecyclerView recyclerView;
    private List<City> citiesList;
    private HashMap<String, List<City>> mappedList;
    private CitiesAdapter mAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private OnFragmentInteractionListener mListener;
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    //private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayoutManager mLayoutManager;
    private Parcelable listState;


    public CitiesListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCitySelected(Double latitude, Double longtitude) {
        mListener.onFragmentInteraction(latitude, longtitude);
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CitiesListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CitiesListFragment newInstance(String param1, String param2) {
        CitiesListFragment fragment = new CitiesListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            listState = savedInstanceState.getParcelable(Consts.RECYCLER_LIST_STATE_KEY);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cities_list, container, false);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ListDividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL, 36));
        recyclerView.setNestedScrollingEnabled(false);

        // During the orientation change, lists are saved. if lists or not saved that is first time. It will load
        // data from the json file. If lists object are saved due to orientation changes, lists will not be
        // loaded from json file
        if(citiesList == null && mappedList == null) {
            citiesList = new ArrayList<>();
            // load data from json file and sort cities
            citiesList = LoadData.getInstance().loadCitiesJsonFile(getActivity().getApplicationContext());
            // Mapped cities with alphabetic order in to hashmap
            mappedList = CacheCities.getInstance().getSortListCities(citiesList);

        }
            mAdapter = new CitiesAdapter(getActivity().getApplicationContext(), citiesList.subList(Consts.startPaginationIndex, Consts.endPaginationIndex), this, mappedList);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        // scroll listener to implement paging of city. Paging is done on 10 cities at a time
        recyclerView.addOnScrollListener(scrollListener);


        return view;
    }


    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener()
    {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy)
        {
            if(dy > 0) //check for scroll down
            {
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem  = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
                if ((visibleItemCount + firstVisibleItem) >= totalItemCount){
                    if(mAdapter.getCacheFilterCityList() == null || mAdapter.getCacheFilterCityList().size() ==0 ) {
                        mAdapter.setDataList(citiesList.subList(totalItemCount, totalItemCount + Consts.endPaginationIndex));
                    } else{
                        //  list is greater than or equal 10 items
                        if(mAdapter.getCacheFilterCityList().size() >= Consts.endPaginationIndex) {
                            mAdapter.setDataList(mAdapter.getCacheFilterCityList().subList(totalItemCount, totalItemCount + Consts.endPaginationIndex));
                        } else{
                            // if list items is lessthan 10 items
                            mAdapter.setDataList(mAdapter.getCacheFilterCityList().subList(totalItemCount, mAdapter.cacheFilterCityList.size()));

                        }
                    }
                    recyclerView.post(new Runnable() {
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }
    };

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        // Save list state
        listState = recyclerView.getLayoutManager().onSaveInstanceState();
        state.putParcelable(Consts.RECYCLER_LIST_STATE_KEY, listState);



    }

    @Override
    public void onResume() {
        super.onResume();
        if (listState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        /*if (mListener != null) {
            mListener.onFragmentInteraction(Double lat, Double longtitude);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Double lat, Double longtitude);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.searchmenu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }

        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            // searchview querylistener implemetation. For each character user typed adpater filter gets called
            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {

                    mAdapter.getFilter().filter(newText);
                    return false;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {

                    mAdapter.getFilter().filter(query);
                    return false;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }
}

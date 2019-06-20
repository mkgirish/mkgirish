package com.example.backbasecodetext;

import android.content.Context;
import android.view.View;

import com.example.backbasecodetext.AboutMVP.AboutInfo;
import com.example.backbasecodetext.AboutMVP.AboutModelImpl;
import com.example.backbasecodetext.AboutMVP.AboutPresenterImpl;
import com.example.backbasecodetext.Model.City;
import com.example.backbasecodetext.Util.CacheCities;
import com.example.backbasecodetext.Util.LoadData;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {
    @Mock
    Context mMockContext;
    HashMap<String, List<City>> mappedList;


    @Before
    public void setup(){


        List<City> citiesList = UnitLoadData.getInstance().loadCitiesJsonFile();
        mappedList = CacheCities.getInstance().getSortListCities(citiesList);

    }

    @After
    public void tearDown() {
        mappedList.clear();
    }

    @Test
    public void testSearchSuccessResult(){
        CopyOnWriteArrayList<City> filteredList = new CopyOnWriteArrayList<>() ;
        String firstchar ="a";
        String searchString = "aa";
        List<City> searchedList = mappedList.get(firstchar.toLowerCase());

        filteredList = CacheCities.getInstance().getFilterSearchList(searchedList, filteredList, searchString );
        assertEquals(filteredList.size() >0, true);



    }

    @Test
    public void testSearchUnSuccessResult(){
        CopyOnWriteArrayList<City> filteredList = new CopyOnWriteArrayList<>() ;
        String firstchar ="x";
        String searchString = "xyz123";

        List<City> searchedList = mappedList.get(firstchar.toLowerCase());

        filteredList = CacheCities.getInstance().getFilterSearchList(searchedList, filteredList,  searchString );

        assertEquals(filteredList.size() >0, false);



    }

    @Test
    public void testSearchCitySuccessResult(){
        CopyOnWriteArrayList<City> filteredList = new CopyOnWriteArrayList<>() ;
        String firstchar ="s";
        String searchString = "Skodsborg";

        List<City> searchedList = mappedList.get(firstchar.toLowerCase());

        filteredList = CacheCities.getInstance().getFilterSearchList(searchedList, filteredList,  searchString );
        //assertEquals(filteredList.size() ==1, true);
        assertEquals(filteredList.get(0).getName().equals("Skodsborg"), true);



    }

    @Test
    public void testSearchCityUnSuccessResult(){
        CopyOnWriteArrayList<City> filteredList = new CopyOnWriteArrayList<>() ;
        String firstchar ="s";
        String searchString = "Smidstrup";

        List<City> searchedList = mappedList.get(firstchar.toLowerCase());

        filteredList = CacheCities.getInstance().getFilterSearchList(searchedList, filteredList,  searchString );
        assertEquals(filteredList.size() >1, false);




    }

    @Test
    public void testSearchCityUnSuccessResult_1(){
        CopyOnWriteArrayList<City> filteredList = new CopyOnWriteArrayList<>() ;
        String firstchar ="s";
        String searchString = "Smidstrup123";

        List<City> searchedList = mappedList.get(firstchar.toLowerCase());

        filteredList = CacheCities.getInstance().getFilterSearchList(searchedList, filteredList,  searchString );
        assertEquals(filteredList.size() ==0, true);



    }

    @Test
    public void testSearchcharactersSuccessResult(){
        CopyOnWriteArrayList<City> filteredList = new CopyOnWriteArrayList<>() ;
        String firstchar ="b";
        String searchString = "ba";

        List<City> searchedList = mappedList.get(firstchar.toLowerCase());

        filteredList = CacheCities.getInstance().getFilterSearchList(searchedList, filteredList,  searchString );
        assertEquals(filteredList.size() >1, true);



    }

    @Test
    public void testSearchcharactersUnSuccessResult(){
        CopyOnWriteArrayList<City> filteredList = new CopyOnWriteArrayList<>() ;
        String firstchar ="b";
        String searchString = "bb";

        List<City> searchedList = mappedList.get(firstchar.toLowerCase());

        filteredList = CacheCities.getInstance().getFilterSearchList(searchedList, filteredList,  searchString );
        assertEquals(filteredList.size() ==0, true);



    }

    @Test
    public void testSearchMidCharactersSuccessResult(){
        CopyOnWriteArrayList<City> filteredList = new CopyOnWriteArrayList<>() ;
        String firstchar ="b";
        String searchString = "babur";

        List<City> searchedList = mappedList.get(firstchar.toLowerCase());

        filteredList = CacheCities.getInstance().getFilterSearchList(searchedList, filteredList,  searchString );
        assertEquals(filteredList.size() ==1, true);



    }

    @Test
    public void testSearchMidCharactersUnSuccessResult(){
        CopyOnWriteArrayList<City> filteredList = new CopyOnWriteArrayList<>() ;
        String firstchar ="b";
        String searchString = "baburi";

        List<City> searchedList = mappedList.get(firstchar.toLowerCase());

        filteredList = CacheCities.getInstance().getFilterSearchList(searchedList, filteredList,  searchString );
        assertEquals(filteredList.size() ==0, true);



    }

    @Test
    public void testAboutInfoSuccessResult(){
        AboutActivity view = Mockito.mock(AboutActivity.class);


        AboutPresenterImpl aboutPresenter = new AboutPresenterImpl(view, mMockContext);

        AboutModelImpl aboutModel = new AboutModelImpl(aboutPresenter, mMockContext);
        String jsonString = UnitLoadData.getInstance().getAssetAboutInfoJsonData();

        AboutInfo aboutInfo = aboutModel.parseAboutInfo(jsonString);

        assertEquals(!(aboutInfo.getDetails().isEmpty()), true);


        //aboutPresenter.getAboutInfo();

    }

    @Test
    public void testAboutCityInfoSuccessResult(){
        AboutActivity view = Mockito.mock(AboutActivity.class);


        AboutPresenterImpl aboutPresenter = new AboutPresenterImpl(view, mMockContext);

        AboutModelImpl aboutModel = new AboutModelImpl(aboutPresenter, mMockContext);
        String jsonString = UnitLoadData.getInstance().getAssetAboutInfoJsonData();

        AboutInfo aboutInfo = aboutModel.parseAboutInfo(jsonString);

        assertEquals(aboutInfo.getCity().equals("Amsterdam"), true);


        //aboutPresenter.getAboutInfo();

    }

    @Test
    public void testAboutCityInfoUnSuccessResult(){
        AboutActivity view = Mockito.mock(AboutActivity.class);


        AboutPresenterImpl aboutPresenter = new AboutPresenterImpl(view, mMockContext);

        AboutModelImpl aboutModel = new AboutModelImpl(aboutPresenter, mMockContext);
        String jsonString = UnitLoadData.getInstance().getAssetAboutInfoJsonData();

        AboutInfo aboutInfo = aboutModel.parseAboutInfo(jsonString);
        assertEquals(!(aboutInfo.getCity().equals("Newyork")), true);

    }

}
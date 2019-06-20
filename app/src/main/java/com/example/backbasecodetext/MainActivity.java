package com.example.backbasecodetext;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.backbasecodetext.Fragment.CitiesListFragment;
import com.example.backbasecodetext.Fragment.MapCityFragment;

public class MainActivity extends AppCompatActivity implements CitiesListFragment.OnFragmentInteractionListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public void onFragmentInteraction(Double latitude, Double longtitude) {

        MapCityFragment fragment = (MapCityFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment);

        // if fragment is not null and in layout, set text, else launch BodyActivity
        if ((fragment!=null)&&fragment.isInLayout()) {
            fragment.setLatLong(latitude, longtitude);
        } else {
            Intent intent = new Intent(this,MapActivity.class);
            intent.putExtra("latitude",latitude);
            intent.putExtra("longtitude", longtitude);
            startActivity(intent);
        }
    }


}

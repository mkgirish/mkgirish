package com.example.backbasecodetext;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.backbasecodetext.Fragment.MapCityFragment;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_acitivity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // get data from Intent extra
            double latitude = extras.getDouble("latitude");
            double longtitude = extras.getDouble("longtitude");

            Bundle bundle = new Bundle();
            bundle.putDouble("latitude", latitude);
            bundle.putDouble("longtitude", longtitude);

            FragmentManager fragmentManager = getSupportFragmentManager();
            MapCityFragment fragment1 = new MapCityFragment();
            fragment1.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.mapfragment, fragment1).commit();//now replace the argument fragment

        }

    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.example.weatherapplication.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.weatherapplication.Fragments.AddLocation;
import com.example.weatherapplication.Fragments.CurrentWeather;
import com.example.weatherapplication.Fragments.Favorte;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {
ArrayList<Fragment> fragments;
ArrayList<String> mCitiesList;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
switch (pos){


            case 1: return new AddLocation ();
            case 2: return new Favorte();

            default: return new CurrentWeather();
        }
}


    @Override
    public int getItemPosition(Object object) {
        // refresh all fragments when data set changed
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
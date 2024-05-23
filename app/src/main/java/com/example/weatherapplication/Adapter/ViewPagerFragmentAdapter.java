package com.example.weatherapplication.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    private ArrayList<Fragment> mFragmentList = new ArrayList<>();


    public ViewPagerFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragmentList.get ( position );
    }


    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }


//
//    public void removeSavedState(int position) {
////        if(position < mFragmentList.size()) {
////            mFragmentList.set(position, null);
////        }
//        mFragmentList.remove ( position );
//    }

    @Override
    public int getItemCount() {
        return mFragmentList.size();
    }}
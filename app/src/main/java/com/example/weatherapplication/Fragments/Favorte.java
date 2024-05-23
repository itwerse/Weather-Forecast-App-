package com.example.weatherapplication.Fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weatherapplication.Database.DBManager;
import com.example.weatherapplication.Database.DatabaseHelper;
import com.example.weatherapplication.Interface.OnItemClick;
import com.example.weatherapplication.R;

import java.util.ArrayList;


public class Favorte extends Fragment {
    ArrayList<String> itemsCity;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db1;
    DBManager dbManager;
RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_favorte, container, false);
        itemsCity=new ArrayList<>();

        dbManager = new DBManager(getContext());
        openHelper = new DatabaseHelper(getContext());
        db1 = openHelper.getWritableDatabase();
        db1 = openHelper.getReadableDatabase();
        mRecyclerView=view.findViewById(R.id.recyclerView22);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext ());
        Cursor cursorCourses = db1.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null);
        if (cursorCourses.moveToFirst()) {
            do {
                cursorCourses.getString(1).toString();
                itemsCity.add(cursorCourses.getString(1).toString());

            } while (cursorCourses.moveToNext());
        }
//        mAdapter = new Favadapter( itemsCity, (OnItemClick) getContext (),getActivity() );
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();

        return view;
    }

}
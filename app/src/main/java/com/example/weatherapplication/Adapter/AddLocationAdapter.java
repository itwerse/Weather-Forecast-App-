package com.example.weatherapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapplication.Database.DBManager;
import com.example.weatherapplication.Database.DatabaseHelper;
import com.example.weatherapplication.Interface.OnItemClick;
import com.example.weatherapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class AddLocationAdapter extends RecyclerView.Adapter<AddLocationAdapter.ExampleViewHolder> {
    private ArrayList<String> mCityList;
    OnItemClick onItemClickListener;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db1, db2;
    DBManager dbManager;
    Context mContext;
    private OnItemClickListener2 mListener2;
    private ArrayList<Double> mLatList;
    private ArrayList<Double> mLngList;
    public interface OnItemClickListener2 {
        void onItemClick2(int position);
    }
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mCityName,time,date;
        public ImageView mDelete,mFav;
        public ConstraintLayout container;
        public ExampleViewHolder(View itemView) {
            super(itemView);
            mCityName = itemView.findViewById( R.id.cityname);
         time = itemView.findViewById( R.id.time1);
//            date = itemView.findViewById( R.id.date);
            mDelete=itemView.findViewById ( R.id.delete );
          mFav=itemView.findViewById ( R.id.fav );
            container=itemView.findViewById ( R.id.container );

        }
    }
    public AddLocationAdapter(ArrayList<String> exampleList,ArrayList<Double> latList,ArrayList<Double> lngList, OnItemClick onItemClick, Context context,OnItemClickListener2 mListener2) {
        mCityList = exampleList;
        mLatList = latList;
        mLngList = lngList;

        onItemClickListener=onItemClick;
        mContext =context;
        this.mListener2=mListener2;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate( R.layout.list_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, @SuppressLint ("RecyclerView") int position) {
        String currentItem = mCityList.get(position);
        double currentLat = mLatList.get(position); // Retrieve latitude from the list
        double currentLng = mLngList.get(position); // Retrieve longitude from the list

        dbManager = new DBManager(mContext);
        openHelper = new DatabaseHelper(mContext);
        db1 = openHelper.getWritableDatabase();
        db1 = openHelper.getReadableDatabase();
        holder.mCityName.setText(currentItem);
        String currentDate = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());
holder.time.setText(currentTime);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemSelected_city(currentLat,currentLng);
            }
        });
        holder.mDelete.setOnClickListener ( new View.OnClickListener ( ) {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                if (mListener2 != null) {
                    mListener2.onItemClick2(position);
                }
                db1.delete(DatabaseHelper.TABLE_NAME1, "City_Name=?", new String[]{currentItem});
                db1.close();



            }


        } );
    }


    @Override
    public int getItemCount() {
        return mCityList.size();
    }
}
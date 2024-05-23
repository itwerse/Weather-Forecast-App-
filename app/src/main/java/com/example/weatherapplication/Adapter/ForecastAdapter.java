package com.example.weatherapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapplication.R;

import java.util.Date;

public class ForecastAdapter extends BaseAdapter {
    String [] humidity;
    Context    mContext;
    String []  rain;
    String [] icon;
    String [] time;
    private static LayoutInflater inflater=null;
    public ForecastAdapter(Context context, String[] humidity, String[] rain, String[]icon, String[]time) {
        // TODO Auto-generated constructor stub
        this.humidity=humidity;
        this.mContext=context;
        this.rain=rain;
        this.icon=icon;
        this.time=time;


        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return humidity.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }
    public class Holder
    {
        TextView humidity;
        TextView rain;
        TextView icon_text;
        TextView time;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i , View view , ViewGroup viewGroup) {
        Holder holder=new Holder();

        View rowView;

        rowView = inflater.inflate( R.layout.forecast, null);
        holder.humidity=(TextView) rowView.findViewById( R.id.humidity);
        holder.rain=(TextView) rowView.findViewById( R.id.rain_description);
        holder.time=(TextView) rowView.findViewById( R.id.time);
        holder.icon_text=(TextView) rowView.findViewById( R.id.weather_icon_text);

        long timestamp = Long.parseLong(time[i]);
        Date expiry = new Date(timestamp * 1000);
        holder.time.setText(String.valueOf(expiry));

        holder.humidity.setText(humidity[i]);
        holder.rain.setText(rain[i]);
        rowView.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText( v.getContext ( ) , "You Clicked "+humidity[i], Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }
}

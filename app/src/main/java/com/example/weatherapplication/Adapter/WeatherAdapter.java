package com.example.weatherapplication.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapplication.Classes.UnitConvertor;
import com.example.weatherapplication.Classes.Weather;
import com.example.weatherapplication.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    private final static String PATH_TO_WEATHER_FONT = "fonts/weather.ttf";
    Typeface weather_font;
    Context  ctx;
    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll;
        ImageView    weather_icon;
        TextView     temperture;
        TextView rain_description;
        TextView time,date;
        Typeface weatherfont;
        TextView btn_more;


        WeatherViewHolder(View itemView) {
            super ( itemView );
            ll = (LinearLayout) itemView.findViewById ( R.id.recycler_id );
            weather_icon = (ImageView) itemView.findViewById ( R.id.weather_icon_text );
            btn_more=(TextView) itemView.findViewById ( R.id.btn_more );
            temperture = (TextView) itemView.findViewById ( R.id.temperture_TextView );
            rain_description = (TextView) itemView.findViewById ( R.id.rain_description__TextView );
            time = (TextView) itemView.findViewById ( R.id.time_TextView );
            date = (TextView) itemView.findViewById ( R.id.date_tv );
        }
    }

    List<Weather> weather;

    public WeatherAdapter(List<Weather> weathers,Context mContext) {
        //  Log.w("strong", String.valueOf(weathers.get(0).getIcon ()));
        this.weather = weathers;
        this.ctx=mContext;

    }
    @Override
    public int getItemViewType(int position) {


        return position;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView ( recyclerView );
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup viewGroup , int i) {
        View v;
        v = LayoutInflater.from ( viewGroup.getContext ( ) ).inflate ( R.layout.cardview_forecast , viewGroup , false );
        WeatherViewHolder pvh = new WeatherViewHolder ( v );
        return pvh;
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder weatherViewHolder , int i) {

     //   weatherViewHolder.weather_icon.setTypeface ( weather_font );

        switch (weather.get ( i ).weather_icon) {
            case "01d":
                weatherViewHolder.weather_icon.setImageResource ( R.drawable.ic_clear_sky );
                break;
            case "02d":
                weatherViewHolder.weather_icon.setImageResource ( R.drawable.ic_few_clouds );

                break;
            case "03d":
                weatherViewHolder.weather_icon.setImageResource ( R.drawable.ic_scattered_clouds);

                break;
            case "04d":
                weatherViewHolder.weather_icon.setImageResource ( R.drawable.ic_broken_clouds );

                break;
            case "09d":
                weatherViewHolder.weather_icon.setImageResource ( R.drawable.ic_shower_rain );

                break;
            case "10d":
                weatherViewHolder.weather_icon.setImageResource ( R.drawable.ic_rain );

                break;
            case "11d":
                weatherViewHolder.weather_icon.setImageResource ( R.drawable.ic_thunderstorm );

                break;
            case "13d":
                weatherViewHolder.weather_icon.setImageResource ( R.drawable.ic_snow );

                break;
            case "01n":
                weatherViewHolder.weather_icon.setImageResource ( R.drawable.ic_clear_sky );

                break;
            case "50d":
                weatherViewHolder.weather_icon.setImageResource ( R.drawable.ic_mist );

                break;
            case "03n":
                weatherViewHolder.weather_icon.setImageResource ( R.drawable.ic_scattered_clouds);

                break;
            case "10n":
                weatherViewHolder.weather_icon.setImageResource ( R.drawable.ic_rain );

                break;
            case "11n":
                weatherViewHolder.weather_icon.setImageResource ( R.drawable.ic_thunderstorm );

                break;
            case "13n":
                weatherViewHolder.weather_icon.setImageResource ( R.drawable.ic_snow );

                break;}
        Log.w ( "time:" , String.valueOf ( weather.get ( i).time.contains ( " ") ) );
        long timestamp = Long.parseLong ( String.valueOf ( weather.get ( i ).time ) );
        Date expiry = new Date ( timestamp * 1000 );

        String format = new SimpleDateFormat ( "hh a" , Locale.ENGLISH ).format ( new Date ( String.valueOf ( expiry ) ) );
        String format2 = new SimpleDateFormat ( "EEEE" , Locale.ENGLISH ).format ( new Date ( String.valueOf ( expiry ) ) );


        Log.w ( "time:" , String.valueOf ( weather.get ( i).time.contains ( " ") ) );

        weatherViewHolder.time.setText ( String.valueOf ( format ) );
        weatherViewHolder.date.setText ( String.valueOf ( format2 ) );

//        weatherViewHolder.time.setText (weather.get ( i ).time);
        weatherViewHolder.rain_description.setText ( String.valueOf (   weather.get ( i ).rain_descr ));
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);

        float temperature1 = UnitConvertor.convertTemperature(   Float.parseFloat( String.valueOf (String.valueOf (weather.get ( i ).getHumidity ()) )) ,  sp  );
        if (sp.getBoolean("temperatureInteger", false)) {
            temperature1 = Math.round(temperature1);
            weatherViewHolder.temperture.setText (String.valueOf(Math.round(temperature1)+sp.getString("unit", "")));

        }else {
            weatherViewHolder.temperture.setText(String.valueOf(Math.round(temperature1) + sp.getString("unit", "C")));
        }
    }

    @Override
    public int getItemCount() {
        return weather.size ( );
    }
}
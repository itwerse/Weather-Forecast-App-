package com.example.weatherapplication.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapplication.Classes.UnitConvertor;
import com.example.weatherapplication.Modelss.WeatherDataFor;
import com.example.weatherapplication.R;

import java.util.List;
import java.util.Locale;

public class WeatherDataAdapter extends RecyclerView.Adapter<WeatherDataAdapter.WeatherDataViewHolder> {
    private List<WeatherDataFor> weatherDataList;
Context ctx;
    public WeatherDataAdapter(List<WeatherDataFor> weatherDataList,Context context) {
        this.weatherDataList = weatherDataList;
        this.ctx=context;
    }

    @NonNull
    @Override
    public WeatherDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_data, parent, false);
        return new WeatherDataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherDataViewHolder holder, int position) {
        WeatherDataFor weatherData = weatherDataList.get(position);
        holder.tvDate.setText(weatherData.getDt());
        holder.tvmaxTemp.setText(String.valueOf(Math.round(weatherData.getLongDayTemp())));
        holder.tvmintemp.setText(String.valueOf(Math.round(weatherData.getLongNightTemp())));

        String rain_fall= String.valueOf(weatherData.getRainfall());
        if (rain_fall.contains("0.0")){
            holder.rain_icon.setImageResource ( R.drawable.umc );

        }else {
            holder.rain_icon.setImageResource ( R.drawable.um );

        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        // Inside your adapter or wherever you're setting the text for tvRainfall
        float rainfall = weatherData.getRainfall();

// Use UnitConverter to format the rainfall
        String formattedRainfall = String.format(Locale.ENGLISH, "%.2f %s",
                UnitConvertor.convertRain(rainfall, sp),
                sp.getString("lengthUnit", ""));

        holder.tvRainfall.setText(formattedRainfall);

        holder.tvRainfall.setText(formattedRainfall);
        float temperature1 = UnitConvertor.convertTemperature(   Float.parseFloat(String.valueOf (weatherData.getLongDayTemp())) ,  sp  );
        float temperature2 = UnitConvertor.convertTemperature(   Float.parseFloat(String.valueOf (weatherData.getLongNightTemp())) ,  sp  );

        if (sp.getBoolean("temperatureInteger", false)) {
            holder.tvmaxTemp.setText(String.valueOf(Math.round(temperature1)+sp.getString("unit", "")));
            holder.tvmintemp.setText(String.valueOf(Math.round(temperature2)+sp.getString("unit", "")));
        }else {
            holder.tvmaxTemp.setText(String.valueOf(Math.round(temperature1)+sp.getString("unit", "C")));
            holder.tvmintemp.setText(String.valueOf(Math.round(temperature2)+sp.getString("unit", "C")));
        }
        switch (weatherData.getWeatherIcon()) {
            case "01d":
                holder.weatherIcon.setImageResource ( R.drawable.ic_clear_sky );
                break;
            case "02d":
                holder.weatherIcon.setImageResource ( R.drawable.ic_few_clouds );

                break;
            case "03d":
                holder.weatherIcon.setImageResource ( R.drawable.ic_scattered_clouds);

                break;
            case "04d":
                holder.weatherIcon.setImageResource ( R.drawable.ic_broken_clouds );

                break;
            case "09d":
                holder.weatherIcon.setImageResource ( R.drawable.ic_shower_rain );

                break;
            case "10d":
                holder.weatherIcon.setImageResource ( R.drawable.ic_rain );

                break;
            case "11d":
                holder.weatherIcon.setImageResource ( R.drawable.ic_thunderstorm );

                break;
            case "13d":
                holder.weatherIcon.setImageResource ( R.drawable.ic_snow );

                break;
            case "01n":
                holder.weatherIcon.setImageResource ( R.drawable.ic_clear_sky );

                break;
            case "50d":
                holder.weatherIcon.setImageResource ( R.drawable.ic_mist );

                break;
            case "03n":
                holder.weatherIcon.setImageResource ( R.drawable.ic_scattered_clouds);

                break;
            case "10n":
                holder.weatherIcon.setImageResource ( R.drawable.ic_rain );

                break;
            case "11n":
                holder.weatherIcon.setImageResource ( R.drawable.ic_thunderstorm );

                break;
            case "13n":
                holder.weatherIcon.setImageResource ( R.drawable.ic_snow );

                break;}

        // Bind other attributes to corresponding UI elements
    }

    @Override
    public int getItemCount() {
        return weatherDataList.size();
    }

    public static class WeatherDataViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate;
        public TextView tvmaxTemp;
        public TextView tvmintemp;
        public TextView tvRainfall;
        public ImageView weatherIcon,rain_icon;

        public WeatherDataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvmaxTemp = itemView.findViewById(R.id.tvLongDayTemp);
            tvmintemp = itemView.findViewById(R.id.tvLongNightTemp);
            tvRainfall = itemView.findViewById(R.id.tvRainfall);
            weatherIcon = itemView.findViewById(R.id.icon);
            rain_icon = itemView.findViewById(R.id.rain_icon);

            // Bind other UI elements to their respective views
        }
    }
}

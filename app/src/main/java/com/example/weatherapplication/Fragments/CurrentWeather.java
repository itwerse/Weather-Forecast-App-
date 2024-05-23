package com.example.weatherapplication.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapplication.Activities.ForecastActivity;
import com.example.weatherapplication.Activities.MainActivity;
import com.example.weatherapplication.Adapter.WeatherAdapter;
import com.example.weatherapplication.Adapter.WeatherDataAdapter;
import com.example.weatherapplication.Classes.AutoScrollRecyclerView;
import com.example.weatherapplication.Classes.UnitConvertor;
import com.example.weatherapplication.Classes.Weather;
import com.example.weatherapplication.Classes.WeatherChartView;
import com.example.weatherapplication.Client.ApiClient;
import com.example.weatherapplication.Interface.ApiInterface;
import com.example.weatherapplication.Interface.ResponseCurrentWeatherData;
import com.example.weatherapplication.Model.ForecastData;
import com.example.weatherapplication.Model.Time;
import com.example.weatherapplication.Model.WeatherData;
import com.example.weatherapplication.Modelss.WeatherDataFor;
import com.example.weatherapplication.R;
import com.example.weatherapplication.ssv.SunriseSunsetView;
import com.github.matteobattilana.weather.PrecipType;
import com.github.matteobattilana.weather.WeatherView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentWeather extends Fragment implements ResponseCurrentWeatherData, Animation.AnimationListener {
    private RecyclerView mRecyclerView;
    Context c;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView mDescription;
    ImageView mWeather_icon;
    private ProgressDialog progress;
    TextView mTemp,
            mHumidity,
            mPressure, mSpeed, mSunrise, mSunset, mTemp_max, mTemp_min, mFeelLike, mVisbility;
    LinearLayout btn_forecast;
    LinearLayout adLayout;
    FrameLayout frameLayout;

    private int progressStatus = 40;
    FusedLocationProviderClient client;
    public List<Weather> weathers;
    private static final int REQUEST_LOCATION_PERMISSION = 111;
    private GoogleMap mGoogleMap;
    Location mlocation;
    private SunriseSunsetView mSunriseSunsetView;
    TextView btn_more;
    ImageView icon1, icon2, icon3, icon4, icon5, icon6;
    String my_City;
    String mForeCastCity;
    public List<Weather> weathers_auto;

    LineChart lineChart;
    WeatherAdapter weatherAdapter_auto;
    ArrayList<String> tempData;
    ArrayList<String> wind;
    ArrayList<String> description;
    ArrayList<String> icons;
    ArrayList<String> time;
    float temperature1, temperature2, temperature3, temperature4, temperature5, temperature6;
    Double temp_dta, temp_dta1, temp_dta2, temp_dta3, temp_dta4, temp_dta5;
    float temp1, temp2, temp3, temp4, temp5, temp6;
    ArrayList dataForeCast;
    private static ArrayList<Response<ForecastData>> mForecastModel;
    ArrayList<Entry> mValues;
    TextView cityName;
    SharedPreferences sp;
    Animation animFade;
    WeatherChartView chartView;
    WeatherView weatherView;
    ConstraintLayout bt_top;
    private InterstitialAd mInterstitialAd;
    AutoScrollRecyclerView autoScrollRecyclerView;
RecyclerView recyclerView_for;
    TextView tem_minn, tem_max;
    private final String TAG = "NativeAdActivity".getClass().getSimpleName();
    TextView tv_time1, tv_time2, tv_time3, tv_time4, tv_time5, tv_time6;
    String tim1, tim2, tim3, tim4, tim5, tim6;
    ArrayList<WeatherDataFor> weatherDataList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_weather1, container, false);
        weatherView = view.findViewById(R.id.weather_view);
        recyclerView_for=view.findViewById(R.id.rv_for);
        tem_minn = view.findViewById(R.id.tv_temp_min);
        weatherDataList=new ArrayList<>();
        tem_max = view.findViewById(R.id.tv_temp_max);
        tv_time1 = view.findViewById(R.id.time1);
        tv_time2 = view.findViewById(R.id.time2);
        tv_time3 = view.findViewById(R.id.time3);
        tv_time4 = view.findViewById(R.id.time4);
        tv_time5 = view.findViewById(R.id.time5);
        tv_time6 = view.findViewById(R.id.time6);
        frameLayout = view.findViewById(R.id.native_banner_ad_container);
        bt_top = view.findViewById(R.id.bg_top);
        animFade = AnimationUtils.loadAnimation(getContext(), R.anim.fade);
        animFade.setAnimationListener(this);
        cityName = view.findViewById(R.id.tv_cityName);
        mTemp = view.findViewById(R.id.tv_temp);

        dataForeCast = new ArrayList();
        weathers_auto = new ArrayList<>();
        wind = new ArrayList<>();
        autoScrollRecyclerView = new AutoScrollRecyclerView(getActivity());
        autoScrollRecyclerView = view.findViewById(R.id.recylerViewAuto);
        btn_more = view.findViewById(R.id.btn_more);
        mForecastModel = new ArrayList<>();

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(getContext(), "ca-app-pub-7956328597641937/1235432065", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("TAG", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d("TAG", loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
        chartView = (WeatherChartView) view.findViewById(R.id.line_char);
        // set day
        //set icon
        icon1 = view.findViewById(R.id.weather_icon_text);
        icon2 = view.findViewById(R.id.eleven);
        icon3 = view.findViewById(R.id.two);
        icon4 = view.findViewById(R.id.five);
        icon5 = view.findViewById(R.id.eight);
        icon6 = view.findViewById(R.id.eleven2);
        //////


        sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();


                if (mInterstitialAd != null) {
                    mInterstitialAd.show(getActivity());
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
            }
        });

        if (mValues != null) {
            mGraphData();
            //  Toast.makeText ( getContext (),"Data found",Toast.LENGTH_SHORT ).show ();
        } else {
            // Toast.makeText ( getContext (),"Data not found",Toast.LENGTH_SHORT ).show ();
        }
//        progress = new ProgressDialog(this.getContext());

        btn_forecast = view.findViewById(R.id.btn_Forcast);
        mWeather_icon = view.findViewById(R.id.iv_Weather);
        mWeather_icon.startAnimation(animFade);
        mDescription = view.findViewById(R.id.tv_descrip);
        mSunrise = view.findViewById(R.id.tv_sunrise);
        mSunset = view.findViewById(R.id.tv_sunset);

        mHumidity = view.findViewById(R.id.tv_humidity);
        mPressure = view.findViewById(R.id.tv_pressure);
        mSpeed = view.findViewById(R.id.tv_speed);
        mVisbility = view.findViewById(R.id.tv_visibility);
        mTemp_max = view.findViewById(R.id.tv_tempMax);
        mTemp_min = view.findViewById(R.id.tv_tempMin);
        mFeelLike = view.findViewById(R.id.tv_feellike);
//        loadNativeAd();
        mSunriseSunsetView = view.findViewById(R.id.ssv);
        /* Get instance of MainActivity*/
        Log.e("TAGFORCAST", "onCreateView: " + MainActivity.weathers);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).passVal((ResponseCurrentWeatherData) this);
        }


        return view;
    }

    public void mGraphData() {
        mValues = new ArrayList<>();
        mValues.add(new Entry(0, Float.valueOf(String.valueOf(temp_dta))));
        mValues.add(new Entry(1, Float.valueOf(String.valueOf(temp_dta1))));
        mValues.add(new Entry(2, Float.valueOf(String.valueOf(temp_dta2))));
        mValues.add(new Entry(3, Float.valueOf(String.valueOf(temp_dta3))));
        mValues.add(new Entry(4, Float.valueOf(String.valueOf(temp_dta4))));
        mValues.add(new Entry(5, Float.valueOf(String.valueOf(temp_dta5))));
        chartView.setTempDay(new int[]{(int) Float.parseFloat(String.valueOf(temp_dta)), (int) Float.parseFloat(String.valueOf(temp_dta1)), (int) Float.parseFloat(String.valueOf(temp_dta2)), (int) Float.parseFloat(String.valueOf(temp_dta3)), (int) Float.parseFloat(String.valueOf(temp_dta4)), (int) Float.parseFloat(String.valueOf(temp_dta5))});
        chartView.setTempNight(new int[]{(int) Float.parseFloat(String.valueOf(temp_dta - 3)), (int) Float.parseFloat(String.valueOf(temp_dta1 - 3)), (int) Float.parseFloat(String.valueOf(temp_dta2 - 3)), (int) Float.parseFloat(String.valueOf(temp_dta3 - 3)), (int) Float.parseFloat(String.valueOf(temp_dta4 - 3)), (int) Float.parseFloat(String.valueOf(temp_dta5 - 3))});
        // set night
        chartView.invalidate();


    }

    private void sendData() {
        Intent i = new Intent(getActivity().getBaseContext(),
                ForecastActivity.class);

        //PACK DATA
        i.putExtra("SENDER_KEY", "MyFragment");
        i.putExtra("NAME_KEY", my_City.toString());

        Bundle bundle = new Bundle();
        i.putExtra("Temperature", tempData);
        i.putExtra("Description", description);
        i.putExtra("Icons", icons);
        i.putExtra("Time", time);
        i.putExtras(bundle);


        //START ACTIVITY
        getActivity().startActivity(i);
    }

    public void get_forecast_Details(double lat ,double lng) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ForecastData> call = apiInterface.forecast_DataByLatLng(lat,lng);
        call.enqueue(new Callback<ForecastData>() {
            @Override
            public void onResponse(Call<ForecastData> call, Response<ForecastData> forecastDataResponse) {
                if (forecastDataResponse.isSuccessful()) {
                    mForecastModel.add(forecastDataResponse);
//                    progress.dismiss();
                    dataForeCast.add(forecastDataResponse);
                    mForecastModel.add(forecastDataResponse);
                    tempData = new ArrayList<String>();
                    description = new ArrayList<>();
                    icons = new ArrayList<>();
                    time = new ArrayList<>();

                    for (int i = 0; i < 39; i++) {
                        description.add(forecastDataResponse.body().getList().get(i).getWeather().get(0).getDescription());
                        float rains;
                        rains = forecastDataResponse.body().getList().get(0).getMain().getPressure();
                        mVisbility.setText(String.valueOf(rains) + " ft");
                        float temperature1 = UnitConvertor.convertTemperature((Float.parseFloat(String.valueOf(forecastDataResponse.body().getList().get(i).getMain().getTemp()))), sp);
                        String temp_DTA = String.valueOf(temperature1);
                        Double temperatureDta = Double.valueOf(temp_DTA);
                        tempData.add(String.valueOf(Math.round(temperatureDta) + sp.getString("unit", "°C")));
                        icons.add(forecastDataResponse.body().getList().get(i).getWeather().get(0).getIcon());
                        time.add(String.valueOf(forecastDataResponse.body().getList().get(i).getDt()));
                        wind.add(String.valueOf(forecastDataResponse.body().getList().get(i).getWind().getSpeed()));
                    }

                    temp1 = (forecastDataResponse.body().getList().get(0).getMain().getTemp());
                    temp2 = (forecastDataResponse.body().getList().get(1).getMain().getTemp());
                    temp3 = (forecastDataResponse.body().getList().get(2).getMain().getTemp());
                    temp4 = (forecastDataResponse.body().getList().get(3).getMain().getTemp());
                    temp5 = (forecastDataResponse.body().getList().get(4).getMain().getTemp());
                    temp6 = (forecastDataResponse.body().getList().get(6).getMain().getTemp());
                    tim1 = String.valueOf((forecastDataResponse.body().getList().get(0).getDt()));
                    tim2 = String.valueOf((forecastDataResponse.body().getList().get(1).getDt()));
                    tim3 = String.valueOf((forecastDataResponse.body().getList().get(2).getDt()));
                    tim4 = String.valueOf((forecastDataResponse.body().getList().get(3).getDt()));
                    tim5 = String.valueOf((forecastDataResponse.body().getList().get(4).getDt()));
                    tim6 = String.valueOf((forecastDataResponse.body().getList().get(5).getDt()));
                    long times1 = Long.parseLong(tim1);
                    long times2 = Long.parseLong(tim2);
                    long times3 = Long.parseLong(tim3);
                    long times4 = Long.parseLong(tim4);
                    long times5 = Long.parseLong(tim5);
                    long times6 = Long.parseLong(tim6);
                    Date expiry11, expiry22, expiry33, expiry44, expiry55, expiry66;
                    expiry11 = new Date(times1 * 1000);
                    expiry22 = new Date(times2 * 1000);
                    expiry33 = new Date(times3 * 1000);
                    expiry44 = new Date(times4 * 1000);
                    expiry55 = new Date(times5 * 1000);
                    expiry66 = new Date(times6 * 1000);
                    String time11 = (String) android.text.format.DateFormat.format("hh:mm", expiry11); // 20
                    String time22 = (String) android.text.format.DateFormat.format("hh:mm", expiry22); // 20
                    String time33 = (String) android.text.format.DateFormat.format("hh:mm", expiry33); // 20
                    String time44 = (String) android.text.format.DateFormat.format("hh:mm", expiry44); // 20
                    String time55 = (String) android.text.format.DateFormat.format("hh:mm", expiry55); // 20
                    String time66 = (String) android.text.format.DateFormat.format("hh:mm", expiry66); // 20
                    tv_time1.setText(time11);
                    tv_time2.setText(time22);
                    tv_time3.setText(time33);
                    tv_time4.setText(time44);
                    tv_time5.setText(time55);
                    tv_time6.setText(time66);

//         sp = PreferenceManager.getDefaultSharedPreferences(getContext ()  );
                    temperature1 = UnitConvertor.convertTemperature(Float.parseFloat(String.valueOf(temp1)), sp);
                    temperature2 = UnitConvertor.convertTemperature(Float.parseFloat(String.valueOf(temp2)), sp);
                    temperature3 = UnitConvertor.convertTemperature(Float.parseFloat(String.valueOf(temp3)), sp);
                    temperature4 = UnitConvertor.convertTemperature(Float.parseFloat(String.valueOf(temp4)), sp);
                    temperature5 = UnitConvertor.convertTemperature(Float.parseFloat(String.valueOf(temp5)), sp);
                    temperature6 = UnitConvertor.convertTemperature(Float.parseFloat(String.valueOf(temp6)), sp);
                    temp_dta = Double.valueOf(String.valueOf(temperature1));
                    temp_dta1 = Double.valueOf(String.valueOf(temperature2));
                    temp_dta2 = Double.valueOf(String.valueOf(temperature3));
                    temp_dta3 = Double.valueOf(String.valueOf(temperature4));
                    temp_dta4 = Double.valueOf(String.valueOf(temperature5));
                    temp_dta5 = Double.valueOf(String.valueOf(temperature6));


                    //Set Graph Data
                    mGraphData();
                    for (int i = 0; i <= 5; i++) {
                        switch (forecastDataResponse.body().getList().get(i).getWeather().get(0).getIcon()) {
                            case "01d":
//                            bt_top.setBackgroundResource(R.drawable.background_night_cloudy_bg);
                                icon1.setImageResource(R.drawable.ic_clear_sky);

                                if (i == 1) {
                                    icon2.setImageResource(R.drawable.ic_clear_sky);
                                }
                                if (i == 2) {
                                    icon3.setImageResource(R.drawable.ic_clear_sky);
                                }
                                if (i == 3) {
                                    icon4.setImageResource(R.drawable.ic_clear_sky);
                                }
                                if (i == 4) {
                                    icon5.setImageResource(R.drawable.ic_clear_sky);
                                }
                                if (i == 5) {
                                    icon6.setImageResource(R.drawable.ic_clear_sky);
                                }
                                break;
                            case "01n":

                                icon1.setImageResource(R.drawable.moon);
                                if (i == 1) {
                                    icon2.setImageResource(R.drawable.moon);
                                }
                                if (i == 2) {
                                    icon3.setImageResource(R.drawable.moon);
                                }
                                if (i == 3) {
                                    icon4.setImageResource(R.drawable.moon);
                                }
                                if (i == 4) {
                                    icon5.setImageResource(R.drawable.moon);
                                }
                                if (i == 5) {
                                    icon6.setImageResource(R.drawable.moon);
                                }
                                break;
                            case "02d":

                                icon1.setImageResource(R.drawable.ic_few_clouds);
                                if (i == 1) {
                                    icon2.setImageResource(R.drawable.ic_few_clouds);
                                }
                                if (i == 2) {
                                    icon3.setImageResource(R.drawable.ic_few_clouds);
                                }
                                if (i == 3) {
                                    icon4.setImageResource(R.drawable.ic_few_clouds);
                                }
                                if (i == 4) {
                                    icon5.setImageResource(R.drawable.ic_few_clouds);
                                }
                                if (i == 5) {
                                    icon6.setImageResource(R.drawable.ic_few_clouds);
                                } else {
                                }
                                break;
                            case "03d":

                                icon1.setImageResource(R.drawable.ic_scattered_clouds);
                                if (i == 1) {
                                    icon2.setImageResource(R.drawable.ic_scattered_clouds);
                                }
                                if (i == 2) {
                                    icon3.setImageResource(R.drawable.ic_scattered_clouds);
                                }
                                if (i == 3) {
                                    icon4.setImageResource(R.drawable.ic_scattered_clouds);
                                }
                                if (i == 4) {
                                    icon5.setImageResource(R.drawable.ic_scattered_clouds);
                                }
                                if (i == 5) {
                                    icon6.setImageResource(R.drawable.ic_scattered_clouds);
                                } else {
                                }
                                break;
                            case "04d":
                                icon1.setImageResource(R.drawable.ic_broken_clouds);
                                if (i == 1) {
                                    icon2.setImageResource(R.drawable.ic_broken_clouds);
                                }
                                if (i == 2) {
                                    icon3.setImageResource(R.drawable.ic_broken_clouds);
                                }
                                if (i == 3) {
                                    icon4.setImageResource(R.drawable.ic_broken_clouds);
                                }
                                if (i == 4) {
                                    icon5.setImageResource(R.drawable.ic_broken_clouds);
                                }
                                if (i == 5) {
                                    icon6.setImageResource(R.drawable.ic_broken_clouds);
                                }
                                break;
                            case "09d":

                                icon1.setImageResource(R.drawable.ic_shower_rain);
                                if (i == 1) {
                                    icon2.setImageResource(R.drawable.ic_shower_rain);
                                }
                                if (i == 2) {
                                    icon3.setImageResource(R.drawable.ic_shower_rain);
                                }
                                if (i == 3) {
                                    icon4.setImageResource(R.drawable.ic_shower_rain);
                                }
                                if (i == 4) {
                                    icon5.setImageResource(R.drawable.ic_shower_rain);
                                }
                                if (i == 5) {
                                    icon6.setImageResource(R.drawable.ic_shower_rain);
                                }

                                break;
                            case "09n":

                                icon1.setImageResource(R.drawable.ic_shower_rain);
                                if (i == 1) {
                                    icon2.setImageResource(R.drawable.ic_shower_rain);
                                }
                                if (i == 2) {
                                    icon3.setImageResource(R.drawable.ic_shower_rain);
                                }
                                if (i == 3) {
                                    icon4.setImageResource(R.drawable.ic_shower_rain);
                                }
                                if (i == 4) {
                                    icon5.setImageResource(R.drawable.ic_shower_rain);
                                }
                                if (i == 5) {
                                    icon6.setImageResource(R.drawable.ic_shower_rain);
                                }

                                break;
                            case "10d":

                                icon1.setImageResource(R.drawable.ic_rain);
                                if (i == 1) {
                                    icon2.setImageResource(R.drawable.ic_rain);
                                }
                                if (i == 2) {
                                    icon3.setImageResource(R.drawable.ic_rain);
                                }
                                if (i == 3) {
                                    icon4.setImageResource(R.drawable.ic_rain);
                                }
                                if (i == 4) {
                                    icon5.setImageResource(R.drawable.ic_rain);
                                }
                                if (i == 5) {
                                    icon6.setImageResource(R.drawable.ic_rain);
                                }

                                break;
                            case "10n":

                                icon1.setImageResource(R.drawable.ic_rain);
                                if (i == 1) {
                                    icon2.setImageResource(R.drawable.ic_rain);
                                }
                                if (i == 2) {
                                    icon3.setImageResource(R.drawable.ic_rain);
                                }
                                if (i == 3) {
                                    icon4.setImageResource(R.drawable.ic_rain);
                                }
                                if (i == 4) {
                                    icon5.setImageResource(R.drawable.ic_rain);
                                }
                                if (i == 5) {
                                    icon6.setImageResource(R.drawable.ic_rain);
                                }

                                break;
                            case "11d":

                                icon1.setImageResource(R.drawable.ic_thunderstorm);
                                if (i == 1) {
                                    icon2.setImageResource(R.drawable.ic_thunderstorm);
                                }
                                if (i == 2) {
                                    icon3.setImageResource(R.drawable.ic_thunderstorm);
                                }
                                if (i == 3) {
                                    icon4.setImageResource(R.drawable.ic_thunderstorm);
                                }
                                if (i == 4) {
                                    icon5.setImageResource(R.drawable.ic_thunderstorm);
                                }
                                if (i == 5) {
                                    icon6.setImageResource(R.drawable.ic_thunderstorm);
                                }// weatherViewHolder.weather_icon1.setImageResource ( R.drawable.w11d );
                                break;
                            case "13d":

                                icon1.setImageResource(R.drawable.ic_snow);
                                if (i == 1) {
                                    icon2.setImageResource(R.drawable.ic_snow);
                                }
                                if (i == 2) {
                                    icon3.setImageResource(R.drawable.ic_snow);
                                }
                                if (i == 3) {
                                    icon4.setImageResource(R.drawable.ic_snow);
                                }
                                if (i == 4) {
                                    icon5.setImageResource(R.drawable.ic_snow);
                                }
                                if (i == 5) {
                                    icon6.setImageResource(R.drawable.ic_snow);
                                }
                                // weatherViewHolder.weather_icon1.setImageResource ( R.drawable.w13d);
                                break;
                            case "04n":

                                icon1.setImageResource(R.drawable.ic_broken_clouds);
                                if (i == 1) {
                                    icon2.setImageResource(R.drawable.ic_broken_clouds);
                                }
                                if (i == 2) {
                                    icon3.setImageResource(R.drawable.ic_broken_clouds);
                                }
                                if (i == 3) {
                                    icon4.setImageResource(R.drawable.ic_broken_clouds);
                                }
                                if (i == 4) {
                                    icon5.setImageResource(R.drawable.ic_broken_clouds);
                                }
                                if (i == 5) {
                                    icon6.setImageResource(R.drawable.ic_broken_clouds);
                                }
                                //  weatherViewHolder.weather_icon1.setImageResource ( R.drawable.w04d );
                                break;
                            case "03n":

                                icon1.setImageResource(R.drawable.ic_scattered_clouds);
                                if (i == 1) {
                                    icon2.setImageResource(R.drawable.ic_scattered_clouds);
                                }
                                if (i == 2) {
                                    icon3.setImageResource(R.drawable.ic_scattered_clouds);
                                }
                                if (i == 3) {
                                    icon4.setImageResource(R.drawable.ic_scattered_clouds);
                                }
                                if (i == 4) {
                                    icon5.setImageResource(R.drawable.ic_scattered_clouds);
                                }
                                if (i == 5) {
                                    icon6.setImageResource(R.drawable.ic_scattered_clouds);
                                }
                                //   weatherViewHolder.weather_icon1.setImageResource ( R.drawable.w04d );
                                break;
                        }

                        for (int j = 0; j <= 39; j++) {
                            String data = (forecastDataResponse.body().getList().get(8).getWeather().get(0).getIcon());
                            Log.e("TAG", "onSetForecastData: " + data);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ForecastData> call, Throwable t) {
                Log.e("TAG12", "onResponse: " + "fail");
            }
        });
    }


    /*  get current weather   */
    @Override
    public void onSetCurrentWeather(Response<WeatherData> weatherData, String mCity,double lat,double lng) {
//        Toast.makeText(c, "weather data", Toast.LENGTH_SHORT).show();
        mForeCastCity = mCity;
        my_City = mCity;
        Log.e("TAG", "onSetCurrentWeather: "+weatherData);
//        progress.setMessage("Loading ...");
//        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progress.setIndeterminate(true);
//        progress.setProgress(0);
//        progress.show();
        fetchWeatherForecast(lat,lng);
        get_forecast_Details(lat,lng);
        getForecastData(lat,lng);
        cityName.setText(mCity);
        // Temperature
        float temperature = UnitConvertor.convertTemperature(
                Float.parseFloat(String.valueOf(weatherData.body().getMain().getTemp())), sp);
        // Wind
        float m_temp = 0;


        String mytext = Float.toString(temperature);
        Double temperatureDta = Double.valueOf(mytext);

        double roundOff = Math.round(temperatureDta * 100) / 100;
        float temperature_current = UnitConvertor.convertTemperature(
                Float.parseFloat(String.valueOf(weatherData.body().getMain().getTemp())), sp);
        // Wind
//        download();
        mTemp.setText(String.valueOf(Math.round(temperature_current)) + sp.getString("unit", ""));
        mDescription.setText(weatherData.body().getName());
        switch (weatherData.body().getWeather().get(0).getIcon()) {
            case "01d":
                bt_top.setBackgroundResource(R.drawable.bg_cloudy_day);

                mWeather_icon.setImageResource(R.drawable.ic_clear_sky);
                break;
            case "02d":
                mWeather_icon.setImageResource(R.drawable.ic_few_clouds);
                bt_top.setBackgroundResource(R.drawable.bg_cloudy_day);

                break;
            case "02n":
                bt_top.setBackgroundResource(R.drawable.bg_cloudy_night);

                mWeather_icon.setImageResource(R.drawable.ic_few_clouds);

                break;
            case "03d":
                bt_top.setBackgroundResource(R.drawable.background_cloudy);

                mWeather_icon.setImageResource(R.drawable.ic_scattered_clouds);
                break;
            case "04d":
                bt_top.setBackgroundResource(R.drawable.background_cloudy);

                mWeather_icon.setImageResource(R.drawable.ic_broken_clouds);
                break;
            case "04n":
                bt_top.setBackgroundResource(R.drawable.bg_cloudy_night);

                mWeather_icon.setImageResource(R.drawable.ic_broken_clouds);
                break;
            case "09d":
                bt_top.setBackgroundResource(R.drawable.bg_night_rain);
                weatherView.setWeatherData(PrecipType.RAIN);
                mWeather_icon.setImageResource(R.drawable.ic_shower_rain);
                break;
            case "09n":
                mWeather_icon.setImageResource(R.drawable.ic_shower_rain);
                bt_top.setBackgroundResource(R.drawable.bg_night_rain);
                weatherView.setWeatherData(PrecipType.RAIN);

                break;
            case "10d":
                mWeather_icon.setImageResource(R.drawable.ic_rain);
                bt_top.setBackgroundResource(R.drawable.background_rain_night);
                weatherView.setWeatherData(PrecipType.RAIN);

                break;
            case "11d":
                mWeather_icon.setImageResource(R.drawable.ic_thunderstorm);
                bt_top.setBackgroundResource(R.drawable.bg_day_rain);
                weatherView.setWeatherData(PrecipType.RAIN);

                break;
            case "13d":
                mWeather_icon.setImageResource(R.drawable.ic_snow);
                bt_top.setBackgroundResource(R.drawable.snow_night);

                break;
            case "01n":
                mWeather_icon.setImageResource(R.drawable.ic_clear_sky);
                bt_top.setBackgroundResource(R.drawable.few_cloud_s_bg);

                break;
            case "50d":
                mWeather_icon.setImageResource(R.drawable.ic_mist);
                break;
            case "50n":
                mWeather_icon.setImageResource(R.drawable.ic_mist);
                break;
            case "03n":
                bt_top.setBackgroundResource(R.drawable.few_cloud_s_bg);
//                weatherView.setWeatherData(PrecipType.RAIN);

                mWeather_icon.setImageResource(R.drawable.ic_scattered_clouds);
                break;
            case "10n":
                bt_top.setBackgroundResource(R.drawable.bg_cloudy_night);
                weatherView.setWeatherData(PrecipType.RAIN);

                mWeather_icon.setImageResource(R.drawable.ic_rain);
                break;
            case "11n":
                mWeather_icon.setImageResource(R.drawable.ic_thunderstorm);
                bt_top.setBackgroundResource(R.drawable.bg_night_rain);
                weatherView.setWeatherData(PrecipType.RAIN);


                break;
            case "13n":
                weatherView.setWeatherData(PrecipType.SNOW);

                bt_top.setBackgroundResource(R.drawable.snow_night);
                mWeather_icon.setImageResource(R.drawable.ic_snow);
                break;
        }

        Long rise = weatherData.body().getSys().getSunrise();
        Long set = weatherData.body().getSys().getSunset();
        String sunriseValue = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(rise * 1000));
        String sunsetValue = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(set * 1000));
        //Set Seek Bar with time format
        int sunriseHour = Integer.parseInt(new SimpleDateFormat("hh", Locale.ENGLISH).format(new Date(rise * 1000)));
        int sunriseMinute = Integer.parseInt(new SimpleDateFormat("mm", Locale.ENGLISH).format(new Date(rise * 1000)));
        int sunsetHour = Integer.parseInt(new SimpleDateFormat("HH", Locale.ENGLISH).format(new Date(set * 1000)));
        int sunsetMinute = Integer.parseInt(new SimpleDateFormat("mm", Locale.ENGLISH).format(new Date(set * 1000)));
        ///
        refreshSSV(sunriseHour, sunriseMinute, sunsetHour, sunsetMinute);
        mSunrise.setText(sunriseValue);
        mSunset.setText(sunsetValue);
        float speed = (float) UnitConvertor.convertWind((float) Double.parseDouble(weatherData.body().getWind().getSpeed().toString()), sp);
        String speed_value = Float.toString(speed);
        mSpeed.setText(speed_value + " "+sp.getString("speedUnit", "mph"));
        float temp_feel_like = UnitConvertor.convertTemperature(
                Float.parseFloat(String.valueOf(weatherData.body().getMain().getFeels_like())), sp);
        String mtemp_feel = Float.toString(temp_feel_like);
        Double temperture_feel_like = Double.valueOf(mtemp_feel);
        double roundOff_feel = Math.round(temperture_feel_like * 100) / 100;
        mTemp.setText(String.valueOf(Math.round(temperature_current)) + sp.getString("unit", ""));
        mFeelLike.setText("Feel like" + " " + Math.round(roundOff_feel) + sp.getString("unit", ""));
        float temperature_min = UnitConvertor.convertTemperature(
                Float.parseFloat(String.valueOf(weatherData.body().getMain().getTemp_min())), sp);
        float temperature_max = UnitConvertor.convertTemperature(
                Float.parseFloat(String.valueOf(weatherData.body().getMain().getTemp_max())), sp);
//        mTemp_max.setText(Math.round(temperature_max) + sp.getString("unit", ""));
//        tem_max.setText("H:" + Math.round(temperature_max) + "°");
        mTemp_min.setText(Math.round(temperature_min - 1) + sp.getString("unit", ""));
//        tem_minn.setText("L:" + Math.round(temperature_min - 1) + "°");

        mDescription.setText(weatherData.body().getWeather().get(0).getDescription());
        mHumidity.setText(weatherData.body().getMain().getHumidity() + "%");
        float pressure = UnitConvertor.convertPressure((float) Double.parseDouble(weatherData.body().getMain().getPressure().toString()), sp);
        String str = Float.toString(pressure);
        mPressure.setText(str + " " + sp.getString("pressureUnit", "hPa"));

    }

    @Override
    public void onSetForecastData(Response<ForecastData> forecastDataResponse) {

    }

    /*  method for sun set and sun rise in progress bar*/
    private void refreshSSV(int sunriseHour, int sunriseMinute, int sunsetHour, int sunsetMinute) {
        mSunriseSunsetView.setSunriseTime(new Time(sunriseHour, sunriseMinute));
        mSunriseSunsetView.setSunsetTime(new Time(sunsetHour, sunsetMinute));
        mSunriseSunsetView.startAnimate();


    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void fetchWeatherForecast(double lat,double lng) {
        ApiInterface apiInterface = ApiClient.getClient ( ).create ( ApiInterface.class );
        Call<JsonObject> call = apiInterface.forecastDataByLatLng(lat,lng);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.e("TAG", "onResponseFOOR "+response );
                        JSONObject responseJson = new JSONObject(response.body().toString());
                        JSONArray listArray = responseJson.getJSONArray("list");
                        for (int i = 0; i < listArray.length(); i++) {
                            JSONObject forecastJson = listArray.getJSONObject(i);
                            long dt = forecastJson.getLong("dt");
                            long milliseconds = dt * 1000;
                            // Create a Date object from the milliseconds
                            java.util.Date date = new java.util.Date(milliseconds);
                            // Get the day of the week
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE");
                            String day=sdf.format(date);
                           JSONObject tempJson = forecastJson.getJSONObject("temp");
                            double maxTemperature = tempJson.getDouble("max");
                            double minTemperature = tempJson.getDouble("min");


                            // Extract other temperature values as needed
                            double rainfall = forecastJson.optDouble("rain", 0.0);
                            float rainfallFloat = (float) rainfall; // Using casting
                            JSONArray weatherArray = forecastJson.getJSONArray("weather");
                            JSONObject weatherJson = weatherArray.getJSONObject(0);
                            String weatherIcon = weatherJson.getString("icon");
                            // Extract the required data from the forecastJson object
                            Log.e("TAG", "onResponse+DDd: "+dt+maxTemperature+"   "+weatherIcon+"rainfall"+ rainfall );
                            // Create DailyForecast object or add data to your RecyclerView adapter
                            // Add the forecast object to your list or adapter
                            WeatherDataFor weatherData1 = new WeatherDataFor(day, maxTemperature, minTemperature, rainfallFloat,weatherIcon);
                            weatherDataList.add(weatherData1);
                           double max_tem= UnitConvertor.convertTemperature(
                                    Float.parseFloat(String.valueOf(weatherDataList.get(0).getLongDayTemp())), sp);
                           double min_tem= UnitConvertor.convertTemperature(
                                    Float.parseFloat(String.valueOf(weatherDataList.get(0).getLongNightTemp())), sp);
                            tem_max.setText("H:" + Math.round(max_tem) + "°");
                            tem_minn.setText("L:" + Math.round(min_tem) + "°");
                            mTemp_max.setText(Math.round(max_tem) + sp.getString("unit", ""));

                        }

                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView_for.setLayoutManager(layoutManager);

                        WeatherDataAdapter adapter = new WeatherDataAdapter(weatherDataList,getContext());
                        recyclerView_for.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("TAG", "onResponse+DDd: "+e.getMessage() );

                        // Handle JSON parsing error
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        // Handle null responseString error
                    }

                    // Process the response data as needed
                } else {

                    // Handle API error
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Handle network or other errors
            }
        });

    }

    public void getForecastData(double lat ,double lng) {
        //    download ();
        ApiInterface apiInterface = ApiClient.getClient ( ).create ( ApiInterface.class );
        Call<ForecastData> call = apiInterface.forecast_DataByLatLng (lat, lng);
        call.enqueue ( new Callback<ForecastData> ( ) {
            @Override
            public void onResponse(Call<ForecastData> call , Response<ForecastData> response) {
                if ( response.isSuccessful ( ) ) {
                    Log.e("TAG", "onResponse:ffxx "+response.toString() );

                    Log.e ( "TAG787" , "onResponse: " +response.body ().getList ()  );
                    String[] temperature = new String[40];
                    String[] rain_description = new String[40];
                    String[] icon = new String[40];
                    String[] time = new String[40];
                    weathers_auto = new ArrayList<> ( );
        for (int i = 0; i <=38; i++) {
                    temperature[i] = String.valueOf ( response.body ( ).getList ( ).get ( i ).getMain ( ).getTemp ( )  );
                    rain_description[i] = String.valueOf ( response.body ( ).getList ( ).get ( i ).getWeather ( ).get ( 0 ).getDescription ( ) );
                    icon[i] = String.valueOf ( response.body ( ).getList ( ).get ( i ).getWeather ( ).get ( 0 ).getIcon ( ) );
//                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext ());
        float temperature1 = UnitConvertor.convertTemperature(  ( Float.parseFloat( String.valueOf (response.body ( ).getList ( ).get ( i ).getMain ( ).getTemp ( ) ) ) ) ,  sp  );
//                    if (sp.getBoolean("temperatureInteger", false)) {
//                        temperature1 = Math.round(temperature1);
//                    }
                   // Log.e ( "TAG45" , "onSetForecastData33: " +temperature1  );
                    time[i] = String.valueOf ( response.body ( ).getList ( ).get ( i ).getDt ( ) );
                    if ( time[i].contains ( "Sat" ) ) {
                        Log.e ( "FORECAST" , "onSetForecastData: " + time[i] );
                        Log.w ( "time" , time[i] );
                        Log.w ( "humidity" , temperature[i] );
                    }
                    Log.w ( "rain_description" , rain_description[i] );
                    Log.w ( "icon" , icon[i] );
                  weathers_auto.add ( new Weather ( String.valueOf ( response.body ( ).getList ( ).get ( i ).getWeather ( ).get ( 0 ).getIcon ( ) ) ,String.valueOf (  temperature1) , String.valueOf ( response.body ( ).getList ( ).get (i ).getWeather ( ).get ( 0 ).getDescription ( ) ) , String.valueOf ( response.body ( ).getList ( ).get ( i ).getDt ( ) ) ) );


   }
                    weatherAdapter_auto = new WeatherAdapter( weathers_auto,getContext() );
                    autoScrollRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
                    autoScrollRecyclerView.setAdapter ( weatherAdapter_auto );
            weatherAdapter_auto.notifyDataSetChanged ();
            autoScrollRecyclerView.setLoopEnabled(true);

            autoScrollRecyclerView.startAutoScroll();

            autoScrollRecyclerView.isLoopEnabled();
            autoScrollRecyclerView.setNestedScrollingEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<ForecastData> call , Throwable t) {
                Log.e ( "TAG12" , "onResponse: " + "fail" );

            }
        } );
    }
}



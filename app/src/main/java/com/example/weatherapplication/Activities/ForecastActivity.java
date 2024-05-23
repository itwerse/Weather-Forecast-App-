package com.example.weatherapplication.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weatherapplication.Adapter.WeatherAdapter;
import com.example.weatherapplication.Classes.UnitConvertor;
import com.example.weatherapplication.Classes.Weather;
import com.example.weatherapplication.Client.ApiClient;
import com.example.weatherapplication.Interface.ApiInterface;
import com.example.weatherapplication.Interface.ResponseCurrentWeatherData;
import com.example.weatherapplication.Interface.SendForecastData;
import com.example.weatherapplication.Model.ForecastData;
import com.example.weatherapplication.Model.WeatherData;
import com.example.weatherapplication.R;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastActivity extends AppCompatActivity  {
    private RecyclerView mRecyclerView;
    Context c;
    private RecyclerView.Adapter       mAdapter;
    private    RecyclerView.LayoutManager mLayoutManager;
    public List<Weather>              weathers;
    ImageView back_btn;
    TextView tv_CityName;
    ArrayList arrayList;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_forecast );
        mRecyclerView = findViewById ( R.id.recylerView );
//        mLayoutManager = new LinearLayoutManager ( getActivity ( ) );
        mRecyclerView.setLayoutManager ( mLayoutManager );
        Window window = ForecastActivity.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(ForecastActivity.this,R.color.app_primary_dark12));
        arrayList=new ArrayList (  );
        back_btn=findViewById ( R.id.back );

        back_btn.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                finish ();
            }
        } );
        tv_CityName=findViewById ( R.id.tv_city_name );
        receiveData();
    }
    private void receiveData()
    {
        ArrayList data=new ArrayList (  );
        //RECEIVE DATA VIA INTENT
        Intent i_data = getIntent();
        String name = i_data.getStringExtra("NAME_KEY");
        Bundle bundle = getIntent().getExtras();
        arrayList = i_data.getStringArrayListExtra("stock_list");
        ArrayList<Float> tempData = new ArrayList<> ();
        ArrayList<Float> descriptionDATA = new ArrayList<> ();
        ArrayList<Float> iconData = new ArrayList<> ();
        ArrayList<Float> timeData1 = new ArrayList<> ();
        timeData1.clear ();
        tempData = (ArrayList<Float>) getIntent().getSerializableExtra("Temperature");
        descriptionDATA = (ArrayList<Float>) getIntent().getSerializableExtra("Description");
        iconData = (ArrayList<Float>) getIntent().getSerializableExtra("Icons");
        timeData1 = (ArrayList<Float>) getIntent().getSerializableExtra("Time");


//        if (sp.getBoolean("temperatureInteger", false)) {
//            temperature1 = Math.round(temperature1);
//        }
        weathers=new ArrayList<> (  );
       for (int i = 0; i <39; i++) {

            weathers.add ( new Weather ( String.valueOf ( iconData.get ( i ) ), String.valueOf ( tempData.get ( i ) ) , String.valueOf ( descriptionDATA.get ( i ) ) , String.valueOf ( timeData1.get ( i ) ) ) );
         //  weathers.add ( new Weather ( String.valueOf ( tempData.get ( i ) ) , String.valueOf ( descriptionDATA.get ( i ) ) , String.valueOf ( tempData.get ( i ) ) , String.valueOf ( descriptionDATA.get ( i) ) ) );

       }
        mAdapter = new WeatherAdapter ( weathers,getApplicationContext() );
        mRecyclerView.setAdapter ( mAdapter );

        mRecyclerView.setLayoutManager ( new GridLayoutManager ( getApplicationContext ( ) , 1 ) );
        mAdapter.notifyDataSetChanged ();
        Log.e ( "TAG" , "receiveData: " +tempData  );
        tv_CityName.setText ( name );

      // getForecastData( name);
        //SET DATA TO TEXTVIEWS

    }



}
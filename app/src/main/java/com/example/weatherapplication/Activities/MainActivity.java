package com.example.weatherapplication.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weatherapplication.Adapter.ViewPagerFragmentAdapter;
import com.example.weatherapplication.Classes.AdsManager;
import com.example.weatherapplication.Classes.TemperatureForegroundService;
import com.example.weatherapplication.Classes.Weather;
import com.example.weatherapplication.Client.ApiClient;
import com.example.weatherapplication.Database.DBManager;
import com.example.weatherapplication.Database.DatabaseHelper;
import com.example.weatherapplication.Fragments.AddLocation;
import com.example.weatherapplication.Fragments.CurrentWeather;
import com.example.weatherapplication.Fragments.Favorte;
import com.example.weatherapplication.Interface.ApiInterface;
import com.example.weatherapplication.Interface.OnItemClick;
import com.example.weatherapplication.Interface.ResponseCurrentWeatherData;
import com.example.weatherapplication.Interface.SendData;
import com.example.weatherapplication.Interface.SendForecastData;
import com.example.weatherapplication.Model.ForecastData;
import com.example.weatherapplication.Model.WeatherData;
import com.example.weatherapplication.R;
import com.google.android.datatransport.BuildConfig;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnItemClick, SendData, NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ArrayList<Response<WeatherData>> mCWeatherModel;
    private ArrayList<Response<ForecastData>> mForecastModel;
    private ArrayList<String> m_CitiesList;
    private ArrayList<String> m_CitiesList_send;
    private NavigationView navigationView;
    private SwitchCompat darkModeSwitch;
    FusedLocationProviderClient client;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    public static final ArrayList<Weather> weathers = new ArrayList<>();
    ViewPager2 viewPager;
    ResponseCurrentWeatherData responseCurrentWeatherData;
    SendForecastData sendForecastData;
    private ProgressDialog progress;
    ViewPagerFragmentAdapter adapter;
    ArrayList<String> mCitiesList;
    DotsIndicator dotsIndicator;
    ArrayList<String> itemsCity;
    ArrayList<String> noDuplicates;
    String temptoFar;
    String value_to_far;
    int baseOnPos = 2;
    double latitude;
    double longitude;
    ImageButton mUpdate, mSearch, favourite;
    private WindowManager mWindowManager_dialog;
    private View mFloatingView_dialog;
    TextView tv_pp;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db1, db2;
    DBManager dbManager;

    // Initialize AdLoader and NativeAdOptions
    private AdLoader adLoader;
    private NativeAd nativeAd;

    @SuppressLint({"NewApi", "MissingPermission"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = new DBManager(this);
        openHelper = new DatabaseHelper(this);
        db1 = openHelper.getWritableDatabase();
        db1 = openHelper.getReadableDatabase();



        initializeViews();
        toggleDrawer();
        initializeDefaultFragment(savedInstanceState, 0);
        setDarkModeSwitchListener();
        mCitiesList = new ArrayList<>();
        viewPager = (ViewPager2) findViewById(R.id.viewPager);
        m_CitiesList = new ArrayList<>();
        m_CitiesList_send = new ArrayList<>();
        mUpdate = findViewById(R.id.update);
        favourite = findViewById(R.id.fav);
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download();
                viewPager.setCurrentItem(0);
                getCurrentLocation();

            }
        });
        mSearch = findViewById(R.id.search);
        Window window = MainActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.app_primary_dark12));
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCityFragemnt();

            }
        });
        mCWeatherModel = new ArrayList<Response<WeatherData>>();
        mForecastModel = new ArrayList<Response<ForecastData>>();
        /*.......DotsIndicator.......*/
        dotsIndicator = (DotsIndicator) findViewById(R.id.dots_indicator);

        /////load data from cache memory
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (!prefs.getBoolean("firstTimepack", false)) {
            loadData();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTimepack", true);
            editor.commit();
        } else {
            mCitiesList.clear();
            m_CitiesList.clear();
            mForecastModel.clear();
            mCWeatherModel.clear();

            loadData();
        }

        download();
        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        myAsyncTasks.execute();

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress = new ProgressDialog(MainActivity.this);
                progress.setMessage("Fetching ...");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.setProgress(0);
                progress.show();

                Handler handler = new Handler();
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        } catch (Exception e) {
                        }

                        handler.post(new Runnable() {
                            public void run() {

                                progress.dismiss();
                            }
                        });
                    }
                }).start();
                viewPager.setCurrentItem(0);
            }

        });
    }

    public void download() {
        progress = new ProgressDialog(this);
        progress.setMessage("Loading ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

    }

    //Here is new method
    public void passVal(ResponseCurrentWeatherData responseCurrentWeatherData) {
        this.responseCurrentWeatherData = responseCurrentWeatherData;

    }

    //Here is new method
    public void passVal2(SendForecastData sendForecastData) {
        this.sendForecastData = sendForecastData;

    }


    //Get weather by lat lng
    private void weatherByLatLon(double lat, double lng) {
        //   Toast.makeText ( getApplicationContext ( ) , "data" , Toast.LENGTH_SHORT ).show ( );
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<WeatherData> call = apiInterface.getWeatherByLatLng(lat, lng);
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {

                progress.dismiss();
                if (response.isSuccessful()) {
                    String mCity = response.body().getName();
                    Log.e("TAG", "onResponse: " + mCity);
                    m_CitiesList.clear();
//                    progress.dismiss();
//                    getWeatherData(mCity);
//                   getForecastData(mCity);
                    mCWeatherModel.clear();
                    mCWeatherModel.add(response);
                    Log.e("TAG", "weatherByLatlng " + response);
                    //Collections.reverse ( mCWeatherModel );
//                    Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                    m_CitiesList.clear();
                    m_CitiesList_send.clear();
                    m_CitiesList.add(mCity);
                    m_CitiesList_send.add(mCity);
                    temptoFar = response.body().getMain().getTemp() + "F";

//                        saveData ( );

                    addTabs(viewPager);
                    Log.e("TAGVV", "onResponse: " + m_CitiesList);


                    } else {
                        new AlertDialog.Builder ( MainActivity.this )
                                .setTitle ( getResources ( ).getString ( R.string.app_name ) )
                                .setMessage ( getResources ( ).getString ( R.string.citynotfound ) )
                                .setPositiveButton ( "OK" , null ).show ( );
                    }


            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
            }
        });
    }

    ///Get  forecast Weather by city Name
    private void getForecastData(String name) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ForecastData> call = apiInterface.getForecast_Data(name);
        call.enqueue(new Callback<ForecastData>() {
            @Override
            public void onResponse(Call<ForecastData> call, Response<ForecastData> response) {
                if (response.isSuccessful()) {
                    mForecastModel.add(response);
                    progress.dismiss();
                    Log.e("NoDuplicatesList0", response.toString());
                    for (int i = 0; i < mForecastModel.size(); i++) {

                        Log.d("NoDuplicatesList1", String.valueOf(mForecastModel.get(i)));
                    }
                } else {

                    Log.e("TAG12", "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ForecastData> call, Throwable t) {
                Log.e("TAG12", "onResponse: " + "fail");
            }
        });
    }

    @Override
    public void sendCityName(double latitude1, double longitude1) {

        download();
//        latitude1=latitude;
//        longitude1=longitude;
//        viewPager.setCurrentItem(0);

        weatherByLatLon(latitude1,longitude1);
        Log.e("AddLocation", "sendCityName: "+latitude1+" "+longitude1 );
        latitude=latitude1;
        longitude=longitude1;
        Log.e("AddLocation", "sendCityName1: "+latitude+" "+longitude );


    }


//    @Override
//    public void sendCityDetails(String cityName, double latitude, double longitude) {
//        download ( );
//        viewPager.setCurrentItem (0);
//        weatherByLatLon ( latitude , latitude );
////        getForecastByLatLon ( lat , lng )
//    }

    /////////Aync Class//////////////
    public class MyAsyncTasks extends AsyncTask<String, String, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   download ();
            // display a progress dialog for good user experiance

        }

        @Override
        protected Void doInBackground(String... params) {
            //////////// View pager ////////////
            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }

                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    if (position == 0) {
                        if (!mCWeatherModel.isEmpty() && !m_CitiesList_send.isEmpty()) {
                            responseCurrentWeatherData.onSetCurrentWeather(mCWeatherModel.get(0), m_CitiesList_send.get(0),latitude,longitude);
                        }

                        // if((mCWeatherModel.size ()==1)|| (mCWeatherModel.size ()==2)||(mCWeatherModel.size ()==3)||(mCWeatherModel.size ()==4)) {
//                   responseCurrentWeatherData.onSetCurrentWeather(mCWeatherModel.get(0), m_CitiesList_send.get(0));
                    }


                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    super.onPageScrollStateChanged(state);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
        }

    }

    //Get  forecast Weather by lat lng
    private void getForecastByLatLon(double lat, double lng) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ForecastData> call = apiInterface.forecast_DataByLatLng(lat, lng);
        call.enqueue(new Callback<ForecastData>() {
            public List<Weather> weathers;
            @Override
            public void onResponse(Call<ForecastData> call, Response<ForecastData> response) {
                if (response.isSuccessful()) {
                    mForecastModel.add(response);
                    progress.dismiss();
                    Log.e("NoDuplicatesList0", response.toString());
                    for (int i = 0; i < mForecastModel.size(); i++) {

                        Log.d("NoDuplicatesList1", String.valueOf(mForecastModel.get(i)));
                    }
                } else {

                    Log.e("TAG12", "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ForecastData> call, Throwable t) {
                Log.e("TAG12", "onResponse: " + "fail");
//               Toast.makeText ( getActivity (),"fail ",Toast.LENGTH_SHORT ).show ();
            }
        });
    }

    //Get  current Weather by city name
    public void getWeatherData(String name) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<WeatherData> call = apiInterface.getWeatherData(name);
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.e("TAG", "onResponse:ff " + response.toString());
                        mCWeatherModel.clear();
                        mCWeatherModel.add(response);

                        //Collections.reverse ( mCWeatherModel );

                        m_CitiesList.clear();
                        m_CitiesList_send.clear();
                        m_CitiesList.add(name);
                        m_CitiesList_send.add(name);
                        temptoFar = response.body().getMain().getTemp() + "F";

                        progress.dismiss();
//                        saveData ( );

                        addTabs(viewPager);
                        Log.e("TAGVV", "onResponse: " + m_CitiesList);


                    } else {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(getResources().getString(R.string.app_name))
                                .setMessage(getResources().getString(R.string.citynotfound))
                                .setPositiveButton("OK", null).show();
                        //     Toast.makeText ( getApplicationContext ( ) , "City Not Found" , Toast.LENGTH_SHORT ).show ( );
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {

            }
        });
    }

    // Add Fragments and set dots indicator
    private void addTabs(ViewPager2 viewPager) {
        adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle());
//            for (int i = 0; i < m_CitiesList.size ( ); i++) {
        adapter.addFragment(new CurrentWeather());
//    }
        adapter.addFragment(new AddLocation());
//        adapter.addFragment ( new Favorte( ) );
        Log.e("TAGSS", "addTabs: " + m_CitiesList);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);

        dotsIndicator.setViewPager2(viewPager);
        Log.e("POS", "onCreate: " + viewPager.getCurrentItem());
    }
    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar_id);
        toolbar.setTitle(R.string.toolbar_title);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout_id);
        navigationView = findViewById(R.id.navigationview_id);
        navigationView.setNavigationItemSelectedListener(this);
        darkModeSwitch = (SwitchCompat) navigationView.getMenu().findItem(R.id.nav_darkmode_id).getActionView();
    }


    private void initializeDefaultFragment(Bundle savedInstanceState, int itemIndex) {
        if (savedInstanceState == null) {
            MenuItem menuItem = navigationView.getMenu().getItem(itemIndex).setChecked(true);
            onNavigationItemSelected(menuItem);

        }
    }

    private void toggleDrawer() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        showCustomDialog_back();

    }

    public void addCityFragemnt() {

        viewPager.setCurrentItem(1);
//


    }

    //Navigation
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home_id:
                closeDrawer();
                break;
            case R.id.id_add_city:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                closeDrawer();
                //  mDialogUnitSettings ();
                break;

            case R.id.share:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
                break;
            case R.id.rate_us:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + getPackageName())));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                //3 Toast.makeText ( this , "Rate Pressed" , Toast.LENGTH_SHORT ).show ( );
                break;
            case R.id.feedback:
                sendFeedback();

                break;
            case R.id.privacy:
                showPrivacyPolicyDialog();

                break;


        }
        return true;
    }

    public void sendFeedback() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.feedback);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        EditText et_email = dialog.findViewById(R.id.et_email);
        EditText et_submit = dialog.findViewById(R.id.et_post);

        (dialog.findViewById(R.id.bt_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_email.getText())) {
                    Toast.makeText(getApplicationContext(), "Email is Empty", Toast.LENGTH_SHORT).show();

                }
                if (TextUtils.isEmpty(et_submit.getText())) {
                    Toast.makeText(getApplicationContext(), "Feedback is Empty", Toast.LENGTH_SHORT).show();

                }
                if (!TextUtils.isEmpty(et_email.getText()) && (!TextUtils.isEmpty(et_submit.getText()))) {
                    Toast.makeText(getApplicationContext(), "Submitted Successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        (dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });
        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void setDarkModeSwitchListener() {
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                    //  Toast.makeText ( MainActivity.this , "Dark Mode Turn Off" , Toast.LENGTH_SHORT ).show ( );
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                    //  Toast.makeText ( MainActivity.this , "Dark Mode Turn On" , Toast.LENGTH_SHORT ).show ( );
                }
            }
        });
    }

    private void closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    // Save values in shared preferences
    private void saveData() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : m_CitiesList) {
            stringBuilder.append(s);
            stringBuilder.append(",");
        }
        Log.e("TAG", "saveData: " + m_CitiesList);
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("words", stringBuilder.toString());
        editor.commit();
    }

    // Load values from shared preferences
    @SuppressLint("NewApi")
    private void loadData() {
        SharedPreferences sharedPreferences1 = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String wordsCity = sharedPreferences1.getString("words", "");
        String[] itemWords = wordsCity.split(",");
        itemsCity = new ArrayList<String>();
        for (int i = 0; i < itemWords.length; i++) {
            itemsCity.add(itemWords[i]);
            itemsCity.removeAll(Arrays.asList("", null));
        }
        noDuplicates = new ArrayList<String>();
        // Removing duplicates from  list
        for (String str : itemsCity) {
            if (!noDuplicates.contains(str)) {
                noDuplicates.add(str);
            }
        }
        if (itemsCity.size() == 0) {
// Check for location permissions
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // Request location permissions if not granted
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);

            } else {
                // Permissions are granted, get the current location
                getCurrentLocation();
            }

        } else {
            for (int i = 0; i < noDuplicates.size(); i++) {
//               getForecastData ( noDuplicates.get ( i ) );
                getWeatherData(noDuplicates.get(i));
                Log.d("NoDuplicatesList1111", noDuplicates.get(i));
            }
        }
    }

    private void getCurrentLocation() {
        // Create a location manager
        MyLocationManager locationManager = new MyLocationManager(this);

        // Request location updates
        locationManager.requestLocationUpdates(new MyLocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Handle the updated location here
                 latitude = location.getLatitude();
                 longitude = location.getLongitude();
                weatherByLatLon ( latitude , longitude );
                           getForecastByLatLon ( latitude , longitude );
                // Do something with the latitude and longitude
            }
        });
    }

    // Define a simple location manager
    private class MyLocationManager {
        private final AppCompatActivity activity;

        MyLocationManager(AppCompatActivity activity) {
            this.activity = activity;
        }

        void requestLocationUpdates(MyLocationListener listener) {
            // Use the LocationManager to request location updates
            android.location.LocationManager locationManager =
                    (android.location.LocationManager) activity.getSystemService(LOCATION_SERVICE);

            if (locationManager != null) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestSingleUpdate(android.location.LocationManager.NETWORK_PROVIDER,
                        new android.location.LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                listener.onLocationChanged(location);
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {
                            }

                            @Override
                            public void onProviderEnabled(String provider) {
                            }

                            @Override
                            public void onProviderDisabled(String provider) {
                            }
                        },
                        Looper.getMainLooper());
            }
        }
    }

    // Define a simple location listener
    private interface MyLocationListener {
        void onLocationChanged(Location location);
    }

    // Handle permission request results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the current location
                getCurrentLocation();
            } else {
                // Permission denied, handle accordingly (e.g., show a message or disable location features)
            }
        }
    }
    private boolean checkLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != android.content.pm.PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {

            // Request permissions if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    100);

            return false;
        }

        return true;
    }


    @Override
    public void onItemSelected(int position) {
Intent intent=new Intent(MainActivity.this,MainActivity.class);
startActivity(intent);

    }

    @Override
    public void onItemSelected_city(double lat, double lng) {
        weatherByLatLon(lat,lng);
        Log.e("AddLocation", "sendCityName: "+lat+" "+lng );
        latitude=lat;
        longitude=lng;
        Log.e("AddLocation", "sendCityName1: "+latitude+" "+longitude );

        viewPager.setCurrentItem(0);
    }


    private void refreshData(){
        m_CitiesList.clear ();

        loadData ();
    }
    private void showCustomDialog_back() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.backpressdialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) FrameLayout frameLayout= (FrameLayout) dialogView.findViewById(R.id.native_banner_ad_container);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        ///initialize ad
     AdsManager.getInstance().showNativeBanner2(this, MainActivity.this, frameLayout);

        (dialogView.findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        (dialogView.findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                alertDialog.dismiss();
            }
        });
        (dialogView.findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + getPackageName())));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        alertDialog.show();
    }


    private void showPrivacyPolicyDialog() {
        final Dialog dialog = new Dialog ( this );
        dialog.requestWindowFeature ( Window.FEATURE_NO_TITLE ); // before
        dialog.setContentView ( R.layout.privacypolicy );
        dialog.getWindow ( ).setBackgroundDrawable ( new ColorDrawable ( Color.TRANSPARENT ) );
        dialog.setCancelable ( true );
        dialog.setCanceledOnTouchOutside ( false );

        (dialog.findViewById ( R.id.btnDone )).setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                dialog.dismiss ( );
            }
        } );
        dialog.show ();
        dialog.getWindow().setLayout( WindowManager.LayoutParams.MATCH_PARENT , WindowManager.LayoutParams.MATCH_PARENT );

}}




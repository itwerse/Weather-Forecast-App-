package com.example.weatherapplication.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.weatherapplication.Classes.AdsManager;
import com.example.weatherapplication.Database.City;
import com.example.weatherapplication.Database.DBManager;
import com.example.weatherapplication.Database.DatabaseHelper;
import com.example.weatherapplication.Database.PFASQLiteHelper;
import com.example.weatherapplication.Interface.SEarchCity;
import com.example.weatherapplication.Interface.SendData;
import com.example.weatherapplication.R;

import com.example.weatherapplication.util.AutoCompleteCityTextViewGenerator;
import com.example.weatherapplication.util.MyConsumer;



public class AddLocationDialog extends DialogFragment  {
    private SEarchCity sEarchCity;


    Activity activity;
    View rootView;
    PFASQLiteHelper database;
String cityName;

    private AutoCompleteTextView autoCompleteTextView;
    private AutoCompleteCityTextViewGenerator cityTextViewGenerator;
    City selectedCity;
FrameLayout frameLayout1;
    final int LIST_LIMIT = 100;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db1, db2;
    DBManager dbManager;
    SendData sendData;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       if (context instanceof Activity){
            this.activity=(Activity) context;
        }
        try {
            sendData = (SendData) context;
        } catch(ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement DataPullingInterface");
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            sEarchCity = (SEarchCity) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
//        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.dialog_add_location, null);
        ImageView btn_cancel = (ImageView) view.findViewById(R.id.btncn);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        builder.setCancelable(false);
        frameLayout1 = (FrameLayout) view.findViewById(R.id.native_banner_ad_container1);
//        nativeAdLayout=(NativeAdLayout)view.findViewById(R.id.native_banner_ad_container1);
       AdsManager.getInstance().showNativeBanner2(getContext(), getActivity(), frameLayout1);
//        FacebookAdsManger.getInstance().showFacebookNativeAd(getContext(),nativeAdLayout);

        TextView btn_okay = (TextView) view.findViewById(R.id.btnok);
        final AutoCompleteTextView et = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTvAddDialog);
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityName = et.getText().toString();
                if (cityName.matches("")) {
                    Toast.makeText(getContext(), "You did not enter a City Name", Toast.LENGTH_SHORT).show();
                    return;
                }

// Storing data into SharedPreferences
//                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
//                SharedPreferences.Editor myEdit = sharedPreferences.edit();
//                myEdit.putString("Country", et.getText().toString());

//                myEdit.commit();
                if ((cityName != null)) {
//                    Intent intent = new Intent(getContext(), MainActivity.class);
//
//                    // now by putExtra method put the value in key, value pair
//                    // key is message_key by this key we will receive the value, and put the string
//
//                    intent.putExtra("message_key", cityName);
//
//                    // start the Intent
//                    startActivity(intent);

//                   sEarchCity.searchCityName(cityName.toString());
                    dbManager = new DBManager(getContext());
                    openHelper = new DatabaseHelper(getContext());
                    db1 = openHelper.getWritableDatabase();
                    db1 = openHelper.getReadableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseHelper.COL_2, cityName.toString());
                    long id = db1.insert(DatabaseHelper.TABLE_NAME1, null, contentValues);
                    Log.e("TAG", "onClick: ."+cityName.toString() );
//                    passValue ( cityName.toString() );

                    dismiss();
                    //                    if (NetworkConnectivity.isNetworkStatusAvailable(getApplicationContext())) {
//                        new GetPrayerTimes().execute();
//                    } else {
//                        LoadPreviousSalatData();
//                    }

                }

            }
        });
        rootView = view;

        builder.setView(view);
        builder.setTitle(getActivity().getString(R.string.dialog_add_label));

        this.database = PFASQLiteHelper.getInstance(getActivity());
//        final WebView webview= rootView.findViewById(R.id.webViewAddLocation);
//        webview.getSettings().setJavaScriptEnabled(true);
//        webview.getSettings().setUserAgentString(BuildConfig.APPLICATION_ID+"/"+BuildConfig.VERSION_NAME);
//        webview.setBackgroundColor(0x00000000);
//        webview.setBackgroundResource(R.drawable.map_back);
        cityTextViewGenerator = new AutoCompleteCityTextViewGenerator(getContext(), database);
        autoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTvAddDialog);

        cityTextViewGenerator.generate(autoCompleteTextView, LIST_LIMIT, EditorInfo.IME_ACTION_DONE, new MyConsumer<City>() {
            @Override
            public void accept(City city) {
                if(city!=null) {
                    selectedCity = city;

//Toast.makeText(getContext(),"data",Toast.LENGTH_SHORT).show();
                    //Hide keyboard to have more space
                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
                    //Show city on map
//                    webview.loadUrl("file:///android_asset/map.html?lat=" + selectedCity.getLatitude() + "&lon=" + selectedCity.getLongitude());

                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                performDone();
            }
        });

        builder.setPositiveButton(getActivity().getString(R.string.dialog_add_add_button), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                performDone();
            }
        });

        builder.setNegativeButton(getActivity().getString(R.string.dialog_add_close_button), null);

        return builder.create();
    }

    private void performDone() {
        if (selectedCity == null) {
            Toast.makeText(activity, R.string.dialog_add_no_city_found, Toast.LENGTH_SHORT).show();
        }else {
//            ((ManageLocationsActivity) activity).addCityToList(selectedCity);
            dismiss();
        }
    }

}

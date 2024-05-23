package com.example.weatherapplication.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapplication.Activities.MainActivity;
import com.example.weatherapplication.Adapter.AddLocationAdapter;
import com.example.weatherapplication.Database.DBManager;
import com.example.weatherapplication.Database.DatabaseHelper;
import com.example.weatherapplication.Interface.OnItemClick;
import com.example.weatherapplication.Interface.SEarchCity;
import com.example.weatherapplication.Interface.SendData;
import com.example.weatherapplication.R;
import com.example.weatherapplication.dialogs.AddLocationDialog;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AddLocation extends Fragment   implements AddLocationAdapter.OnItemClickListener2
        {
    LinearLayout buttonInsert;
    String              mCity_Name;
    String   cityName;
    TextView tv_city;
    TextView et_CityName;
    ArrayList<String> noDuplicates=new ArrayList<>();
    private RecyclerView               mRecyclerView;
    private AddLocationAdapter         mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SendData sendData;
    ArrayList<String> itemsCity;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db1, db2;
    DBManager dbManager;
            private ArrayList<Double> mLatList;
            private ArrayList<Double> mLngList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
    }
    TextView btn_add;
            // Declare latitude and longitude variables
            private double latitude;
            private double longitude;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate ( R.layout.fragment_add_location , container , false );
        itemsCity = new ArrayList<String> ( );
        mLatList=new ArrayList<>();
        mLngList=new ArrayList<>();
        dbManager = new DBManager(getContext());
        openHelper = new DatabaseHelper(getContext());
        db1 = openHelper.getWritableDatabase();
        db1 = openHelper.getReadableDatabase();
       mRecyclerView=view.findViewById(R.id.recyclerView223);                                                                                                                                                et_CityName = view.findViewById( R.id.et_addCity);
        Places.initialize(getContext(), getString(R.string.google_maps_key));


//   loadData();
        noDuplicates=new ArrayList<>();
     buttonInsert = view.findViewById( R.id.button_insert);
      // loadData();
        buildRecyclerView();
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGooglePlacesAutocomplete();

//                FragmentManager fragmentManager = getChildFragmentManager();
//                AddLocationDialog addLocationDialog = new AddLocationDialog();
//                addLocationDialog.show(fragmentManager, "AddLocationDialog");
//                getChildFragmentManager().executePendingTransactions();
//                addLocationDialog.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            }
        });

        return  view;
    }

    //Here is new method
//    public void passValue(String data) {
//      sendData.sendCityName ( data );
//    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach ( context );

        try {
            sendData = (SendData) context;
        } catch(ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement DataPullingInterface");
        }
    }

    // Save values in shared preferences


    private void buildRecyclerView() {
       mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext ());
        Cursor cursorCourses = db1.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME1, null);
        if (cursorCourses.moveToFirst()) {
            do {

                String city = cursorCourses.getString(1);
                double lat=cursorCourses.getDouble(2);
                double lng=cursorCourses.getDouble(3);

                if (city != null) {
                    itemsCity.add(city);
                    mLatList.add(lat);
                    mLngList.add(lng);

                }
// Outside of the loop
                mAdapter = new AddLocationAdapter(itemsCity,mLatList,mLngList, (OnItemClick) getContext(), getActivity(), this::onItemClick2);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);



            } while (cursorCourses.moveToNext());
        }

    }





            private void launchGooglePlacesAutocomplete() {
                List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                        .build(getContext());

                startActivityForResult(intent, 3); // Use a unique requestCode, e.g., 3
            }


            @Override
            public void onActivityResult(int requestCode, int resultCode, @NotNull Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                if (requestCode == 3) {
                    if (resultCode == AutocompleteActivity.RESULT_OK) {
                        Place place = Autocomplete.getPlaceFromIntent(data);
                        String address = place.getAddress();

                        // Use Geocoding API to get latitude and longitude
                        getLocationFromAddress(address);
                    } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                        Status status = Autocomplete.getStatusFromIntent(data);
                        Log.e("AddLocation", "Error: " + status.getStatusMessage());
                    } else if (resultCode == AutocompleteActivity.RESULT_CANCELED) {
                        Log.i("AddLocation", "Autocomplete canceled");
                    }
                }
            }

            private void getLocationFromAddress(String address) {
                Geocoder geocoder = new Geocoder(getContext());
                List<Address> addresses;

                try {
                    addresses = geocoder.getFromLocationName(address, 1);
                    if (!addresses.isEmpty()) {
                        Address location = addresses.get(0);
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        passValue(latitude,longitude);
                        Log.d("AddLocation", "Latitude: " + latitude + ", Longitude: " + longitude);

                        // Now you have latitude and longitude, you can use them as needed
                        // For example, you can save them to the database
                        saveLocationToDatabase(address, latitude, longitude);
                    } else {
                        Log.e("AddLocation", "No location found for the address: " + address);
                    }
                } catch (IOException e) {
                    Log.e("AddLocation", "Geocoding error: " + e.getMessage());
                }
            }

            private void saveLocationToDatabase(String address, double latitude, double longitude) {
                openHelper = new DatabaseHelper(getContext());
                db1 = openHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseHelper.COL_2, address);
                contentValues.put(DatabaseHelper.COL_LATITUDE, latitude);
                contentValues.put(DatabaseHelper.COL_LONGITUDE, longitude);
                long id = db1.insert(DatabaseHelper.TABLE_NAME1, null, contentValues);
                db1.close();

//                Toast.makeText(getContext(), "Location saved successfully!", Toast.LENGTH_SHORT).show();
            }
            // Modify passValue method to include latitude and longitude
            public void passValue(double latitude, double longitude) {
        sendData.sendCityName(latitude, longitude);
            }
            @Override
            public void onItemClick2(int position) {
                Toast.makeText(getContext(), "Deleted..", Toast.LENGTH_SHORT).show();
                mAdapter.notifyItemRemoved(position);
                itemsCity.remove(position);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);
            }


        }


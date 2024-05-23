package com.example.weatherapplication.Pref;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static String TAG = SessionManager.class.getSimpleName();
    SharedPreferences
            pref;
    SharedPreferences.Editor
            editor;
    Context
            _context;
    private static final String PREF_NAME = "MyLoginPref";

    private static final String KEY_TEMPERATURE_UNITS = "temperature";
    private static final String KEY_TIME_FORMAT = "time";
    private static final String KEY_PRESSURE = "pressure";


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }
    public void setKeyTemperatureUnits(int tempUnit)
    {

        editor.putInt(KEY_TEMPERATURE_UNITS, tempUnit);
        editor.commit();
    }
    public int getKeyTemperatureUnit(){
        return pref.getInt(KEY_TEMPERATURE_UNITS,0);


    }


}

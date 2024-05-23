package com.example.weatherapplication.Classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;

public class PrefConfig


{
    private static final String LIST_KEY="list_key";
    public static void writeListInPref(Context context, ArrayList<String> list)
    {
        Gson gson=new Gson ();
        String stringGson= gson.toJson ( list);


        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences ( context );
        SharedPreferences.Editor editor= sharedPreferences.edit ( );
        editor.putString ( LIST_KEY,stringGson );
        editor.apply ();
    }
//    public static ArrayList<String>  readListInPref(Context context)
//    {
//        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences ( context );
//        String stringGson= sharedPreferences.getString ( LIST_KEY,"" );
//        Gson gson=new Gson ();
//        Type type=new TypeToken<ArrayList<String>> (  ).getType ();
//
//
//    }
}

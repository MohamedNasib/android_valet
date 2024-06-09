package com.icapp.icapp.Helpers;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icapp.icapp.Models.CarPart;
import com.icapp.icapp.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPrefs {
    Context context;

    public SharedPrefs(Context context) {
        this.context = context;
    }

    protected SharedPreferences getPreferences() {
        return context.getSharedPreferences(context.getString(R.string.shared_preference), MODE_PRIVATE);
    }

    public void set(int key, String Value) {
        SharedPreferences prefs = getPreferences();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(key), Value);
        editor.apply();
    }

    public void remove(int key) {
        SharedPreferences prefs = getPreferences();
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(context.getString(key));
        editor.apply();
    }

    public String get(int key, String defValue) {
        SharedPreferences prefs = getPreferences();
        return prefs.getString(context.getString(key), defValue);
    }

    public void clear() {
        SharedPreferences prefs = getPreferences();
        prefs.edit().clear().apply();
    }

    public ArrayList<CarPart> get(int key) {
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        SharedPreferences prefs = getPreferences();

        // creating a variable for gson.
        Gson gson = new Gson();

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = prefs.getString(context.getString(key), null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<CarPart>>() {}.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        ArrayList<CarPart> lst = gson.fromJson(json, type);

        // checking below if the array list is empty or not
        if (lst == null) {
            // if the array list is empty
            // creating a new array list.
            return new ArrayList<>();
        }
        return lst;
    }

    public void set(int key, ArrayList<CarPart> lst) {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        SharedPreferences prefs = getPreferences();

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = prefs.edit();

        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(lst);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString(context.getString(key), json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply();
    }
}


package com.example.android.sunshine.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.sunshine.utils.Network;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by fernando on 5/06/16.
 */
public class Forecast extends Fragment {
    ListView list;
    private ArrayAdapter<String> arrayadapter_forecast;
    private SharedPreferences preferences;

    private static Context context;
    public Forecast() {
    }

    public static Context getContext(){
        return context;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        Forecast.context=getActivity();
        preferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==R.id.action_refresh){
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateWeather(){
        new FetchWeatherTask().execute(preferences.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default)),
                preferences.getString(getString(R.string.pref_unit_key), getString(R.string.pref_unit_default)));
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        arrayadapter_forecast=new ArrayAdapter<String>(this.getActivity(),R.layout.list_item_forecast,R.id.list_item_forecast_textview,new ArrayList<String>());
        list=(ListView)rootView.findViewById(R.id.listview_forecast);
        list.setAdapter(arrayadapter_forecast);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(),DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT,arrayadapter_forecast.getItem(i));
                startActivity(intent);
                //Toast.makeText(getActivity(),arrayadapter_forecast.getItem(i),Toast.LENGTH_LONG).show();
            }
        });
        return rootView;
    }

    private class FetchWeatherTask extends AsyncTask<String,Void,String[]>{

        @Override
        protected String[] doInBackground(String... strings) {
            HashMap<String,String> parametros=new HashMap<String,String>();
            parametros.put("q",strings[0]);
            parametros.put("mode","json");
            parametros.put("units",strings[1]);
            parametros.put("cnt","7");
            parametros.put("appid", getString(R.string.app_key));
            //String respuesta=Network.getInfo("api.openweathermap.org", parametros);
            String[] weatherArray=weatherArray=Network.getWeatherDataFromJson(Network.getInfo("api.openweathermap.org", parametros),3,strings[1]);
            Log.v("OPENWEATHER", "" + weatherArray[0]);
            //"?q=94043&mode=json&units=metric&cnt=7&appid=b3b3a3db674c1819b6af0f15ccfc20ec"
            return weatherArray;
        }



        @Override
        protected void onPostExecute(String[] strings) {
            if(strings!=null){
                arrayadapter_forecast.clear();
                for(String weather:strings){
                    arrayadapter_forecast.add(weather);
                }
            }
        }
    }
}

package com.example.app06;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.example.app06.model.Planet;
import com.example.app06.model.PlanetListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.LinkedList;


public class FetchContent extends AsyncTask<Void,Void,Void> {


    private static final String TAG="fetchData";
    private String jsonString;
    private LinkedList<Planet> linkedList;
    private Context context;
    private ListView listView;

    public FetchContent(Context context, ListView listView){
        this.context=context;
        this.listView=listView;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            jsonString = NetworkUtils.getURLText(Constants.URL_JSON_PLANETS);
            JSONArray jsonArray= new JSONArray(jsonString);
            linkedList=new LinkedList<>();
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject =(JSONObject)jsonArray.get(i);
                Planet planet = new Planet();
                planet.setName(jsonObject.getString("name"));
                planet.setDiameter(jsonObject.getInt("diameter"));
                planet.setDistance(jsonObject.getInt("distance"));
                planet.setGravity(jsonObject.getInt("gravity"));
                planet.setImagen(NetworkUtils.getBitmapFromURL(Constants.URL_JSON+jsonObject.getString("image")));
                linkedList.add(planet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Log.i(TAG, jsonString);
        //Log.i(TAG,this.linkedList.size()+"");
        PlanetListAdapter adapter =new PlanetListAdapter(context,linkedList);
        listView.setAdapter(adapter);
    }
}

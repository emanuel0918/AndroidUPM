package com.example.app06.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app06.MainActivity;
import com.example.app06.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PlanetListAdapter extends BaseAdapter {
    private List<Planet> arrayData;
    private Context context;

    public PlanetListAdapter(Context ctx,List<Planet> planets){
        super();
        this.arrayData=planets;
        this.context=ctx;
    }
    @Override
    public int getCount() {
        return arrayData.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView ==null) {
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            convertView = layoutInflater.inflate(R.layout.planet_list_layout, null);
        }
        Planet p =arrayData.get(position);
        ((TextView)convertView.findViewById(R.id.lbl_name)).setText(p.getName());
        String diameter=context.getResources().getString(R.string.diameter_name) +": "+Integer.toString(p.getDistance());
        String gravity=context.getResources().getString(R.string.gravity_name) +": "+Integer.toString(p.getGravity());
        String distance=context.getResources().getString(R.string.distance_name) +": "+Integer.toString(p.getDistance());
        ((TextView)convertView.findViewById(R.id.lbl_gravity)).setText(gravity);
        ((TextView)convertView.findViewById(R.id.lbl_diameter)).setText(diameter);
        ((TextView)convertView.findViewById(R.id.lbl_distance)).setText(distance);
        ((ImageView)convertView.findViewById(R.id.planet_image)).setImageBitmap((p.getImagen()));

        return convertView;

    }
}

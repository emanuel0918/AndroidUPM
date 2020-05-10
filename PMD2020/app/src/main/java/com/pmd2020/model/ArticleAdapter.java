package com.pmd2020.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.pmd2020.R;

import java.util.List;

public class ArticleAdapter extends BaseAdapter {
    private List<Article> arrayData;
    private Context context;
    public ArticleAdapter(Context ctx, List<Article> list){
        super();
        this.context=context;
        this.arrayData=list;
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater layoutInflater=LayoutInflater.from(this.context);
            convertView=layoutInflater.inflate(R.layout.new_template_layout,null);
        }
        return convertView;
    }
}

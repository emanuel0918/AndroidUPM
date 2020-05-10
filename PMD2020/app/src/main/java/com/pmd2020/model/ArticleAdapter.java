package com.pmd2020.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pmd2020.R;
import com.pmd2020.utils.network.exceptions.ServerCommunicationError;

import java.nio.ByteBuffer;
import java.util.List;

public class ArticleAdapter extends BaseAdapter {
    private List<Article> arrayData;
    private Context context;
    public ArticleAdapter(Context ctx, List<Article> list){
        super();
        this.context=ctx;
        this.arrayData=list;
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
        if(convertView==null){
            LayoutInflater layoutInflater=LayoutInflater.from(this.context);
            convertView=layoutInflater.inflate(R.layout.new_template_layout,null);
        }
        Article article= arrayData.get(position);
        ((TextView)convertView.findViewById(R.id.categoryNew)).setText(article.getCategory());
        ((TextView)convertView.findViewById(R.id.titleNew)).setText(article.getTitleText());
        ((TextView)convertView.findViewById(R.id.subtitleNew)).setText(article.getSubtitleText());
        ((TextView)convertView.findViewById(R.id.abstractNew)).setText(article.getAbstractText());
        Image image = null;
        Bitmap bitmap=null;
        try {
            image =(Image) article.getImage();
        } catch (ServerCommunicationError serverCommunicationError) {
            serverCommunicationError.printStackTrace();
        }
        if(image!=null) {
        }else{
            bitmap = BitmapFactory.decodeResource(this.context.getResources(),R.drawable.ic_news_foreground);
        }
        ((ImageView) convertView.findViewById(R.id.imageView)).setImageBitmap(bitmap);
        return convertView;
    }
}

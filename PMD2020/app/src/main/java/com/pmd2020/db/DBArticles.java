package com.pmd2020.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pmd2020.model.Article;

import java.util.ArrayList;
import java.util.List;

public class DBArticles {
    public static final String TAG="DBArticles";
    private static ArticleDatabaseHelper helper;
    public static void init(Context context){
        helper=new ArticleDatabaseHelper(context);
    }
    public static List<Article> loadAllArticles(){
        List<Article> articles=new ArrayList<>();
        Article article;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor=db.query(Constants.DB_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            //_id
            long id =cursor.getLong(0);
            //timestamp
            long datetime=cursor.getLong(1);
            //title
            String title=cursor.getString(2);
            //subtitle
            String subtitle=cursor.getString(3);
            //category
            String category = cursor.getString(4);
            //abstract
            String abstractText=cursor.getString(5);
            //body
            String body=cursor.getString(6);
            //image
            byte[] imagen_array;
            try {
                imagen_array = cursor.getBlob(7);
            }catch (Exception imageException){

            }
            //
            //
            article=new Article(category,title,abstractText,body,subtitle,"1");
            articles.add(article);
            cursor.moveToNext();
        }
        return articles;
    }
    public static void saveArticle(Article article,long timestamp){
        SQLiteDatabase db =helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Constants.DB_TABLE_FIELD_TITLE, article.getTitleText());
        values.put(Constants.DB_TABLE_FIELD_SUBTITLE,article.getSubtitleText());
        values.put(Constants.DB_TABLE_FIELD_ABSTRACT,article.getAbstractText());
        values.put(Constants.DB_TABLE_FIELD_CATEGORY,article.getCategory());
        values.put(Constants.DB_TABLE_FIELD_BODY,article.getBodyText());
        values.put(Constants.DB_TABLE_FIELD_TIME,timestamp);
        byte imagen_array[];
        try{
            imagen_array=article.getImage().getImage().getBytes();
            //values.put(Constants.DB_TABLE_FIELD_IMAGE,imagen_array);
        }catch (Exception e){
            Log.i(TAG,"Error insertando la imagen");
        }
        long insertId;
        insertId=db.insert(Constants.DB_TABLE_NAME,null,values);
        Log.i(TAG,"Article created");

    }
}

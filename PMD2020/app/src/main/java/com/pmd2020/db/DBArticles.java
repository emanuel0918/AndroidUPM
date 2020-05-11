package com.pmd2020.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import com.pmd2020.model.Article;
import com.pmd2020.model.Image;
import com.pmd2020.utils.SerializationUtils;
import com.pmd2020.utils.network.exceptions.ServerCommunicationError;

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
        int columnIndex;
        while(!cursor.isAfterLast()){
            //reset columnIndex
            columnIndex=0;
            //_id
            long id =cursor.getLong(columnIndex);
            columnIndex++;
            //title
            String title=cursor.getString(columnIndex);
            columnIndex++;
            //subtitle
            String subtitle=cursor.getString(columnIndex);
            columnIndex++;
            //category
            String category = cursor.getString(columnIndex);
            columnIndex++;
            //abstract
            String abstractText=cursor.getString(columnIndex);
            columnIndex++;
            //body
            String body=cursor.getString(columnIndex);
            columnIndex++;
            //image
            String b64Image=cursor.getString(columnIndex);
            columnIndex++;
            //description
            String description = cursor.getString(columnIndex);
            article=new Article(category,title,abstractText,body,subtitle,"1");
            try {
                article.addImage(b64Image,description);
            } catch (Exception e) {
            }
            articles.add(article);
            cursor.moveToNext();
        }
        return articles;
    }
    public static void saveArticle(Article article){
        SQLiteDatabase db =helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Constants.DB_TABLE_FIELD_TITLE, article.getTitleText());
        values.put(Constants.DB_TABLE_FIELD_SUBTITLE,article.getSubtitleText());
        values.put(Constants.DB_TABLE_FIELD_ABSTRACT,article.getAbstractText());
        values.put(Constants.DB_TABLE_FIELD_CATEGORY,article.getCategory());
        values.put(Constants.DB_TABLE_FIELD_BODY,article.getBodyText());
        String imagenString="";
        String description="";
        try{
            Image image=article.getImage();
            imagenString=image.getImage();
            description=image.getDescription();
        }catch (Exception e){

            Log.i(TAG,"Error insertando la imagen");
        }
        values.put(Constants.DB_TABLE_FIELD_IMAGE,imagenString);
        values.put(Constants.DB_TABLE_FIELD_DESCRIPTION,description);
        long insertId;
        insertId=db.insert(Constants.DB_TABLE_NAME,null,values);
        Log.i(TAG,"Article created");

    }
}

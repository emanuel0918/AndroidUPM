package com.example.appnewspaper;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.support.annotation.RequiresApi;

import com.example.appnewspaper.model.Article;
import com.example.appnewspaper.utils.network.exceptions.ServerCommunicationError;

import java.util.concurrent.ExecutionException;

import static com.example.appnewspaper.utils.SerializationUtils.base64StringToImg;

public class activity_article extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        //Hacer peticion al servidor para coger el articulo

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.loginArticle);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goLogin = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(goLogin);
            }
        });

        AsyncTask<Void, Void, Article> p = new LoadArticleTask().execute();
        Article article = null;
        try {
            article = p.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("UPDATE DATE " + article.getAttributes().contains("update_date"));

        try {
            Spanned htmlAsSpanned;
            ConstraintLayout articleScreen = (ConstraintLayout) findViewById(R.id.layaoutArticle);

            ImageView imageView = (ImageView) findViewById(R.id.imageViewArticle);
            imageView.setImageBitmap(base64StringToImg(article.getImage().getImage()));
            TextView category = (TextView) findViewById(R.id.categoryArticle);
            htmlAsSpanned = Html.fromHtml(article.getCategory());
            category.setText(htmlAsSpanned);
            TextView title = (TextView) findViewById(R.id.titleArticleSelect);
            htmlAsSpanned = Html.fromHtml(article.getTitleText());
            title.setText(htmlAsSpanned);
            TextView subTitle = (TextView) findViewById(R.id.subtitleArticle2);
            htmlAsSpanned = Html.fromHtml(article.getAbstractText());
            subTitle.setText(htmlAsSpanned);
            TextView body = (TextView) findViewById(R.id.bodyArticle2);
             htmlAsSpanned = Html.fromHtml(article.getBodyText());
            body.setText(htmlAsSpanned);



        } catch (ServerCommunicationError serverCommunicationError) {
            serverCommunicationError.printStackTrace();
        }


    }
}

package com.example.appnewspaper;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.appnewspaper.model.Article;
import com.example.appnewspaper.utils.network.exceptions.ServerCommunicationError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.ExecutionException;

import static com.example.appnewspaper.utils.SerializationUtils.base64StringToImg;

public class activity_article extends Activity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        //Hacer peticion al servidor para coger el articulo

        FloatingActionButton fab = findViewById(R.id.loginArticle);
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
            ConstraintLayout articleScreen = findViewById(R.id.layaoutArticle);

            ImageView imageView = findViewById(R.id.imageViewArticle);
            imageView.setImageBitmap(base64StringToImg(article.getImage().getImage()));
            TextView category = findViewById(R.id.categoryArticle);
            htmlAsSpanned = Html.fromHtml(article.getCategory());
            category.setText(htmlAsSpanned);
            TextView title = findViewById(R.id.titleArticleSelect);
            htmlAsSpanned = Html.fromHtml(article.getTitleText());
            title.setText(htmlAsSpanned);
            TextView subTitle = findViewById(R.id.subtitleArticle2);
            htmlAsSpanned = Html.fromHtml(article.getAbstractText());
            subTitle.setText(htmlAsSpanned);
            TextView body = findViewById(R.id.bodyArticle2);
             htmlAsSpanned = Html.fromHtml(article.getBodyText());
            body.setText(htmlAsSpanned);



        } catch (ServerCommunicationError serverCommunicationError) {
            serverCommunicationError.printStackTrace();
        }


    }
}

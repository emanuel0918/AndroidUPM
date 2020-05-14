package com.example.appnewspaper;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appnewspaper.LoadArticleTask;
import com.example.appnewspaper.MainActivity;
import com.example.appnewspaper.R;
import com.example.appnewspaper.model.Article;
import com.example.appnewspaper.utils.network.exceptions.ServerCommunicationError;

import java.util.concurrent.ExecutionException;

import static com.example.appnewspaper.utils.SerializationUtils.base64StringToImg;

public class activity_article_after_login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_after_login);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.logoutAfterLogin);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goLogin = new Intent(getBaseContext(), MainActivity.class);
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

        //System.out.println(article);

        try {
            Spanned htmlAsSpanned;
            ImageView imageView = (ImageView) findViewById(R.id.imageView4);
            imageView.setImageBitmap(base64StringToImg(article.getImage().getImage()));
            TextView category = (TextView) findViewById(R.id.categoryArticleAferLogin);
            htmlAsSpanned = Html.fromHtml(article.getCategory());
            category.setText(htmlAsSpanned);
            TextView title = (TextView) findViewById(R.id.titleArticleAferLogin);
            htmlAsSpanned = Html.fromHtml(article.getTitleText());
            title.setText(htmlAsSpanned);
            TextView subTitle = (TextView) findViewById(R.id.subtitleArticleAferLogin);
            htmlAsSpanned = Html.fromHtml(article.getAbstractText());
            subTitle.setText(htmlAsSpanned);
            TextView body = (TextView) findViewById(R.id.bodyArticleAferLogin);
            htmlAsSpanned = Html.fromHtml(article.getBodyText());
            body.setText(htmlAsSpanned);

        } catch (ServerCommunicationError serverCommunicationError) {
            serverCommunicationError.printStackTrace();
        }

    }
}

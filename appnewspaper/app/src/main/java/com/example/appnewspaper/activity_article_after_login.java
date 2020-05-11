package com.example.appnewspaper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.ExecutionException;

import static com.example.appnewspaper.utils.SerializationUtils.base64StringToImg;

public class activity_article_after_login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_after_login);

        FloatingActionButton fab = findViewById(R.id.logoutAfterLogin);
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
            ImageView imageView = findViewById(R.id.imageView4);
            imageView.setImageBitmap(base64StringToImg(article.getImage().getImage()));
            TextView category = findViewById(R.id.categoryArticleAferLogin);
            htmlAsSpanned = Html.fromHtml(article.getCategory());
            category.setText(htmlAsSpanned);
            TextView title = findViewById(R.id.titleArticleAferLogin);
            htmlAsSpanned = Html.fromHtml(article.getTitleText());
            title.setText(htmlAsSpanned);
            TextView subTitle = findViewById(R.id.subtitleArticleAferLogin);
            htmlAsSpanned = Html.fromHtml(article.getAbstractText());
            subTitle.setText(htmlAsSpanned);
            TextView body = findViewById(R.id.bodyArticleAferLogin);
            htmlAsSpanned = Html.fromHtml(article.getBodyText());
            body.setText(htmlAsSpanned);

        } catch (ServerCommunicationError serverCommunicationError) {
            serverCommunicationError.printStackTrace();
        }

    }
}

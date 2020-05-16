package com.appnewspaper;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.appnewspaper.model.Article;
import com.appnewspaper.utils.network.ModelManager;
import com.appnewspaper.utils.network.exceptions.ServerCommunicationError;

public class AddArticleTask extends AsyncTask<Void, Void, Article> {
    public static Article article;
    private MainActivity activity;


    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }
    @Override
    protected Article doInBackground(Void... voids) {

        try {
            if (ModelManager.isConnected()) {
                ModelManager.saveArticle(article);
            }
        } catch (ServerCommunicationError serverCommunicationError) {
            serverCommunicationError.printStackTrace();
        }
        return article;
    }

    @Override
    protected void onPostExecute(Article article) {
        ((MainActivity)this.activity).someDialog();
        super.onPostExecute(article);

    }
}

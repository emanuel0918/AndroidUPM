package com.appnewspaper;

import android.os.AsyncTask;

import com.appnewspaper.model.Article;
import com.appnewspaper.utils.network.ModelManager;
import com.appnewspaper.utils.network.exceptions.ServerCommunicationError;

public class AddArticleTask extends AsyncTask<Void, Void, Article> {
    public static Article article;

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

}

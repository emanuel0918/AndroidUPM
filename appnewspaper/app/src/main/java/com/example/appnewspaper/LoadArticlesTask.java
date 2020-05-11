package com.example.appnewspaper;

import android.os.AsyncTask;
import android.util.Log;

import com.example.appnewspaper.model.Article;
import com.example.appnewspaper.utils.network.ModelManager;
import com.example.appnewspaper.utils.network.exceptions.AuthenticationError;
import com.example.appnewspaper.utils.network.exceptions.ServerCommunicationError;

import java.util.List;


public class LoadArticlesTask extends AsyncTask<Void, Void, List<Article>> {

    private static final String TAG = "LoadArticlesTask";
    private String strIdUser;
    private String strApiKey;
    private String strIdAuthUser;

    List<Article> res = null;

    @Override
    protected List<Article> doInBackground(Void... voids) {

        //ModelManager uses singleton pattern, connecting once per app execution in enough

        try {
            // obtain 6 articles from offset 0
            if (ModelManager.isConnected()) {
                res = ModelManager.getArticles(6, 0);
            } else {
                ModelManager.login("DEV_TEAM_07", "89423");
                res = ModelManager.getArticles(6, 0);
                //Article article2 = ModelManager.getArticle(145);

            }
            for (Article article : res) {
                // We print articles in Log
                Log.i(TAG, String.valueOf(article));
            }
        } catch (ServerCommunicationError e) {
            Log.e(TAG, e.getMessage());
        } catch (AuthenticationError authenticationError) {
            authenticationError.printStackTrace();
        }

        return res;

    }


    @Override
    protected void onPostExecute(List<Article> articles) {
        super.onPostExecute(articles);
    }
}


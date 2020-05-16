package com.appnewspaper;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.appnewspaper.model.Article;
import com.appnewspaper.utils.SerializationUtils;
import com.appnewspaper.utils.network.ModelManager;
import com.appnewspaper.utils.network.exceptions.ServerCommunicationError;

public class AddArticleTask extends AsyncTask<Void, Void, Article> {
    private Article article;
    private MainActivity activity;


    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public void setArticle(Article article) {
        this.article = new Article(
                article.getCategory(),
                article.getTitleText(),
                article.getAbstractText(),
                article.getBodyText(),
                article.getSubtitleText(),
                article.getIdUser()+"");
        try {
            this.article.addImage(article.getImage().getImage(),article.getImage().getDescription());

        }catch (Exception e){
            try {
                this.article.addImage(SerializationUtils.IMG_STRING, "description");
            }catch (Exception ee){
            }

        }
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

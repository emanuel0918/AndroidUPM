package com.example.appnewspaper;

import android.os.AsyncTask;
import android.util.Log;

import com.example.appnewspaper.model.Article;
import com.example.appnewspaper.utils.network.ModelManager;
import com.example.appnewspaper.utils.network.exceptions.AuthenticationError;
import com.example.appnewspaper.utils.network.exceptions.ServerCommunicationError;

import static com.example.appnewspaper.utils.Logger.TAG;

public class DeleteArticleTask extends AsyncTask<Void, Void, Integer> {
    public static int id;

    /**
     * @param voids
     * @return
     */
    @Override
    protected Integer doInBackground(Void... voids) {
        int isDeleted = 0;
        //ModelManager uses singleton pattern, connecting once per app execution in enough
        try {
            if (ModelManager.isConnected()) {
                //ModelManager.login("DEV_TEAM_07", "89423");
                ModelManager.deleteArticle(id);
            }
        } catch (
                ServerCommunicationError e) {
            Log.e(TAG, e.getMessage());
        }
        return isDeleted;
    }
}

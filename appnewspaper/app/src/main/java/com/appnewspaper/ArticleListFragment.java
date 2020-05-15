package com.appnewspaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.appnewspaper.db.DBArticles;
import com.appnewspaper.model.Article;
import com.appnewspaper.model.ArticleAdapter;
import com.appnewspaper.utils.network.ModelManager;
import com.appnewspaper.utils.network.exceptions.ServerCommunicationError;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ArticleListFragment extends Fragment {
    private boolean session;


    private ListView newListView;
    private SharedPreferences rememberMe;

    public ArticleListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // BD
        DBArticles.init(getActivity().getApplicationContext());;
        //Sesion
        rememberMe = getActivity().getBaseContext().getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        Map<String, ?> map = rememberMe.getAll();
        session = (Boolean) map.get("session");
        return inflater.inflate(R.layout.fragment_new_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        newListView=(ListView)getView().findViewById(R.id.news_list);
        List<Article> articles=null;
        Article a;
        try {
            AsyncTask<Void, Void, List<Article>> p = new LoadArticlesTask().execute();
            //new FetchDataTask().execute("http://sanger.dia.fi.upm.es/pmd-task/articles");
            //new FetchDataTask().execute("https://DEV_TEAM_07:89423@sanger.dia.fi.upm.es/pmd-task/");
             articles = p.get();
        } catch (Exception er) {

        }
        if(session){
            MyAdapter articleAdapter= new MyAdapter(getActivity().getApplicationContext(),(ArrayList<Article>)articles);
            newListView.setAdapter(articleAdapter);
            super.onViewCreated(view, savedInstanceState);
        }else{
            ArticleAdapter articleAdapter= new ArticleAdapter(getActivity().getApplicationContext(),articles);
            newListView.setAdapter(articleAdapter);
            super.onViewCreated(view, savedInstanceState);

        }
    }

}
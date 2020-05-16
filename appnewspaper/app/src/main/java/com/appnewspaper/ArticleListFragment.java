package com.appnewspaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
    private boolean stayLogged;


    private ListView newListView;

    public ArticleListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // BD
        DBArticles.init(getActivity().getApplicationContext());
        SharedPreferences rememberMe = getActivity().getBaseContext().getSharedPreferences(
                "rememberMe", Context.MODE_PRIVATE);
        Map<String, ?> map = rememberMe.getAll();
        Boolean mantenerSesion;
        try {
            mantenerSesion = (Boolean) map.get("stayLogged");
        }catch (Exception e){
            mantenerSesion=null;
        }
        Boolean sesion1;
        try{
            sesion1=(Boolean)map.get("session");
        }catch (Exception e){
            sesion1=false;
        }
        if (mantenerSesion == null) {
            SharedPreferences.Editor editorTwo = rememberMe.edit();
            editorTwo = rememberMe.edit();
            editorTwo.putBoolean("session", false);
            session=false;
            editorTwo.commit();
        }else{
            stayLogged=mantenerSesion;
            session=sesion1;
            if(!sesion1) {
                session = mantenerSesion;
            }
        }
        //
        return inflater.inflate(R.layout.fragment_new_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        newListView=(ListView)getView().findViewById(R.id.news_list);
        ArrayList<String> categorias_por_filtrar=new ArrayList<>(); //Agregamos las categorias del strings.xml
        List<Article> articles=null;
        List<Article> articles_non_filtered=null;
        Article a;
        try {
            AsyncTask<Void, Void, List<Article>> p = new LoadArticlesTask().execute();
            //new FetchDataTask().execute("http://sanger.dia.fi.upm.es/pmd-task/articles");
            //new FetchDataTask().execute("https://DEV_TEAM_07:89423@sanger.dia.fi.upm.es/pmd-task/");
             articles_non_filtered = p.get();
             articles=new ArrayList<>();
             boolean filtrar=false;
             int filtro=((MainActivity)getActivity()).filter;
             switch (filtro) {
                 case 0:
                     categorias_por_filtrar.add("National");
                     categorias_por_filtrar.add("Nacional");
                     filtrar=true;
                     break;
                 case 1:
                     categorias_por_filtrar.add("Economy");
                     categorias_por_filtrar.add("Economia");
                     filtrar=true;
                     break;
                 case 2:
                     categorias_por_filtrar.add("Sports");
                     categorias_por_filtrar.add("Deportes");
                     filtrar=true;
                     break;
                 case 3:
                     categorias_por_filtrar.add("Technology");
                     categorias_por_filtrar.add("Tecnologia");
                     filtrar=true;
                     break;
                 default:
                     break;
             }
            if(filtrar){
                 for(Article article_category:articles_non_filtered){
                     for(String category_resource:categorias_por_filtrar){
                         if(article_category.getCategory().equals(category_resource)){
                             articles.add(article_category);
                         }
                     }
                 }
             }else {
                 articles=articles_non_filtered; //Opcion para no filtrar
             }

        } catch (Exception er) {

        }
        if(session){
            FloatingActionButton publishArticlefloatingButton;
            publishArticlefloatingButton= (FloatingActionButton)getView().findViewById((R.id.publish_new_floating_btn));
            publishArticlefloatingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    publishArticle();
                }
            });
            publishArticlefloatingButton.show();
            MyAdapter articleAdapter= new MyAdapter(getActivity().getApplicationContext(),(ArrayList<Article>)articles);
            //IMPORTANTE
            articleAdapter.setActivity(getActivity());
            //
            newListView.setAdapter(articleAdapter);
            super.onViewCreated(view, savedInstanceState);
        }else{
            FloatingActionButton publishArticlefloatingButton;
            publishArticlefloatingButton= (FloatingActionButton)getView().findViewById((R.id.publish_new_floating_btn));
            publishArticlefloatingButton.hide();
            ArticleAdapter articleAdapter= new ArticleAdapter(getActivity().getApplicationContext(),articles);
            newListView.setAdapter(articleAdapter);
            super.onViewCreated(view, savedInstanceState);

        }
    }

    private void publishArticle() {
        ((MainActivity)getActivity()).publishArticle();
    }

}
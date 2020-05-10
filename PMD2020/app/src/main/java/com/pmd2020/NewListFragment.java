package com.pmd2020;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pmd2020.model.Article;
import com.pmd2020.model.ArticleAdapter;
import com.pmd2020.utils.network.ModelManager;
import com.pmd2020.utils.network.exceptions.ServerCommunicationError;

import java.util.LinkedList;
import java.util.List;

public class NewListFragment extends Fragment {
    private ListView newListView;

    public NewListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        newListView=(ListView)getView().findViewById(R.id.news_list);
        List<Article> articles=null;
        Article a;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                articles= ModelManager.getArticles();
            }
        } catch (ServerCommunicationError serverCommunicationError) {
            serverCommunicationError.printStackTrace();
            articles= new LinkedList<>();
            a = new Article("category","title","abstract","body","subtitle","3");
            articles.add(a);
            a = new Article("category","title","abstract","body","subtitle","3");
            articles.add(a);
        }
        ArticleAdapter articleAdapter= new ArticleAdapter(getActivity().getApplicationContext(),articles);
        newListView.setAdapter(articleAdapter);
        super.onViewCreated(view, savedInstanceState);
    }
}
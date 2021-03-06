package com.pmd2020;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.pmd2020.db.DBArticles;
import com.pmd2020.model.Article;
import com.pmd2020.utils.SerializationUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class PublishNewFragment extends Fragment {

    public final int REQUEST_OPEN_IMAGE =203;
    //
    private String b64Image;
    private ImageView imageView;
    private Button publishArticleButton;

    public PublishNewFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // BD
        //DBArticles.init(getActivity().getApplicationContext());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_publish_new, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //
        imageView=(ImageView)getView().findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });
        publishArticleButton =(Button)getView().findViewById(R.id.publish_new_btn);
        publishArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishNew();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void publishNew() {
        ((Button)getView().findViewById(R.id.publish_new_btn)).setEnabled(false);
        //
        //
        DialogInterface.OnClickListener clickListener=null;
        //
        //
        FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
        SomeDialog builder=new SomeDialog();
        if(!(((EditText)getView().findViewById(R.id.title)).getText().toString().equals("")) &&
                !(((EditText)getView().findViewById(R.id.subtitle)).getText().toString().equals("")) &&
                !(((EditText)getView().findViewById(R.id.category)).getText().toString().equals("")) &&
                !(((EditText)getView().findViewById(R.id.abstrac)).getText().toString().equals("")) &&
                !(((EditText)getView().findViewById(R.id.description)).getText().toString().equals("")) &&
                !(((EditText)getView().findViewById(R.id.body)).getText().toString().equals(""))){
            //CREATED
            String new_published="";
            try {
                String title=((EditText)getView().findViewById(R.id.title)).getText().toString();
                String subtitle=((EditText)getView().findViewById(R.id.subtitle)).getText().toString();
                String category=((EditText)getView().findViewById(R.id.category)).getText().toString();
                String abstrac=((EditText)getView().findViewById(R.id.abstrac)).getText().toString();
                String body=((EditText)getView().findViewById(R.id.body)).getText().toString();
                String description=((EditText)getView().findViewById(R.id.description)).getText().toString();
                String idUser="7";
                Article article=new Article(category,title,abstrac,body,subtitle,idUser);
                try {
                    article.addImage(b64Image, description);
                    //article.setImage(new Image());
                }catch (Exception ee){

                }
                //long timestamp=0;
                //set datetime
                DBArticles.saveArticle(article);
                new_published = getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.new_published
                        );
                builder.setTitle(new_published);
                builder.setMessage("");
                clickListener=new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                };
            }catch(Exception e){

            }finally {
                builder.setTitle(new_published);
                builder.setMessage("");
            }
        }else{

            String missingData=getActivity().getApplicationContext()
                    .getResources().getString(
                            R.string.missing_data
                    );
            String missingValue="";
            if(((EditText)getView().findViewById(R.id.title)).getText().toString().equals("")){
                missingValue = getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.title
                        );
            }else if(((EditText)getView().findViewById(R.id.subtitle)).getText().toString().equals("")){
                missingValue = getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.subtitle
                        );

            }else if(((EditText)getView().findViewById(R.id.category)).getText().toString().equals("")){
                missingValue = getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.category
                        );

            }else if(((EditText)getView().findViewById(R.id.abstrac)).getText().toString().equals("")){
                missingValue = getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.abstrac
                        );

            }else if(((EditText)getView().findViewById(R.id.body)).getText().toString().equals("")){
                missingValue = getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.body
                        );

            }else if(((EditText)getView().findViewById(R.id.description)).getText().toString().equals("")){
                missingValue = getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.description
                        );

            }
            builder.setTitle(missingData);
            builder.setMessage(missingValue);
        }

        String accept_btn=getActivity().getApplicationContext()
                .getResources().getString(
                        R.string.ok
                );

        builder.setPositiveButton(accept_btn,clickListener);
        builder.show(transaction,"dialog");
        //
        //
        publishArticleButton.setEnabled(true);
    }

    private void finish() {
        String tag=getActivity().getBaseContext().getResources().getString(R.string.list_news);
        NewListFragment fragment = new NewListFragment();
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame,fragment,tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void getImage() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES,new String[]{"image/jpg","image/png"});
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_OPEN_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_OPEN_IMAGE:
                if(resultCode== Activity.RESULT_OK){
                    InputStream stream=null;
                    try{
                        stream=getActivity()
                                .getApplicationContext()
                                .getContentResolver()
                                .openInputStream(data.getData());
                        Bitmap bitmap= BitmapFactory.decodeStream(stream);
                        imageView.setImageBitmap(bitmap);
                        b64Image = SerializationUtils.encodeImage(bitmap);
                    }catch (Exception e){

                    }finally {
                        if(stream!=null){
                            try {
                                stream.close();
                            } catch (Exception e) {

                            }
                        }
                    }
                }
                break;
        }
    }

}
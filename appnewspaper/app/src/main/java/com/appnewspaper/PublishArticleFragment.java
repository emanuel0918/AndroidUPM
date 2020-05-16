package com.appnewspaper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;


import com.appnewspaper.db.DBArticles;
import com.appnewspaper.model.Article;
import com.appnewspaper.utils.SerializationUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

public class PublishArticleFragment extends Fragment {
    private boolean session;
    private boolean stayLogged;

    //
    private SharedPreferences rememberMe;

    public final int REQUEST_OPEN_IMAGE =203;
    //
    private String b64Image;
    private ImageView imageView;
    private ImageButton imageButton;
    private Button publishArticleButton;
    private Button cancelButton;
    private Bitmap bitmap;
    //

    public PublishArticleFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // BD
        //DBArticles.init(getActivity().getApplicationContext());
        //Sesion
        rememberMe = getActivity().getBaseContext().getSharedPreferences(
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
        imageButton=(ImageButton)getView().findViewById(R.id.image_btn_add_image);
        imageButton.setOnClickListener(new View.OnClickListener() {
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
        cancelButton =(Button)getView().findViewById(R.id.publish_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
                SomeDialog builder=new SomeDialog();
                builder.setOpcion(SomeDialog.MULTIPLE);
                builder.setTitle(getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.warning
                        ));
                builder.setMessage(getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.discard_changes
                        ));
                builder.setNegativeButton(getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.cancel
                        ),null);
                builder.setPositiveButton(getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.accept
                        ), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reload_articles();
                    }
                });
                builder.show(transaction,"dialog");
            }
        });

        Spinner spinnerCategory = (Spinner) getView().findViewById(R.id.category_new);
        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(
                getActivity().getBaseContext(), R.array.category, android.R.layout.simple_spinner_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategory);
        super.onViewCreated(view, savedInstanceState);
    }

    private void publishNew() {
        ((Button)getView().findViewById(R.id.publish_new_btn)).setEnabled(false);
        //
        String required_data =getActivity().getBaseContext().getResources().getString(R.string.required_data);
        //
        FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
        SomeDialog builder=new SomeDialog();
        DialogInterface.OnClickListener clickListener = null;
        EditText title_form=((EditText)getView().findViewById(R.id.title));
        EditText subtitle_form=((EditText)getView().findViewById(R.id.subtitle));
        Spinner categorySpinner = (Spinner) getView().findViewById(R.id.category_new);
        EditText abstrac_form=((EditText)getView().findViewById(R.id.abstrac));
        EditText description_form=((EditText)getView().findViewById(R.id.description));
        EditText body_form=((EditText)getView().findViewById(R.id.body));
        if(!((title_form.getText().toString().equals("")) &&
                !(subtitle_form.getText().toString().equals(""))) &&
                !(abstrac_form.getText().toString().equals("")) &&
                !(description_form.getText().toString().equals("")) &&
                !(body_form.getText().toString().equals(""))){
            //CREATED
            String new_published="";
            try {
                String title=title_form.getText().toString();
                String subtitle=subtitle_form.getText().toString();
                String category = String.valueOf(categorySpinner.getSelectedItem());
                String abstrac=abstrac_form.getText().toString();
                String body=body_form.getText().toString();
                String description=description_form.getText().toString();
                Map<String, ?> map = rememberMe.getAll();
                String userId = (String) map.get("idUser");
                Article article=new Article(category,title,abstrac,body,subtitle,userId);
                b64Image=null;
                try {
                    b64Image = SerializationUtils.encodeImage(bitmap);
                    //article.addImage(b64Image, description);

                    //article.setImage(new Image());
                }catch (Exception ee){
                    b64Image="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";

                }
                article.addImage(b64Image, description);
                //long timestamp=0;
                //set datetime


                // CREAR SOLO EN LA BD
                try {
                    DBArticles.saveArticle(article);
                }catch (Exception e){

                }

                //CREAR EN EL WEB SERVICE
                try {

                    AddArticleTask addArticleTask = (AddArticleTask) new AddArticleTask();
                    addArticleTask.article = article;
                    addArticleTask.execute();
                }catch (Exception e){

                }

                //--------------------------------------------------
                new_published = getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.new_published
                        );
                builder.setTitle(new_published);
                builder.setMessage("");
                clickListener=new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reload_articles();
                    }
                };
            }catch(Exception e){

            }
        }else{
            title_form.setError(null);
            if(title_form.getText().toString().equals("")){
                title_form.setError(required_data,null);
            }
            subtitle_form.setError(null);
            if(subtitle_form.getText().toString().equals("")){
                subtitle_form.setError(required_data,null);
            }
            abstrac_form.setError(null);
            if(abstrac_form.getText().toString().equals("")){
                abstrac_form.setError(required_data,null);
            }
            body_form.setError(null);
            if(body_form.getText().toString().equals("")){
                body_form.setError(required_data,null);
            }
            description_form.setError(null);
            if(description_form.getText().toString().equals("")){
                description_form.setError(required_data,null);
            }

            String missingData=getActivity().getApplicationContext()
                    .getResources().getString(
                            R.string.missing_data
                    );
            String missingValue="";
            if(title_form.getText().toString().equals("")){
                missingValue = getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.title
                        );
            }else if(subtitle_form.getText().toString().equals("")){
                missingValue = getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.subtitle
                        );

            }else if(abstrac_form.getText().toString().equals("")){
                missingValue = getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.abstrac
                        );

            }else if(body_form.getText().toString().equals("")){
                missingValue = getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.body
                        );

            }else if(description_form.getText().toString().equals("")){
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

    private void reload_articles() {
        ((MainActivity)getActivity()).reload_articles();
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
                        bitmap= BitmapFactory.decodeStream(stream);
                        imageView.setImageBitmap(bitmap);
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
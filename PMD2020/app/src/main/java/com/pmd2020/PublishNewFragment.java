package com.pmd2020;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.pmd2020.db.DBArticles;
import com.pmd2020.model.Article;
import com.pmd2020.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class PublishNewFragment extends Fragment {

    public final int REQUEST_OPEN_IMAGE =203;
    //
    private byte[] imagen;
    private ImageView imageView;
    private Button publishArticle;

    public PublishNewFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        publishArticle=(Button)getView().findViewById(R.id.publish_new_btn);
        publishArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishNew();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void publishNew() {
        FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
        SomeDialog builder=new SomeDialog();
        if(!(((EditText)getView().findViewById(R.id.title)).getText().toString().equals("")) &&
                !(((EditText)getView().findViewById(R.id.subtitle)).getText().toString().equals("")) &&
                !(((EditText)getView().findViewById(R.id.category)).getText().toString().equals("")) &&
                !(((EditText)getView().findViewById(R.id.abstrac)).getText().toString().equals("")) &&
                !(((EditText)getView().findViewById(R.id.body)).getText().toString().equals(""))){
            //CREATED
            String new_published="";
            try {
                String title=((EditText)getView().findViewById(R.id.title)).getText().toString();
                String subtitle=((EditText)getView().findViewById(R.id.subtitle)).getText().toString();
                String category=((EditText)getView().findViewById(R.id.category)).getText().toString();
                String abstrac=((EditText)getView().findViewById(R.id.abstrac)).getText().toString();
                String body=((EditText)getView().findViewById(R.id.body)).getText().toString();
                String idUser="7";
                Article article=new Article(category,title,abstrac,body,subtitle,idUser);
                //article.setImage(new Image());
                long timestamp=0;
                //set datetime
                DBArticles.saveArticle(article,timestamp);
                new_published = getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.new_published
                        );
                builder.setTitle(new_published);
                builder.setMessage("");
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

            }
            builder.setTitle(missingData);
            builder.setMessage(missingValue);
        }

        String accept_btn=getActivity().getApplicationContext()
                .getResources().getString(
                        R.string.ok
                );

        builder.setPositiveButton(accept_btn,null);
        builder.show(transaction,"dialog");
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
                        try{
                            imagen=readAllBytes(stream);
                        }catch (Exception ioe){

                        }
                        Bitmap bitmap= BitmapFactory.decodeStream(stream);
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

    private byte[] readAllBytes(InputStream is) {
        byte[] bytes=null;
        byte[] data = new byte[16384];
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ArrayList<Byte> array=new ArrayList<>();
        int nRead;
        try {
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
        }catch (Exception e){

        }
        return  buffer.toByteArray();
    }
}
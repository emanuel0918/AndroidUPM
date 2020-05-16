package com.testlistview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.appnewspaper.AddArticleTask;

import com.appnewspaper.LoadArticleTask;
import com.appnewspaper.MainActivity;
import com.appnewspaper.MainActivityAfterLogin;

import com.appnewspaper.R;
import com.appnewspaper.model.Article;
import com.appnewspaper.model.Image;
import com.appnewspaper.utils.network.exceptions.ServerCommunicationError;



import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.appnewspaper.utils.SerializationUtils.base64StringToImg;
import static com.appnewspaper.utils.SerializationUtils.imgToBase64String;

public class Modify_article_after_login extends AppCompatActivity {
    private boolean stayLogged;
    private boolean session;
    private static final int PICK_IMAGE = 100;
    ImageView imageView;
    Article newModifArticle;
    Article article = null;
    SharedPreferences rememberMe;
    Bitmap bitmap;
    String thumbail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_article_after_login);
        //Sesion
        rememberMe = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
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

        Button chooseImagen =(Button) findViewById(R.id.chooseImagen);
        chooseImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });


        AsyncTask<Void, Void, Article> p = new LoadArticleTask().execute();
        try {
            article = p.get();
            Spanned htmlAsSpanned;
            imageView = (ImageView)findViewById(R.id.imageModify);
            thumbail = article.getImage().getImage();
            imageView.setImageBitmap(base64StringToImg(article.getImage().getImage()));
            bitmap = base64StringToImg(article.getImage().getImage());
            TextView title = (TextView) findViewById(R.id.titleModify);
            htmlAsSpanned = Html.fromHtml(article.getTitleText());
            title.setText(htmlAsSpanned);
            TextView subTitle = (TextView) findViewById(R.id.subtitleModify);
            htmlAsSpanned = Html.fromHtml(article.getAbstractText());
            subTitle.setText(htmlAsSpanned);
            TextView abstractText = (TextView)findViewById(R.id.abstractModify);
            htmlAsSpanned = Html.fromHtml(article.getAbstractText());
            abstractText.setText(htmlAsSpanned);
            TextView body = (TextView)findViewById(R.id.bodyModify);
            htmlAsSpanned = Html.fromHtml(article.getBodyText());
            body.setText(htmlAsSpanned);

            Spinner spinnerCategory = (Spinner)findViewById(R.id.categoryModify);

            String[] opciones = setcategory(article.getCategory());
            ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
            spinnerCategory.setAdapter(adapterCategory);


            //
            Button saveArticleModify = (Button)findViewById(R.id.saveModify);
            saveArticleModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Vamos a coger los valores para modificar el articulo
                    TextView title = (TextView)findViewById(R.id.titleModify);
                    String titleAccept = String.valueOf(title.getText());
                    //System.out.println("Titulo cambiado " + titleAccept);
                    TextView subTitle = (TextView) findViewById(R.id.subtitleModify);
                    String subTitleAccept = String.valueOf(subTitle.getText());
                    //System.out.println("Subtitulo cambiado " + t);
                    TextView abstractText = (TextView)findViewById(R.id.abstractModify);
                    String abstractAccept = String.valueOf(abstractText.getText());
                    //System.out.println("Abstract cambiado " + abstractAccept);
                    TextView body = (TextView)findViewById(R.id.bodyModify);
                    String bodyAccept = String.valueOf(body.getText());
                    //System.out.println("Body cambiado " + bodyAccept);
                    Spinner spinnerCategory = (Spinner) findViewById(R.id.categoryModify);
                    String addCategory = String.valueOf(spinnerCategory.getSelectedItem());
                    //System.out.println("Category cambiado " + addCategory);
                    rememberMe = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
                    Map<String, ?> map = rememberMe.getAll();
                    String userId = (String) map.get("idUser");
                    article.setTitleText(titleAccept);
                    article.setCategory(addCategory);
                    article.setAbstractText(abstractAccept);
                    article.setBodyText(bodyAccept);
                    article.setSubtitleText(subTitleAccept);

                    //newModifArticle = new Article(addCategory, titleAccept, abstractAccept, bodyAccept, subTitleAccept, userId);
                    try {
                        String imageDescription = article.getImage().getDescription();
                        thumbail = imgToBase64String(bitmap);
                        Image image = new Image(0,imageDescription, article.getId(), thumbail);
                        article.setImage(image);
                        AddArticleTask.article = article;
                        AddArticleTask saveArticle = new AddArticleTask();
                        saveArticle.execute();
                        newModifArticle = saveArticle.get();
                        //SI LA LLAMADA A MODIFICAR EL ARTICULO HA IDO BIEN
                        AlertDialog.Builder builder = new AlertDialog.Builder(Modify_article_after_login.this);
                        builder.setTitle(getResources().getString(R.string.warning));
                        builder.setMessage(getResources().getString(R.string.article_modified));

                        builder.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent back_intent=new Intent(Modify_article_after_login.this, MainActivity.class);
                                startActivity(back_intent);
                                // reload_articles();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        //Intent back_intent=new Intent(Modify_article_after_login.this, MainActivity.class);
                        //startActivity(back_intent);
                        // reload_articles();

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Modify_article_after_login.this);
                        builder.setTitle(getResources().getString(R.string.warning));
                        builder.setMessage(getResources().getString(R.string.article_not_modified));

                        builder.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } catch (InterruptedException e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Modify_article_after_login.this);
                        builder.setTitle(getResources().getString(R.string.warning));
                        builder.setMessage(getResources().getString(R.string.article_not_modified));

                        builder.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        e.printStackTrace();
                    } catch (ServerCommunicationError serverCommunicationError) {
                        serverCommunicationError.printStackTrace();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Modify_article_after_login.this);
                        builder.setTitle(getResources().getString(R.string.warning));
                        builder.setMessage(getResources().getString(R.string.article_not_modified));

                        builder.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }

                }
            });


            Button cancelArticleModify =(Button) findViewById(R.id.cancelModify);
            cancelArticleModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Modify_article_after_login.this);
                    builder.setTitle(getResources().getString(R.string.warning));
                    builder.setMessage(getResources().getString(R.string.modify_article));

                    builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Me quedo donde estoyi
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent goMainAfterLogin = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(goMainAfterLogin);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            });


        } catch (
                ServerCommunicationError serverCommunicationError) {
            serverCommunicationError.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        SharedPreferences rememberMeTwo = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorTwo = rememberMeTwo.edit();
        editorTwo.putBoolean("session", false);
        editorTwo.putBoolean("stayLogged", stayLogged);
        editorTwo.commit();
        super.onPause();
    }

    @Override
    protected void onStop() {
        SharedPreferences rememberMeTwo = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorTwo = rememberMeTwo.edit();
        editorTwo.putBoolean("session", false);
        editorTwo.putBoolean("stayLogged", stayLogged);
        editorTwo.commit();
        super.onStop();
    }

    public String[] setcategory(String categoryArticle) {
        //Only English
        //Resources standardResources = Modify_article_after_login.this.getResources();
        //AssetManager assets = standardResources.getAssets();
        //DisplayMetrics metrics = standardResources.getDisplayMetrics();
        //Configuration config = new Configuration(standardResources.getConfiguration());
        //config.locale = Locale.US;
        //Resources defaultResources = new Resources(assets, metrics, config);
        //String[] set = defaultResources.getStringArray(R.array.category);
        String[] set = getResources().getStringArray(R.array.category);
        int index;
        if(categoryArticle.equals("National") || categoryArticle.equals("Nacional")){
            index=0;
        }else if(categoryArticle.equals("Economy") || categoryArticle.equals("Economia") ||
        categoryArticle.equals("Economía")){
            index=1;
        }else if(categoryArticle.equals("Sports") || categoryArticle.equals("Deportes")){
            index=2;
        }else if(categoryArticle.equals("Tecnology") || categoryArticle.equals("Tecnologia") ||
        categoryArticle.equals("Tecnología")){
            index=3;
        }else{
            index=1;
        }
        set[index]=categoryArticle;
        return set;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap); //FUNCIONA
                try {
                    thumbail = imgToBase64String(bitmap);
                }catch (Exception e){
                    thumbail="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";
                }
                //System.out.println("THUMBAIL EEEEEEE " + thumbail);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}

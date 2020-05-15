package com.testlistview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.appnewspaper.utils.network.exceptions.ServerCommunicationError;



import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.simple.JSONObject;

import java.io.IOException;
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
        Boolean mantenerSesion = (Boolean) map.get("stayLogged");
        Boolean session = (Boolean) map.get("session");
        if (mantenerSesion == null) {
            SharedPreferences.Editor editorTwo = rememberMe.edit();
            editorTwo = rememberMe.edit();
            editorTwo.putBoolean("session", false);
            editorTwo.commit();
        }else{
            stayLogged=mantenerSesion;
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
                    newModifArticle = new Article(addCategory, titleAccept, abstractAccept, bodyAccept, subTitleAccept, userId);
                    try {
                        String imageDescription = article.getImage().getDescription();
                        thumbail = imgToBase64String(bitmap);
                        newModifArticle.addImage(thumbail, imageDescription);

                        AddArticleTask.article = newModifArticle;
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
                        Intent back_intent=new Intent(Modify_article_after_login.this, MainActivity.class);
                        startActivity(back_intent);
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
        String[] set = getResources().getStringArray(R.array.category);
        int index=0;
        for(int i=0;i<set.length;i++){
            if(categoryArticle.equals(set[i])){
                index=i;
            }
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
                thumbail = imgToBase64String(bitmap);
                System.out.println("THUMBAIL EEEEEEE " + thumbail);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}

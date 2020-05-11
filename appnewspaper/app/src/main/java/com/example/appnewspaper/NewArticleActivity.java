package com.example.appnewspaper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.example.appnewspaper.model.Article;
import com.example.appnewspaper.model.Image;
import com.example.appnewspaper.utils.network.ModelManager;
import com.example.appnewspaper.utils.network.exceptions.ServerCommunicationError;
import com.example.testlistview.Modify_article_after_login;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
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

import static com.example.appnewspaper.utils.SerializationUtils.imgToBase64String;

public class NewArticleActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    ImageView imageView;
    Bitmap bitmap;
    String thumbail;
    Article newModifArticle;
    SharedPreferences rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_article);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner spinnerCategory = findViewById(R.id.categoryAdd);
        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategory);


        imageView = findViewById(R.id.imageView);
        Button button = findViewById(R.id.uploadNewImageArticle);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        Button cancelButton = findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast alertMensaje = Toast.makeText(getApplicationContext(), "Dialogo de confirmacion", Toast.LENGTH_LONG);
                //alertMensaje.show();
                AlertDialog.Builder builder = new AlertDialog.Builder(NewArticleActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("Attention!");
                builder.setMessage("Are you sure to cancel the process?");

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Me quedo donde estoyi
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent goMainAfterLogin = new Intent(getBaseContext(), MainActivityAfterLogin.class);
                        startActivity(goMainAfterLogin);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                //Intent goBeforeScreen = new Intent(getBaseContext(), MainActivityAfterLogin.class);
                //startActivity(goBeforeScreen);

            }
        });

        Button saveButton = findViewById(R.id.savve);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Comprobar que cada uno de los campos estan rellenos
                TextInputEditText addTitle = findViewById(R.id.titleAddForm);
                String titleAdd = addTitle.getText().toString();
                TextInputEditText addSubtitle = findViewById(R.id.subtitleAddArticle);
                String subtiteAdd = addSubtitle.getText().toString();
                TextInputEditText addAbstract = findViewById(R.id.resumen);
                String abstractAdd = String.valueOf(addAbstract.getText());
                TextInputEditText bodyNoticia = findViewById(R.id.noticia);
                String noticiaAdd = String.valueOf(bodyNoticia.getText());
                Spinner category = findViewById(R.id.categoryAdd);
                String addCategory = String.valueOf(category.getSelectedItem());
                TextInputEditText imageDescription = findViewById(R.id.imageDescriptionNew);
                String imageDescriptionString = String.valueOf(imageDescription.getText());

                if (titleAdd.equals("") || subtiteAdd.equals("") || abstractAdd.equals("") || noticiaAdd.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewArticleActivity.this);
                    builder.setTitle("Attention!");
                    builder.setMessage("Please, fill the fields");

                    builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    rememberMe = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
                    Map<String, ?> map = rememberMe.getAll();
                    String userId = (String) map.get("idUser");
                    newModifArticle = new Article(addCategory, titleAdd, abstractAdd, noticiaAdd, subtiteAdd, userId);
                    try {
                        newModifArticle.addImage(thumbail, imageDescriptionString);
                    } catch (ServerCommunicationError serverCommunicationError) {
                        serverCommunicationError.printStackTrace();
                    }
                    System.out.println("THUMBAIL EEEEEEE " + thumbail);
                    //Hago llamada a model manager

                    AddArticleTask addArticleTask = (AddArticleTask) new AddArticleTask();
                    addArticleTask.article = newModifArticle;
                    addArticleTask.execute();

                    try {
                        Article articleAdd = addArticleTask.get();
                        System.out.println(articleAdd);
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewArticleActivity.this);
                        builder.setTitle("Attention!");
                        builder.setMessage("The article is saved correctly");

                        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent goMainAfterLogin = new Intent(getBaseContext(), MainActivityAfterLogin.class);
                                startActivity(goMainAfterLogin);
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    } catch (ExecutionException e) {
                        e.printStackTrace();

                        AlertDialog.Builder builder = new AlertDialog.Builder(NewArticleActivity.this);
                        builder.setTitle("Attention!");
                        builder.setMessage("The article have been added correctly");
                        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent goBeforeScreen = new Intent(getBaseContext(), MainActivityAfterLogin.class);
                                startActivity(goBeforeScreen);
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    } catch (InterruptedException e) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(NewArticleActivity.this);
                        builder.setTitle("Attention!");
                        builder.setMessage("The article have been added correctly");
                        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent goBeforeScreen = new Intent(getBaseContext(), MainActivityAfterLogin.class);
                                startActivity(goBeforeScreen);
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        e.printStackTrace();
                    }

                }

            }
        });

    }

    /**
     *
     */
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
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

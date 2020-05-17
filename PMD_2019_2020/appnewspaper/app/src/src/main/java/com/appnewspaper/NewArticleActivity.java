package com.appnewspaper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.appnewspaper.model.Article;
import com.appnewspaper.model.Image;
import com.appnewspaper.utils.network.ModelManager;
import com.appnewspaper.utils.network.exceptions.ServerCommunicationError;
import com.testlistview.Modify_article_after_login;


import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import static com.appnewspaper.utils.SerializationUtils.imgToBase64String;

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

        Spinner spinnerCategory = (Spinner) findViewById(R.id.categoryAdd);
        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(
                this, R.array.category, android.R.layout.simple_spinner_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategory);


        imageView = (ImageView) findViewById(R.id.imageView);
        Button button = (Button) findViewById(R.id.uploadNewImageArticle);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        //Button cancel the process, it is shown an alert to inform that process will abort
        Button cancelButton = (Button) findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String warning_string=getResources().getString(R.string.warning);
                String cancel=getResources().getString(R.string.cancelModify);
                String ok=getResources().getString(R.string.ok);
                AlertDialog.Builder builder = new AlertDialog.Builder(NewArticleActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle(warning_string);
                builder.setMessage("?");
                //
                builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Me quedo donde estoyi
                        dialog.dismiss();
                    }
                });
                //Come back to sceen when I can see all the articles to delete, create, modify and see them.
                builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent goMainAfterLogin = new Intent(getBaseContext(), MainActivityAfterLogin.class);
                        startActivity(goMainAfterLogin);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        //Button to save the modified article
        Button saveButton = (Button) findViewById(R.id.savve);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                String warning_string=getResources().getString(R.string.warning);
                String article_created=getResources().getString(R.string.new_published);
                String required_data=getResources().getString(R.string.required_data);
                String ok=getResources().getString(R.string.ok);

                //Catch al the values of the fields to fill
                TextInputEditText addTitle = (TextInputEditText) findViewById(R.id.titleAddForm);
                String titleAdd = addTitle.getText().toString();
                TextInputEditText addSubtitle = (TextInputEditText) findViewById(R.id.subtitleAddArticle);
                String subtiteAdd = addSubtitle.getText().toString();
                TextInputEditText addAbstract = (TextInputEditText) findViewById(R.id.resumen);
                String abstractAdd = String.valueOf(addAbstract.getText());
                TextInputEditText bodyNoticia = (TextInputEditText) findViewById(R.id.noticia);
                String noticiaAdd = String.valueOf(bodyNoticia.getText());
                Spinner category = (Spinner) findViewById(R.id.categoryAdd);
                String addCategory = String.valueOf(category.getSelectedItem());
                TextInputEditText imageDescription = (TextInputEditText) findViewById(R.id.imageDescriptionNew);
                String imageDescriptionString = String.valueOf(imageDescription.getText());

                //Chceck that th all fields are filled
                if (titleAdd.equals("") || subtiteAdd.equals("") || abstractAdd.equals("") || noticiaAdd.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewArticleActivity.this);
                    builder.setTitle(warning_string);
                    builder.setMessage(required_data);

                    builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
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
                    //Create the new article to add to the server
                    newModifArticle = new Article(addCategory, titleAdd, abstractAdd, noticiaAdd, subtiteAdd, userId);
                    try {
                        newModifArticle.addImage(thumbail, imageDescriptionString);
                    } catch (ServerCommunicationError serverCommunicationError) {
                        serverCommunicationError.printStackTrace();
                    }

                    //Asynctask to add a new article
                    AddArticleTask addArticleTask = (AddArticleTask) new AddArticleTask();
                    addArticleTask.article = newModifArticle;
                    addArticleTask.execute();

                    try {

                        Article articleAdd = addArticleTask.get();

                        //Positive Feedback when I add a new article
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewArticleActivity.this);
                        builder.setTitle(warning_string);
                        builder.setMessage(article_created);

                        builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
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
                        //Negative Feedback when I add a new article
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewArticleActivity.this);
                        builder.setTitle(warning_string);
                        builder.setMessage(article_created);
                        builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent goBeforeScreen = new Intent(getBaseContext(), MainActivityAfterLogin.class);
                                startActivity(goBeforeScreen);
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    } catch (InterruptedException e) {
                        //Negative Feedback when I add a new article
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewArticleActivity.this);
                        builder.setTitle(warning_string);
                        builder.setMessage(article_created);
                        builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
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
     * Method to open the gallery to add new image
     */
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    /**
     * Method to add a new image to modify the article
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap); //FUNCIONA
                thumbail = imgToBase64String(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}

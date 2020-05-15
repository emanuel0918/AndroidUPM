package com.appnewspaper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import com.appnewspaper.model.Article;
import com.appnewspaper.utils.network.exceptions.ServerCommunicationError;
import com.testlistview.Modify_article_after_login;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.appnewspaper.utils.SerializationUtils.base64StringToImg;

public class MyAdapter extends ArrayAdapter<Article> {
    Context mContext;

    public MyAdapter(Context context, ArrayList<Article> records) {
        super(context, 0, records);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Article item = getItem(position);

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_main_after_login, null, true);
        }

        final TextView listTitle = (TextView) convertView.findViewById(R.id.titleAfterLogin2);
        final TextView listCategory = (TextView) convertView.findViewById(R.id.categoryAfterLogin2);
        final TextView listAbstract = (TextView) convertView.findViewById(R.id.abstractAfterLogin2);

        ImageView imageList = (ImageView) convertView.findViewById(R.id.imageArticleAfter);
        ImageButton editListArticle = (ImageButton) convertView.findViewById(R.id.buttonEditArticle);
        ImageButton deleteListArticle = (ImageButton) convertView.findViewById(R.id.buttonDeleteArticle);
        Bitmap bitmap=null;
        try{
            bitmap=base64StringToImg(item.getImage().getImage());
            imageList.setImageBitmap(bitmap);
        }catch (Exception e){
        }
        Spanned title = Html.fromHtml(item.getTitleText());
        listTitle.setText(title);
        listCategory.setText(item.getCategory());
        Spanned abstractArticle = Html.fromHtml(item.getAbstractText());
        listAbstract.setText(abstractArticle);


        imageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int id2 = (int) id;
                LoadArticleTask.id = item.getId();
                //System.out.println("ID EEEEEE " + item.getId());
                //if (position == 0 || position < articles2.size()) {
                Intent goToArticle = new Intent(mContext, activity_article_after_login.class);
                goToArticle.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.getApplicationContext().startActivity(goToArticle);
                //}
            }
        });


        deleteListArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(mContext.getResources().getString(R.string.warning));
                builder.setMessage(mContext.getResources().getString(R.string.confirm_deleting));

                builder.setPositiveButton(mContext.getResources().getString(R.string.accept),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Añadir
                        DeleteArticleTask.id = item.getId();
                        DeleteArticleTask delete = new DeleteArticleTask();
                        delete.execute();
                        try {
                            int i = delete.get();

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle(mContext.getResources().getString(R.string.warning));
                            builder.setMessage(mContext.getResources().getString(R.string.error_deleting));
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle(mContext.getResources().getString(R.string.warning));
                            builder.setMessage(mContext.getResources().getString(R.string.error_deleting));
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle(mContext.getResources().getString(R.string.warning));
                        builder.setMessage(mContext.getResources().getString(R.string.deleted));
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        Intent goMainAfterLogin = new Intent(mContext, MainActivityAfterLogin.class);
                        goMainAfterLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.getApplicationContext().startActivity(goMainAfterLogin);
                    }
                });
                builder.setNegativeButton(mContext.getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Me quedo donde estoyi
                        ((MainActivity)mContext).finish();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        editListArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadArticleTask.id = item.getId();
                Intent goModifiArticle = new Intent(mContext, Modify_article_after_login.class);
                goModifiArticle.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.getApplicationContext().startActivity(goModifiArticle);

            }
        });
        return convertView;
    }


}
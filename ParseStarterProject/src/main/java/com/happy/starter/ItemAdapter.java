package com.happy.starter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by haris on 5/24/2017.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private Context context;
    private ArrayList<Item> itemList;
    SwipeRefreshLayout swiper;
    private ImageView content;
    public static final String ACTION_LIKE_BUTTON_CLICKED = "action_like_button_button";

    public ItemAdapter(Context context, ArrayList<Item> itemList, SwipeRefreshLayout swiper){
        this.context = context;
        this.itemList = itemList;
        this.swiper= swiper;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext())
                .inflate(R.layout.number_list_item, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final Item item = itemList.get(position);
        Picasso.with(context)
                .load(item.img)
                .fit()
                .error(android.R.drawable.stat_notify_error)
                .placeholder(R.drawable.pp1)
                .into(holder.ivImg);

        //       holder.tvText.setText(item.text);
        final String string = (String) item.img;
        holder.ivImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent in = new Intent(context,MainActivity.class);
                in.putExtra("mystring",string);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
            }
        });

        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "App test demo  ");
                i.putExtra(Intent.EXTRA_TEXT, string);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent.createChooser(i, "Share URL"));

                Log.i("Interaction", "Button Tapped");

            }
        });


    }
    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... urls) {

            try {

                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream inputStream = connection.getInputStream();

                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);

                return myBitmap;


            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

            return null;

        }


    }


    public void swap(ArrayList<Item> datas){
        Collections.reverse(itemList);
        itemList =datas;
        notifyDataSetChanged();
    }
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            Toast.makeText(context,"entered share",Toast.LENGTH_SHORT).show();
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".jpg");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }



    @Override
    public int getItemCount() {
        if(itemList != null){
            return itemList.size();
        }
        return 0;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        public CardView cvItem;
        public ImageView ivImg;

        public SwipeRefreshLayout swiper;
        public ImageView btnMore;


        //  public TextView tvText;

        public ItemViewHolder(View itemView) {
            super(itemView);
            cvItem = (CardView)itemView.findViewById(R.id.cvItem);
            ivImg = (ImageView)itemView.findViewById(R.id.ivImg);
            swiper = (SwipeRefreshLayout)itemView.findViewById(R.id.swiper);
            btnMore = (ImageView) itemView.findViewById(R.id.btnMore);

        }
    }
}

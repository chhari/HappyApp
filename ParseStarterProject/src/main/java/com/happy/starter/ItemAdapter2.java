package com.happy.starter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by haris on 5/27/2017.
 */


public class ItemAdapter2 extends RecyclerView.Adapter<ItemAdapter2.ItemViewHolder>{
    private Context context;
    private ArrayList<Item> itemList;
    public ItemAdapter2(Context context, ArrayList<Item> itemList){
            this.context = context;
            this.itemList = itemList;

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
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        Picasso.with(context)
                .load(item.img)
                .fit()
                .error(android.R.drawable.stat_notify_error)
                .placeholder(R.drawable.pp)
                .into(holder.ivImg);

        //       holder.tvText.setText(item.text);
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
        //  public TextView tvText;

        public ItemViewHolder(View itemView) {
            super(itemView);
            cvItem = (CardView)itemView.findViewById(R.id.cvItem);
            ivImg = (ImageView)itemView.findViewById(R.id.ivImg);
            //       tvText = (TextView)itemView.findViewById(R.id.tvText);

        }
    }
}

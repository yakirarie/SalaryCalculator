package com.example.yakirlaptop.salarycalculator;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DataItemAdapter extends RecyclerView.Adapter<DataItemAdapter.DataItemHolder> {
    private ArrayList<DataItem> dataItemList;
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;

    }

    public static class DataItemHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView textView;
        public TextView textView2;
        public ImageView imageViewDelete;


        public DataItemHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            imageViewDelete = itemView.findViewById(R.id.image_delete);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if(onItemClickListener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });

            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            onItemClickListener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }
    public DataItemAdapter(ArrayList<DataItem> dataItemList){
        this.dataItemList = dataItemList;

    }
    @NonNull
    @Override
    public DataItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_item,viewGroup,false);
        DataItemHolder dataItemHolder = new DataItemHolder(view,onItemClickListener);
        return dataItemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DataItemHolder dataItemHolder, int i) {
        DataItem dataItem = dataItemList.get(i);
        dataItemHolder.imageView.setImageResource(dataItem.getmImageResource());
        dataItemHolder.textView.setText(dataItem.getmText1());
        dataItemHolder.textView2.setText(dataItem.getmText2());

    }

    @Override
    public int getItemCount() {
        return dataItemList.size();
    }
}

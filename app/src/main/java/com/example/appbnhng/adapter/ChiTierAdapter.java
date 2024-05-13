package com.example.appbnhng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbnhng.R;
import com.example.appbnhng.model.Item;
import com.example.appbnhng.utils.Utils;

import java.util.List;

public class ChiTierAdapter extends RecyclerView.Adapter<ChiTierAdapter.MyViewHolder> {

    Context context;
    List<Item> itemList;

    public ChiTierAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ChiTierAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chitiet,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChiTierAdapter.MyViewHolder holder, int position) {
        Item  item = itemList.get(position);
        holder.ten.setText(item.getTensp()+" ");
        holder.soluong.setText("Số lượng:"+item.getSoluong()+"");
        if (item.getHinhanh().contains("http")){
            Glide.with(context).load(item.getHinhanh()).into(holder.imagechitiet);
        }else {
            String hinh= Utils.BASE_URL+"uploads/"+item.getHinhanh();
            Glide.with(context).load(hinh).into(holder.imagechitiet);
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {
       ImageView imagechitiet;
       TextView ten,soluong;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imagechitiet=itemView.findViewById(R.id.item_imgchitiet);
            ten=itemView.findViewById(R.id.item_tenspchitiet);
            soluong=itemView.findViewById(R.id.item_soluongchitiet);
        }
    }
}

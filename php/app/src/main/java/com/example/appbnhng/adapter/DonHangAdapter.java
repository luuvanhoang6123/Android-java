package com.example.appbnhng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbnhng.R;
import com.example.appbnhng.model.DonHang;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter< DonHangAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool=new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang >donHangList;

    public DonHangAdapter(Context context, List<DonHang> donHangList) {
        this.context = context;
        this.donHangList = donHangList;
    }

    @NonNull
    @Override
    public DonHangAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangAdapter.MyViewHolder holder, int position) {
        DonHang donHang=donHangList.get(position);
        holder.txtdonhang.setText("DON HANG: "+donHang.getId());
        LinearLayoutManager layoutManager=new LinearLayoutManager(
          holder.reChitiet.getContext(),
          LinearLayoutManager.VERTICAL,
          false
        );
    layoutManager.setInitialPrefetchItemCount(donHang.getItem().size());
    //adapter chitiet
        ChiTierAdapter chiTierAdapter=new ChiTierAdapter(context,donHang.getItem());
        holder.reChitiet.setLayoutManager(layoutManager);
        holder.reChitiet.setAdapter(chiTierAdapter);
        holder.reChitiet.setRecycledViewPool(viewPool);

    }

    @Override
    public int getItemCount() {
        return donHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtdonhang;
        RecyclerView reChitiet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
          txtdonhang=itemView.findViewById(R.id.iddonhang);
          reChitiet=itemView.findViewById(R.id.rechitiet);
        }
    }
}

package com.example.appbnhng.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbnhng.Interface.ImageClickListener;
import com.example.appbnhng.Interface.ItemClickListener;
import com.example.appbnhng.R;
import com.example.appbnhng.model.EventBus.TinhTongEvent;
import com.example.appbnhng.model.GioHang;
import com.example.appbnhng.model.SanPhamMoi;
import com.example.appbnhng.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
   Context context;
   List<GioHang>gioHangList;
    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public GioHangAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHangAdapter.MyViewHolder holder, int position) {
        GioHang gioHang=gioHangList.get(position);
       // SanPhamMoi sanPhamMoi= new SanPhamMoi();
        holder.item_giohang_tensp.setText(gioHang.getTensp());
        holder.item_giohang_soluong.setText(gioHang.getSoluong()+"");
        Glide.with(context).load(gioHang.getHinhanhsp()).into(holder.item_giohang_image);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.giagoc.setText(decimalFormat.format(gioHang.getGiasp()));
        long gia= gioHang.getSoluong()*gioHang.getGiasp();
        holder.giatong.setText(decimalFormat.format(gia));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Utils.manggiohang.get(holder.getAdapterPosition()).setChecked(true);
                    if (!Utils.mangmuahang.contains(gioHang)){
                        Utils.mangmuahang.add(gioHang);
                    }

                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }else {
                    for (int i = 0; i<Utils.mangmuahang.size(); i++){
                        Utils.manggiohang.get(holder.getAdapterPosition()).setChecked(false);
                        if (Utils.mangmuahang.get(i).getIdsp()==gioHang.getIdsp()){
                            Utils.mangmuahang.remove(i);
                            EventBus.getDefault().postSticky(new TinhTongEvent());
                        }
                    }
                }
            }
        });
        holder.checkBox.setChecked(gioHang.isChecked());

        holder.setListener(new ImageClickListener() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                Log.d("TAG","onImageClick"+pos+"..."+giatri);
                if(giatri==1){
                    if (gioHangList.get(pos).getSoluong()>1){
                        int soluongmoi= gioHangList.get(pos).getSoluong()-1;
                        gioHangList.get(pos).setSoluong(soluongmoi);

                        holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong()+"");
                        long gia=gioHangList.get(pos).getSoluong()*gioHangList.get(pos).getGiasp();
                        holder.giatong.setText(decimalFormat.format(gia));
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    } else if (gioHangList.get(pos).getSoluong()==1) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa sản phẩm này giỏ không?");
                        builder.setPositiveButton("Đồng ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.mangmuahang.remove(gioHang);
                                Utils.manggiohang.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();

                    }
                }else if(giatri==2){
                    if (gioHangList.get(pos).getSoluong()<11){
                        int soluongmoi= gioHangList.get(pos).getSoluong()+1;
                        gioHangList.get(pos).setSoluong(soluongmoi);
                    }
                    holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong()+"");
                    long gia=gioHangList.get(pos).getSoluong()*gioHangList.get(pos).getGiasp();
                    holder.giatong.setText(decimalFormat.format(gia));
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {
       ImageClickListener listener;

        public void setListener(ImageClickListener listener) {
            this.listener = listener;
        }

        ImageView item_giohang_image,img_cong,img_tru;
        TextView item_giohang_tensp,giagoc,item_giohang_soluong,giatong;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_giohang_image=itemView.findViewById(R.id.item_giohang_image);
            giagoc=itemView.findViewById(R.id.item_giohang_gia);
            item_giohang_tensp=itemView.findViewById(R.id.item_giohang_tensp);
            item_giohang_soluong=itemView.findViewById(R.id.item_giohang_soluong);
            giatong=itemView.findViewById(R.id.item_giohang_giasp2);
            img_cong=itemView.findViewById(R.id.item_giohang_cong);
            img_tru=itemView.findViewById(R.id.item_giohang_tru);

            img_cong.setOnClickListener(this::onClick);
            img_tru.setOnClickListener(this::onClick);

            checkBox=itemView.findViewById(R.id.item_giohang_check);
        }
        public  void  onClick(View view){
            if(view==img_tru){
                listener.onImageClick(view,getAdapterPosition(),1);
                //1 la tru
            }else if(view==img_cong){
                //2la cong
                listener.onImageClick(view,getAdapterPosition(),2);
            }
        }
    }
}

package com.example.appbnhng.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.appbnhng.R;
import com.example.appbnhng.model.GioHang;
import com.example.appbnhng.model.SanPhamMoi;
import com.example.appbnhng.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ChiTiet extends AppCompatActivity {

    TextView tensp, giasp,mota;
    ImageView imghinhanh;
    Button btnthem;
    Spinner spinner;
    Toolbar toolbar;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        initData();
        initControl();
    }

    private void initControl() {
    btnthem.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            themGioHang();
        }
    });
    }

    private void themGioHang() {
        if (Utils.manggiohang.size() > 0){
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for (int i=0 ; i<Utils.manggiohang.size(); i++){
                if (Utils.manggiohang.get(i).getIdsp() == sanPhamMoi.getId()){
                    Utils.manggiohang.get(i).setSoluong(soluong + Utils.manggiohang.get(i).getSoluong());
                    long gia=Long.parseLong(sanPhamMoi.getGiasp());
                    Utils.manggiohang.get(i).setGiasp(gia);
                    flag = true;
                }
            }
            if (flag == false){
                long gia= Long.parseLong(sanPhamMoi.getGiasp());
                GioHang gioHang=new GioHang();
                gioHang.setGiasp(gia);
                gioHang.setSoluong(soluong);
                gioHang.setTensp(sanPhamMoi.getTensp());
                gioHang.setIdsp(sanPhamMoi.getId());
                gioHang.setHinhanhsp(sanPhamMoi.getHinhanh());
                Utils.manggiohang.add(gioHang);
            }
        }else{
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia= Long.parseLong(sanPhamMoi.getGiasp());
            GioHang gioHang=new GioHang();
            gioHang.setGiasp(gia);
            gioHang.setSoluong(soluong);
            gioHang.setTensp(sanPhamMoi.getTensp());
            gioHang.setIdsp(sanPhamMoi.getId());
            gioHang.setHinhanhsp(sanPhamMoi.getHinhanh());
            Utils.manggiohang.add(gioHang);
        }

        int totalItem=0;
        for (int i=0; i<Utils.manggiohang.size(); i++){
            totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        sanPhamMoi=sanPhamMoi=(SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanPhamMoi.getTensp());
        mota.setText(sanPhamMoi.getMota());
       Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(imghinhanh);
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        giasp.setText("Gia: "+decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp()))+"Đ");
        Integer[] so=new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adaptersoluong=new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,so);
        spinner.setAdapter(adaptersoluong);
    }

    private void initView() {
    tensp=findViewById(R.id.txttensp);
    giasp=findViewById(R.id.txtgiasp);
    mota=findViewById(R.id.txtmotachitiet);
    btnthem=findViewById(R.id.btnthemvaogiohang);
    imghinhanh=findViewById(R.id.imgchitiet);
    toolbar=findViewById(R.id.toobar);
    spinner=findViewById(R.id.spinner);
    badge=findViewById(R.id.menu_sl);
    FrameLayout frameLayoutgiohang=findViewById(R.id.framegiohang);
    frameLayoutgiohang.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent giohang=new Intent(getApplicationContext(),GioHangActivity.class);
            startActivity(giohang);
        }
    });


}

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.manggiohang!=null){
            int totalItem=0;
            for (int i=0; i<Utils.manggiohang.size(); i++){
                totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }
    }
}
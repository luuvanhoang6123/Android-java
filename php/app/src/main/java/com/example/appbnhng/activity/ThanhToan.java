package com.example.appbnhng.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbnhng.R;
import com.example.appbnhng.retrofit.ApiBanHang;
import com.example.appbnhng.retrofit.RetrofitClient;
import com.example.appbnhng.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToan extends AppCompatActivity {
    Toolbar toolbar;
    TextView txttongTien,txtsodt,txtemail;
    EditText edtdiachi;
    AppCompatButton btndathang;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    ApiBanHang apiBanHang;
    long tongtien;
    int totalItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        initControl();
        countItem();
    }

    private void countItem() {
         totalItem=0;
        for (int i=0; i<Utils.mangmuahang.size(); i++){
            totalItem = totalItem + Utils.mangmuahang.get(i).getSoluong();
        }
    }

    private void initView() {
        apiBanHang= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar=findViewById(R.id.toobar);
        txtemail=findViewById(R.id.txtemail);
        txtsodt=findViewById(R.id.txtsodienthoai);
        edtdiachi=findViewById(R.id.edtdiachi);
        txttongTien=findViewById(R.id.thanhtien);
        btndathang=findViewById(R.id.btndathang);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
         tongtien = getIntent().getLongExtra("tongtien",0);

        txttongTien.setText(decimalFormat.format(tongtien));

        txtsodt.setText(Utils.user_current.getMobile());
        txtemail.setText(Utils.user_current.getEmail());
        btndathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_diachi=edtdiachi.getText().toString().trim();
                if (TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập địa chỉ !",Toast.LENGTH_LONG).show();

                }else {
                    //post data
                    String str_email=Utils.user_current.getEmail();
                    String str_sdt=Utils.user_current.getMobile();
                    int id=Utils.user_current.getId();
                    Log.d("test",new Gson().toJson(Utils.mangmuahang));
                    compositeDisposable.add(apiBanHang.createOder(str_email,str_sdt,String.valueOf(tongtien),id,str_diachi,totalItem,new Gson().toJson(Utils.mangmuahang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        Toast.makeText(getApplicationContext(),"Đặt hàng thành công !",Toast.LENGTH_LONG).show();
                                        Utils.mangmuahang.clear();
                                        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    },throwable -> {
                                        Log.d("loggg", "onClick: "+throwable.getMessage());
                                        Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                                    }

                            ));
                }
            }
        });
    }

}
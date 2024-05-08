package com.example.appbnhng.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appbnhng.R;
import com.example.appbnhng.adapter.DienThoaiAdapter;
import com.example.appbnhng.model.SanPhamMoi;
import com.example.appbnhng.retrofit.ApiBanHang;
import com.example.appbnhng.retrofit.RetrofitClient;
import com.example.appbnhng.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class timkiem extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    EditText edtsearch;
    DienThoaiAdapter adapterDt;
    List<SanPhamMoi> sanPhamMoiList;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable =new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timkiem);
        initView();

    }
    private void initView() {
        sanPhamMoiList=new ArrayList<>();
        apiBanHang= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        edtsearch=findViewById(R.id.edtsearch);
        toolbar=findViewById(R.id.toobar);
        recyclerView=findViewById(R.id.recycleview_search);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()==0){
                    sanPhamMoiList.clear();
                    adapterDt=new DienThoaiAdapter(getApplicationContext(),sanPhamMoiList);
                    recyclerView.setAdapter(adapterDt);
                }else {
                    getDataSearch();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

    }

    private void getDataSearch() {
        sanPhamMoiList.clear();
        String str_search=edtsearch.getText().toString().trim();
        compositeDisposable.add(apiBanHang.search(str_search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()){
                                sanPhamMoiList=sanPhamMoiModel.getResult();
                                adapterDt=new DienThoaiAdapter(getApplicationContext(),sanPhamMoiList);
                                recyclerView.setAdapter(adapterDt);
                            }else{
                                Toast.makeText(getApplicationContext(),sanPhamMoiModel.getMessage(),Toast.LENGTH_LONG).show();
                                sanPhamMoiList.clear();
                                adapterDt.notifyDataSetChanged();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }
                ) );
    }




}
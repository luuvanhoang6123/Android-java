package com.example.appbnhng.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appbnhng.R;
import com.example.appbnhng.databinding.ActivityThemSpBinding;
import com.example.appbnhng.model.MesseageModel;
import com.example.appbnhng.model.SanPhamMoi;
import com.example.appbnhng.retrofit.ApiBanHang;
import com.example.appbnhng.retrofit.RetrofitClient;
import com.example.appbnhng.utils.Utils;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ThemSP extends AppCompatActivity {
    Spinner spinner;
    int loai=0;
    ActivityThemSpBinding binding;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable=new CompositeDisposable();

    String mediaPath;
    SanPhamMoi sanPhamSua;
    boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityThemSpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiBanHang= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Intent intent=getIntent();
        sanPhamSua= (SanPhamMoi) intent.getSerializableExtra("sua");
        if (sanPhamSua==null){
            //themmoi
            flag=false;
        }else {
            //sua
            flag=true;
            binding.btnThem.setText("Sửa sản phẩm");
            binding.mota.setText(sanPhamSua.getMota());
            binding.hinhanh.setText(sanPhamSua.getHinhanh());
            binding.tensp.setText(sanPhamSua.getTensp());
            binding.spinnerLoai.setSelection(sanPhamSua.getLoai());
            binding.giasp.setText(sanPhamSua.getGiasp());
        }
        initView();
        initData();
    }

    private void initData() {
        List<String> stringList=new ArrayList<>();
        stringList.add("Vui lòng chọn loại");
        stringList.add("Loại 1");
        stringList.add("loại 2");
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,stringList);
        spinner.setAdapter(adapter);
       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               loai=position;
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
         binding.btnThem.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (flag==false){
                     themsanpham();
                 }else
                 {
                     suaSanPham();
                 }

             }
         });
         binding.imgcamera.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ImagePicker.with(ThemSP.this)
                         .crop()	    			//Crop image(Optional), Check Customization for more option
                         .compress(1024)			//Final image size will be less than 1 MB(Optional)
                         .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                         .start();
             }
         });
    }

    private void suaSanPham() {
        String str_ten=binding.tensp.getText().toString().trim();
        String str_gia=binding.giasp.getText().toString().trim();
        String str_hinhanh=binding.hinhanh.getText().toString().trim();
        String str_mota=binding.mota.getText().toString().trim();
        if (TextUtils.isEmpty(str_ten)||TextUtils.isEmpty(str_gia)||TextUtils.isEmpty(str_hinhanh)||TextUtils.isEmpty(str_mota)||loai==0){
            Toast.makeText(getApplicationContext(),"Vui lòng nhập đầy đủ thông tin",Toast.LENGTH_LONG).show();
        }else {
            compositeDisposable.add(apiBanHang.suasp(str_ten,str_gia,str_hinhanh,str_mota,loai,sanPhamSua.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messeageModel -> {
                                if (messeageModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(),messeageModel.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                            }
                    )
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaPath= data.getDataString();
        uploadMultipleFiles();
        Log.d("log","onActivityResult:"+mediaPath);
    }

    private void themsanpham() {
        String str_ten=binding.tensp.getText().toString().trim();
        String str_gia=binding.giasp.getText().toString().trim();
        String str_hinhanh=binding.hinhanh.getText().toString().trim();
        String str_mota=binding.mota.getText().toString().trim();
        if (TextUtils.isEmpty(str_ten)||TextUtils.isEmpty(str_gia)||TextUtils.isEmpty(str_hinhanh)||TextUtils.isEmpty(str_mota)||loai==0){
            Toast.makeText(getApplicationContext(),"Vui lòng nhập đầy đủ thông tin",Toast.LENGTH_LONG).show();
        }else {
            compositeDisposable.add(apiBanHang.insertsp(str_ten,str_gia,str_hinhanh,str_mota,(loai))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messeageModel -> {
                                if (messeageModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(),messeageModel.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                            }
                    )
            );
        }
    }
    private  String getPath(Uri uri){
        String result;
        Cursor cursor=getContentResolver().query(uri,null,null,null,null);
        if (cursor==null){
            result=uri.getPath();
        }else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result=cursor.getString(index);
            cursor.close();
        }
       return  result;
    }
    private void uploadMultipleFiles() {
        Uri uri = Uri.parse(mediaPath);
        File file = new File(getPath(uri));

        // Parsing any Media type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file", file.getName(), requestBody1);
        Call<MesseageModel> call = apiBanHang.uploadFile(fileToUpload1);
        call.enqueue(new Callback< MesseageModel >() {
            @Override
            public void onResponse(Call < MesseageModel > call, Response< MesseageModel > response) {
                MesseageModel serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.isSuccess()) {
                       binding.hinhanh.setText(serverResponse.getName());
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.v("Response", serverResponse.toString());
                }

            }
            @Override
            public void onFailure(Call < MesseageModel > call, Throwable t) {
                Log.d("log",t.getMessage());
            }
        });
    }
    private void initView() {
        spinner=findViewById(R.id.spinner_loai);
    }
}
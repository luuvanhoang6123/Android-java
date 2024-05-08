package com.example.appbnhng.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbnhng.R;
import com.example.appbnhng.model.UserModel;
import com.example.appbnhng.retrofit.ApiBanHang;
import com.example.appbnhng.retrofit.RetrofitClient;
import com.example.appbnhng.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhap extends AppCompatActivity {

    TextView Dangki,reresetpass;
    EditText email,pass;
    AppCompatButton btndangnhap;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    boolean isLogin=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        initView();
        initcontrol();
    }

    private void initView() {
        Paper.init(this);
        apiBanHang= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Dangki=findViewById(R.id.txtDangKi);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        btndangnhap=findViewById(R.id.btndangnhap);
        reresetpass=findViewById(R.id.txtresetpass);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        //read data
        if (Paper.book().read("email")!=null && Paper.book().read("pass")!=null ){
            email.setText(Paper.book().read("email"));
            pass.setText(Paper.book().read("pass"));
            if (Paper.book().read("isLogin")!=null){
                boolean flag=Paper.book().read("isLogin");
                if (flag){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    },1000);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.user_current.getEmail()!=null&&Utils.user_current.getPass()!=null){
            email.setText(Utils.user_current.getEmail());
            pass.setText(Utils.user_current.getPass());
        }
    }

    private void initcontrol() {
        reresetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ResetPass.class);
                startActivity(intent);
            }
        });
        Dangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), DangKy.class);
                startActivity(intent);

            }
        });
        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_email=email.getText().toString().trim();
                String str_pass=pass.getText().toString().trim();
                if (TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập Email!",Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(str_pass)) {
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập Pass!",Toast.LENGTH_LONG).show();
                }else {
                    //save

                    Paper.book().write("email",str_email);
                    Paper.book().write("pass",str_pass);

                    if (user!=null){
                        dangNhap(str_email,str_pass);
                    }else {
                        firebaseAuth.signInWithEmailAndPassword(str_email,str_pass)
                                .addOnCompleteListener(DangNhap.this,new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                               if (task.isSuccessful()){
                                   dangNhap(str_email,str_pass);
                               }
                            }
                        });
                    }
                }

            }
        });
    }
    public  void dangNhap ( String str_email,String str_pass){
        compositeDisposable.add(apiBanHang.dangnhap(str_email,str_pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()){
                                isLogin = true;
                                Paper.book().write("isLogin", isLogin);
                                Utils.user_current = userModel.getResult().get(0);
                                //luu lai thong tin nguoi dung
                                Paper.book().write("user",userModel.getResult().get(0));
                                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }
                )
        );
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
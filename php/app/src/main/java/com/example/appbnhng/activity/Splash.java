package com.example.appbnhng.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.appbnhng.R;

import io.paperdb.Paper;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Paper.init(this);
        Thread thread =new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(2000);
                }catch (Exception ex){

                }finally {
                    if (Paper.book().read("user") == null){
                        Intent intent=new Intent(getApplicationContext(),DangNhap.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent home=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(home);
                        finish();
                    }

                }
            }
        };
        thread.start();
    }
}
package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MapActivity extends AppCompatActivity {
private Button daohang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        findView();
        final Intent myIntent = getIntent();

        Log.e("123",getIntent().getStringExtra("Name"));
        daohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                intent.setAction(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("https://www.google.com.tw/maps/@"+myIntent.getStringExtra("Px")+","+myIntent.getStringExtra("Py")+"?hl=zh-TW&authuser=0"));
                String px = myIntent.getStringExtra("Px");
                String py = myIntent.getStringExtra("Py");
                Log.e("test","123: "+ px+ " " + py);
                intent.setData(Uri.parse("https://www.google.com.tw/maps/@"+py+","+px+",17z?hl=zh-TW&authuser=0"));
                try {
                    startActivity(intent);
                }catch (ActivityNotFoundException e){

                }

                Log.e("test123", "222 " + Thread.currentThread().getId());

//                AsyncTask a;
//
//
//                Thread t = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        int x = 3;
//                        while (x>2){
//                            Log.e("test123", "333 " + Thread.currentThread().getId());
//                        }
//
//                    }
//                });
//
//                t.start();

            }
        });
    }
    private void findView(){
        daohang = findViewById(R.id.daohang);
    }

}

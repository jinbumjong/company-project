package com.example.mappart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnuser = null;
    Button btnregister = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnuser = (Button) findViewById(R.id.btnuser);//객체 생성
        btnregister = (Button) findViewById(R.id.btnregister);

        //mappartActivity 화면으로 넘겨준다
        btnuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Mappartactivity.class);
                startActivity(i);
            }
        });
        //mappart2Activity 화면으로 넘겨준다
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Mappartactivity.class);
                startActivity(i);
            }
        });
    }
}
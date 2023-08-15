package com.example.game1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_game1).setOnClickListener(this);
        findViewById(R.id.btn_game2).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent i = null;
        switch (view.getId()) {
            case R.id.btn_game1:
                i = new Intent(MainActivity.this, ScissosActivity.class);
                break;
            case R.id.btn_game2:
                i = new Intent(MainActivity.this, LottoActivity.class);
                break;
        }
        startActivity(i);
    }
}

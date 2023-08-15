package com.example.game1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LottoActivity extends AppCompatActivity implements View.OnClickListener {
    NumberPicker numberPicker;
    ArrayList<Integer> randomList = new ArrayList<Integer>();
    ArrayList<TextView> numTextViewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lotto);

        findViewById(R.id.clearButton).setOnClickListener(this);
        findViewById(R.id.addButton).setOnClickListener(this);
        findViewById(R.id.runButton).setOnClickListener(this);

        numTextViewList.add((TextView) findViewById(R.id.numTextView1));
        numTextViewList.add((TextView)findViewById(R.id.numTextView2));
        numTextViewList.add((TextView)findViewById(R.id.numTextView3));
        numTextViewList.add((TextView)findViewById(R.id.numTextView4));
        numTextViewList.add((TextView)findViewById(R.id.numTextView5));
        numTextViewList.add((TextView) findViewById(R.id.numTextView6));

        numberPicker = findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(45);

    }

    public void getRandomNumber(){

        int randomNumber = 0;

        if(randomList.size() == 6)
            randomList.clear();

        while(randomList.size() < 6){
            randomNumber = (int)(Math.random()*45+1);

            if(randomList.contains(randomNumber))
                continue;

            randomList.add(randomNumber);
        }
    }

    //번호는 5개까지만 선택할 수 있고, 중복되지 않도록 해야함.
    //이미 6개가 선택되어 있는 상태라면 초기화 후에 선택할 수 있도록 해야함.
    public void addButton(){

        if(randomList.size() == 6) {
            Toast.makeText(getApplicationContext(), "초기화 후에 시도해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        int numPick = numberPicker.getValue();

        if(randomList.contains(numPick)){
            Toast.makeText(getApplicationContext(), "중복되는 값입니다.", Toast.LENGTH_SHORT).show();
        }
        else{
            randomList.add(numPick);
        }

    }

    public void drawNumber(){

        for(int i=0; i<randomList.size(); i++){
            numTextViewList.get(i).setText(Integer.toString(randomList.get(i)));
            numTextViewList.get(i).setVisibility(View.VISIBLE);
        }

    }

    public void clearButton(){

        for(int i=0; i<randomList.size(); i++){
            numTextViewList.get(i).setVisibility(View.GONE);
        }

        randomList.clear();

    }
    //번호를 클릭하면 하나씩 추가되도록, visiblility 를 구현해야 함.

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.runButton:
                getRandomNumber();
                drawNumber();
                Log.d("LottoActivity", randomList.toString());
                break;
            case R.id.addButton:
                addButton();
                drawNumber();
                break;
            case R.id.clearButton:
                clearButton();
                break;


        }
    }
}
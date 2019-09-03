package com.bear.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText text;          // 文本框

    private Button clear;           // 清零
    private Button neg;             // 取相反数
    private Button per;             // 取百分数

    private Button add;             // 加
    private Button sub;             // 减
    private Button mul;             // 乘
    private Button div;             // 除
    private Button result;          // 等于
    private Button point;           // 小数点

    private Button num_0;           // 0
    private Button num_1;           // 1
    private Button num_2;           // 2
    private Button num_3;           // 3
    private Button num_4;           // 4
    private Button num_5;           // 5
    private Button num_6;           // 6
    private Button num_7;           // 7
    private Button num_8;           // 8
    private Button num_9;           // 9

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();       // 初始化变量


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clear:
                text.setText("0");
                break;
            case R.id.neg:
                text.setText(negate(text.getText().toString()));
                break;
        }
    }

    // 取反
    private String negate(String s){
        String newStr;
        if (s.charAt(0) == '-'){
            newStr = s.substring(1);
        }else {
            newStr = "-" + s;
        }
        return newStr;
    }

    // 初始化变量
    private void initLayout(){

        text = findViewById(R.id.text_view);

        clear = findViewById(R.id.clear);
        neg = findViewById(R.id.neg);
        per = findViewById(R.id.per);

        add = findViewById(R.id.add);
        sub = findViewById(R.id.sub);
        mul = findViewById(R.id.mul);
        div = findViewById(R.id.div);
        result = findViewById(R.id.result);

        point = findViewById(R.id.point);
        num_0 = findViewById(R.id.num_0);
        num_1 = findViewById(R.id.num_1);
        num_2 = findViewById(R.id.num_2);
        num_3 = findViewById(R.id.num_3);
        num_4 = findViewById(R.id.num_4);
        num_5 = findViewById(R.id.num_5);
        num_6 = findViewById(R.id.num_6);
        num_7 = findViewById(R.id.num_7);
        num_8 = findViewById(R.id.num_8);
        num_9 = findViewById(R.id.num_9);

    }

}

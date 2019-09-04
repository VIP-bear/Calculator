package com.bear.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText text;          // 文本框
    private TextView formula_text;  // 算式

    private Button clear;           // 清零
    private Button neg;             // 取相反数
    private Button per;             // 取百分数

    private Button root;            // 开根号
    private Button sin;            // sin
    private Button cos;            // cos
    private Button back;            // 回退

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

    private String ope = "";        // 运算符
    private String inputNum_1 = "";      // 第一个数
    private String inputNum_2 = "";      // 第二个数

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 隐藏标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        initLayout();       // 初始化变量
        setClickEvent();    // 设置监听器

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clear:
                text.setText("0");
                ope = "";
                inputNum_1 = "";
                inputNum_2 = "";
                break;
            case R.id.neg:
                text.setText(negate());
                break;
            case R.id.per:
                text.setText(div_100());
                break;
            case R.id.num_0:
            case R.id.num_1:
            case R.id.num_2:
            case R.id.num_3:
            case R.id.num_4:
            case R.id.num_5:
            case R.id.num_6:
            case R.id.num_7:
            case R.id.num_8:
            case R.id.num_9:
            case R.id.point:
                showNum((Button)v);
                break;
            case R.id.add:
            case R.id.sub:
            case R.id.mul:
            case R.id.div:
                ope = ((Button) v).getText().toString();
                inputNum_1 = text.getText().toString();
                text.setText("0");
                break;
            case R.id.result:
                withResult();
                break;
            case R.id.root:
                String r = String.valueOf(Math.sqrt(Double.parseDouble(text.getText().toString())));
                inputNum_1 = formatNum(r);
                text.setText(inputNum_1);
                break;
            case R.id.sin:
                String s = String.valueOf(Math.sin(Double.parseDouble(text.getText().toString())));
                inputNum_1 = formatNum(s);
                text.setText(inputNum_1);
                break;
            case R.id.cos:
                String c = String.valueOf(Math.cos(Double.parseDouble(text.getText().toString())));
                inputNum_1 = formatNum(c);
                text.setText(inputNum_1);
                break;
            case R.id.back:
                backSpace();
                break;
            default:
                break;
        }
    }

    // 计算结果
    private void withResult(){
        if (!ope.equals("")) {  // 进行了运算
            inputNum_2 = text.getText().toString();
            double re = 0;  // 结果
            if (inputNum_2.equals("")) {    // 缺失第二个输入，则第二个输入和第一个输入一样
                inputNum_2 = inputNum_1;
            }
            double input1 = Double.parseDouble(inputNum_1);
            double input2 = Double.parseDouble(inputNum_2);
            if (ope.equals("+")){   // 加法
                re = input1 + input2;
            }else if (ope.equals("-")){ // 减法
                re = input1 - input2;
            }else if (ope.equals("x")) { // 乘法
                re = input1 * input2;
            }else {
                if (input2 != 0.0){
                    re = input1 / input2;
                }
            }

            // 格式化结果
            String newRe = formatNum(String.valueOf(re));

            // 显示算式
            String output = inputNum_1 + " " + ope + " "
                    + inputNum_2 + "\n=" + newRe;
            formula_text.setText(output);

            inputNum_1 = String.valueOf(newRe);    // 将结果赋值给   第一个数
            ope = "";
            inputNum_2 = "";
            text.setText(inputNum_1);   // 显示结果
        }

    }

    // 格式化数字
    private String formatNum(String str){
        if (str.contains(".")){
            int p = str.indexOf(".");
            int flag = 0;
            int i = 0;
            for (i = p+1; i < str.length(); i++){
                if (str.charAt(i) != '0'){
                    flag = 1;
                    break;
                }
            }
            if (flag == 0 || (i - p > 12)){
                str = str.substring(0, p);
            }
        }
        return str;
    }

    // 显示数字在界面上
    private void showNum(Button button){
        String s = text.getText().toString();
        String button_text = button.getText().toString();
        if (!(s.contains(".") && button_text.equals("."))) {     // 如果不包含小数点或者输入的不是小数点
            if (s.charAt(0) == '0') { // 去除0
                if (button_text.equals(".")) {
                    text.setText(s + button_text);
                }else{
                    if (!button_text.equals("0")) {
                        text.setText(button_text);
                    }
                }
            } else {
                text.setText(s + button_text);
            }
        }
        Log.d(TAG, "showNum: "+text.getText().toString());

    }

    // 回退一个字符
    private void backSpace(){
        String str = text.getText().toString();
        text.setText(str.substring(0, str.length()-1));
        if (text.getText().toString().equals("")){
            text.setText("0");
        }
    }

    // 取%（除以100）
    private String div_100(){
        String s = text.getText().toString();

        if (s.contains(".")){   // 字符串包含小数点
            inputNum_1 = String.valueOf(Double.parseDouble(s)/100);
            return inputNum_1;
        }else {
            int num = Integer.parseInt(s);
            if (num % 100 == 0){    // 能整除100
                inputNum_1 = String.valueOf(num / 100);
                return inputNum_1;
            }else {
                inputNum_1 = String.valueOf(num / 100.0);
                return inputNum_1;
            }
        }
    }

    // 取反
    private String negate(){
        String s = text.getText().toString();
        String newStr = "";

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
        formula_text = findViewById(R.id.formula_text);

        clear = findViewById(R.id.clear);
        neg = findViewById(R.id.neg);
        per = findViewById(R.id.per);

        root = findViewById(R.id.root);
        sin = findViewById(R.id.sin);
        cos = findViewById(R.id.cos);
        back = findViewById(R.id.back);

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

    // 设置监听器
    private void setClickEvent(){

        clear.setOnClickListener(this);
        neg.setOnClickListener(this);
        per.setOnClickListener(this);

        root.setOnClickListener(this);
        sin.setOnClickListener(this);
        cos.setOnClickListener(this);
        back.setOnClickListener(this);

        add.setOnClickListener(this);
        sub.setOnClickListener(this);
        mul.setOnClickListener(this);
        div.setOnClickListener(this);
        result.setOnClickListener(this);

        point.setOnClickListener(this);
        num_0.setOnClickListener(this);
        num_1.setOnClickListener(this);
        num_2.setOnClickListener(this);
        num_3.setOnClickListener(this);
        num_4.setOnClickListener(this);
        num_5.setOnClickListener(this);
        num_6.setOnClickListener(this);
        num_7.setOnClickListener(this);
        num_8.setOnClickListener(this);
        num_9.setOnClickListener(this);

    }

}

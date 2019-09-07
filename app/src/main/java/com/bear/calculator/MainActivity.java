package com.bear.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Iterator;
import java.util.LinkedList;

import static com.bear.calculator.util.Calculate.*;
import static com.bear.calculator.util.judgeWithCompare.*;

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

    private Button left_parenthesis;    // 左括号
    private Button right_parenthesis;   // 右括号

    private Button add;             // 加
    private Button sub;             // 减
    private Button mul;             // 乘
    private Button div;             // 除
    private Button equal;          // 等于
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

    private String inputStr = "";                                   // 存储输入的字符
    private LinkedList<String> operators = new LinkedList<>();      // 存储操作符
    private StringBuffer sb = new StringBuffer();                   // 存储后缀表达式

    private String result = "";

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 隐藏标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        int orientation = getResources().getConfiguration().orientation;

        initLayout(orientation);       // 初始化变量
        setClickEvent(orientation);    // 设置监听器

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clear:
                inputStr = "";
                text.setText("");
                break;
            case R.id.neg:
                double t = 0;
                if (inputStr.contains("=")) {
                    if (!result.equals("")) {
                        t = Double.parseDouble(result);
                        t *= (-1);
                        inputStr = String.valueOf(t);
                        text.setText(inputStr);
                    }
                }else {
                    try {
                        t = Double.parseDouble(inputStr);
                        t *= (-1);
                        inputStr = String.valueOf(t);
                        text.setText(inputStr);
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.per:
                inputStr = judgePercent(inputStr);
                text.setText(inputStr);
                break;
            case R.id.left_parenthesis:
            case R.id.right_parenthesis:
                 inputStr = judgeParenthesis(((Button)v).getText().toString(), inputStr);
                 Log.d(TAG, "onClick: "+inputStr);
                 text.setText(inputStr);
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
            case R.id.add:
            case R.id.sub:
            case R.id.mul:
            case R.id.div:
                showInputStr((Button)v);
                text.setText(inputStr);
                break;
            case R.id.equal:
                result = "";
                if (!inputStr.endsWith(" ")) {
                    if (transferToPostfix(inputStr)) {
                        inputStr = inputStr + " = " + calculate();
                        text.setText(inputStr);
                    }
                }
                break;
            case R.id.root:
                break;
            case R.id.sin:
                break;
            case R.id.cos:
                break;
            case R.id.back:
                backSpace();
                break;
            default:
                break;
        }
    }

    // 显示输入在界面上
    private void showInputStr(Button button){
        String s = button.getText().toString();
        boolean flag = (s.equals("+") || s.equals("-") || s.equals("x")
                || s.equals("÷"));

        if (text.getText().toString().contains("=")){
            if (flag){  // 继续使用结果
                inputStr = result;
                text.setText(result);
            }else {     // 清除屏幕
                inputStr = "";
                text.setText("");
            }
        }


        if (s.equals("-")){     // 能够输入负数
            if (inputStr.equals("") || (inputStr.length() >= 2 && inputStr.charAt(inputStr.length()-2) == '(')){    // 在没有任何输入或者有左括号(的情况下可以输入-
                inputStr += s;
                return;
            }
        }

        if (inputStr.equals("") && (flag || s.equals("."))){        // 排除在没有输入的情况下输入不合法的字符
            return;
        }else {
            if (flag){  // 输入的是+-*/
                char ch = ' ';
                if (inputStr.length() < 2) {
                    if (inputStr.equals("-")){
                        return;
                    }
                    inputStr = inputStr + " " + s + " ";
                    return;
                }
                ch = inputStr.charAt(inputStr.length() - 2);
                boolean judge = ((ch == '+') || (ch == '-') || (ch == 'x') || (ch == '÷')
                                    || (inputStr.charAt(inputStr.length()-1) == '-'));
                if (judge && (inputStr.charAt(inputStr.length()-1) == ' ')){     // 排除输入++等情况
                    return;
                }else {
                    inputStr = inputStr + " " + s + " ";      // 在每个操作符前后添加一个空格用于切分字符串
                }
            }else if (s.equals(".")){       // 输入的是小数点
                if (inputStr.charAt(inputStr.length()-1) == ' '){   // 排除+.等情况
                    return;
                }
                if (inputStr.contains(".")){    // 如果之前输入包含小数点
                    int p = inputStr.lastIndexOf(".");
                    String subInput = inputStr.substring(p+1);
                    if (subInput.contains("+") || subInput.contains("-") || subInput.contains("x")
                        || subInput.contains("÷")){     // 获取最后一个小数点后的字符串，如果包含运算符则输入有效
                        inputStr += s;
                    }else {
                        return;
                    }
                }else {
                    inputStr += s;
                }
            }else {     // 输入的是数字
                if (!inputStr.isEmpty()) {      // 之前的输入不为空
                    String[] arrayNum = inputStr.split(" ");
                    String lastNums = arrayNum[arrayNum.length-1];
                    if (lastNums != null) {
                        if (lastNums.contains(".")) {   // 如果包含小数点
                            inputStr += s;
                        } else{
                            if (lastNums.charAt(0) == '0'){
                                if (s.equals("0")){     //排除00的情况
                                    return;
                                }else{
                                    inputStr = inputStr.substring(0, inputStr.length()-2) + s;
                                }
                            }else{
                                inputStr += s;
                            }
                        }
                    }else {
                        inputStr += s;
                    }
                }else {
                    inputStr += s;
                }
            }
        }

    }

    // 回退一个字符
    private void backSpace(){
        if (!inputStr.contains("=")) {

            if (inputStr.equals("")) {     // 全部清除了
                return;
            }

            int backLen = 1;
            if (inputStr.endsWith(" ")){
                backLen = 2;
            }
            inputStr = inputStr.substring(0, inputStr.length() - backLen);
            text.setText(inputStr);
        }
    }

    // 中缀表达式转为后缀表达式
    private boolean transferToPostfix(String str){

        if (judgeFormula(str) != 0){
            return false;
        }
        String[] list = str.split(" ");
        int p = 0;
        while (p < list.length) {
            String s = list[p];
            p++;
            if (isOperator(s)) {    // 如果s是操作符
                if (operators.isEmpty()) {  // 操作符栈为空
                    operators.push(s);
                }else{
                    // 如果读入的操作符不是")"且优先级比栈顶元素的优先级高，则将操作符压入栈
                    if (!s.equals(")") && priority(operators.peek()) < priority(s)) {
                        operators.push(s);
                    }else if(!s.equals(")") && priority(operators.peek()) >= priority(s)){
                        while (operators.size()!=0 && priority(operators.peek()) >= priority(s)
                                && !operators.peek().equals("(")) {
                            if (!operators.peek().equals("(")) {
                                String operator=operators.pop();
                                sb.append(operator).append(" ");
                            }
                        }
                        operators.push(s);
                    } else if (s.equals(")")) {
                        // 如果读入的操作符是")"，则弹出从栈顶开始第一个"("及其之前的所有操作符
                        while (!operators.peek().equals("(")) {
                            String operator=operators.pop();
                            sb.append(operator).append(" ");
                        }
                        // 弹出"("
                        operators.pop();
                    }
                }
            }else {  // 读入的为非操作符
                sb.append(s).append(" ");
            }

        }
        if (!operators.isEmpty()) {     // 可能最后还会有一个操作符在栈中
            Iterator<String> iterator=operators.iterator();
            while (iterator.hasNext()) {
                String operator=iterator.next();
                sb.append(operator).append(" ");
            }
        }
        Log.d(TAG, "transferToPostfix: " + sb);
        return true;
    }

    // 计算后缀表达式
    private String calculate(){
        LinkedList<String> numList = new LinkedList<>();    // 储存计算的结果
        String[] opWithNumStr = sb.toString().split(" ");
        for (String s : opWithNumStr){
            if (isOperator(s)){ // 遇到操作符则计算
                if (!numList.isEmpty()){
                    double num1 = 0;
                    double num2 = 0;
                    double newNum = 0;
                    if (s.equals("%")){ // 弹出一个字符串
                        num1 = Double.parseDouble(numList.pop());
                        newNum = oneDigitOperation(num1, s);
                    }else { // 弹出两个字符串
                        num1 = Double.parseDouble(numList.pop());
                        num2 = Double.parseDouble(numList.pop());
                        if (s.equals("÷") && num1 == 0) {
                            return "ERROR";
                        }
                        newNum = twoDigitOperation(num2, num1, s);
                    }
                    numList.push(String.valueOf(newNum));   // 将获得的结果压入栈
                }
            }else {     // 遇到数字则直接入栈
                numList.push(s);
            }
        }
        sb = new StringBuffer();
        operators.clear();
        if (!numList.isEmpty()){
            result = numList.pop();
        }else {
            result = "ERROR";
        }
        return result;
    }

    // 初始化变量
    private void initLayout(int land){

        if (land == 2){     // 横屏
            neg = findViewById(R.id.neg);
            root = findViewById(R.id.root);
            sin = findViewById(R.id.sin);
            cos = findViewById(R.id.cos);
            left_parenthesis = findViewById(R.id.left_parenthesis);
            right_parenthesis = findViewById(R.id.right_parenthesis);
        }

        text = findViewById(R.id.text_view);
        formula_text = findViewById(R.id.formula_text);

        clear = findViewById(R.id.clear);
        per = findViewById(R.id.per);
        back = findViewById(R.id.back);

        add = findViewById(R.id.add);
        sub = findViewById(R.id.sub);
        mul = findViewById(R.id.mul);
        div = findViewById(R.id.div);
        equal = findViewById(R.id.equal);

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
    private void setClickEvent(int land){

        if (land == 2){     // 横屏
            neg.setOnClickListener(this);
            root.setOnClickListener(this);
            sin.setOnClickListener(this);
            cos.setOnClickListener(this);
            left_parenthesis.setOnClickListener(this);
            right_parenthesis.setOnClickListener(this);
        }

        clear.setOnClickListener(this);
        per.setOnClickListener(this);
        back.setOnClickListener(this);

        add.setOnClickListener(this);
        sub.setOnClickListener(this);
        mul.setOnClickListener(this);
        div.setOnClickListener(this);
        equal.setOnClickListener(this);

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

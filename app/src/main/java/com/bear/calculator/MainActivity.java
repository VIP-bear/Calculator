package com.bear.calculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bear.calculator.model.History;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import static com.bear.calculator.util.Calculate.*;
import static com.bear.calculator.util.judgeWithCompare.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText text;          // 文本框

    private Button clear;           // 清零
    private Button neg;             // 取相反数
    private Button per;             // 取百分数

    private Button square_root;     // 平方根
    private Button cube_root;       // 立方根
    private Button n_root;          // n次方根
    private Button squaare;         // 平方
    private Button n_square;        // n次方
    private Button sin;             // sin
    private Button cos;             // cos
    private Button tan;             // tan
    private Button ln;              // ln
    private Button log;             // log
    private Button back;            // 回退

    private Button pi;              // Π
    private Button e;               // e

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

    private String result = "";     // 计算结果

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int orientation = getResources().getConfiguration().orientation;    // 获取横竖屏值
        if (orientation == 2){
            // 横屏隐藏标题栏
            if (getSupportActionBar() != null){
                getSupportActionBar().hide();
            }
        }

        initLayout(orientation);       // 初始化变量
        setClickEvent(orientation);    // 设置监听器

        // 读取保存的数据
        SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
        inputStr = preferences.getString("editview_text", "");
        if (!inputStr.equals("")){
            text.setText(inputStr);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clear:
                inputStr = "";
                text.setText("");
                break;
            case R.id.neg:
                negate();
                break;
            case R.id.per:
                inputStr = judgePercent(inputStr);
                text.setText(inputStr);
                break;
            case R.id.left_parenthesis:
            case R.id.right_parenthesis:
                 inputStr = judgeParenthesis(((Button)v).getText().toString(), inputStr);
                 text.setText(inputStr);
                 break;
            case R.id.square_root:
            case R.id.cube_root:
            case R.id.sin:
            case R.id.cos:
            case R.id.tan:
            case R.id.ln:
            case R.id.log:
                inputStr = judgeRootTrigLog(((Button)v).getText().toString(), inputStr);
                text.setText(inputStr);
                break;
            case R.id.square:
            case R.id.n_square:
            case R.id.n_root:
                inputStr = judgeSquare(((Button)v).getText().toString(), inputStr);
                text.setText(inputStr);
                break;
            case R.id.back:
                backSpace();
                break;
            case R.id.pi:
            case R.id.e:
                inputStr = judgePIOrE(((Button)v).getText().toString(), inputStr);
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
                if (!inputStr.endsWith(" ") && !inputStr.contains("=") && !inputStr.equals("")) {
                    if (transferToPostfix(inputStr)) {
                        inputStr = inputStr + " = " + calculate();
                        text.setText(inputStr);
                        saveHistory(inputStr);
                    }
                }
                break;
            default:
                break;
        }
    }

    // 取反
    private void negate(){
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
            if (inputStr.equals("") || (inputStr.length() >= 2 &&
                    inputStr.charAt(inputStr.length()-2) == '(')){    // 在没有任何输入或者有左括号(的情况下可以输入-
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
                if (!inputStr.equals("")) {      // 之前的输入不为空
                    String[] arrayNum = inputStr.split(" ");
                    String lastNums = arrayNum[arrayNum.length-1];
                    if (lastNums != null) {
                        if (lastNums.equals("Π") || lastNums.equals("e")){
                            return;
                        }
                        if (lastNums.contains(".")) {   // 如果包含小数点
                            inputStr += s;
                        } else{
                            if (lastNums.equals("0") || lastNums.equals("-0")){
                                if (s.equals("0")){     //排除00的情况
                                    return;
                                }else{
                                    if (inputStr.length() > 1) {
                                        inputStr = inputStr.substring(0, inputStr.length() - 2) + s;
                                    }else {
                                        if (inputStr.equals("-")){
                                            inputStr += s;
                                        }else{
                                            inputStr = s;
                                        }
                                    }
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
                    try {
                        if (s.equals("%") || s.equals("sin") || s.equals("cos")
                                || s.equals("tan") || s.equals("√")
                                || s.equals("³√") || s.equals("ln")
                                || s.equals("log")) { // 弹出一个字符串
                            num1 = Double.parseDouble(numList.pop());
                            newNum = oneDigitOperation(num1, s);
                        } else { // 弹出两个字符串
                            num1 = Double.parseDouble(numList.pop());
                            num2 = Double.parseDouble(numList.pop());
                            if (s.equals("÷") && num1 == 0) {
                                return "ERROR";
                            }
                            newNum = twoDigitOperation(num2, num1, s);
                        }
                        newNum = Double.parseDouble(String.format("%.12f", newNum));    // 最多保留12位小数
                        numList.push(String.valueOf(newNum));   // 将获得的结果压入栈
                    }catch (NumberFormatException e){
                        return inputStr;
                    }
                }
            }else {     // 遇到数字则直接入栈
                if (s.equals("Π")){
                    numList.push(String.valueOf(Math.PI));
                }else if (s.equals("e")){
                    numList.push(String.valueOf(Math.E));
                }else {
                    numList.push(s);
                }
            }
        }
        sb = new StringBuffer();
        operators.clear();
        if (!numList.isEmpty()){
            result = numList.pop();
        }else {
            result = "ERROR";
        }
        if (result.endsWith(".0")){
            result = result.substring(0, result.length()-2);
        }
        return result;
    }

    // 初始化变量
    private void initLayout(int land){

        if (land == 2){     // 横屏
            neg = findViewById(R.id.neg);
            square_root = findViewById(R.id.square_root);
            cube_root = findViewById(R.id.cube_root);
            n_root = findViewById(R.id.n_root);
            squaare = findViewById(R.id.square);
            n_square = findViewById(R.id.n_square);
            sin = findViewById(R.id.sin);
            cos = findViewById(R.id.cos);
            tan = findViewById(R.id.tan);
            ln = findViewById(R.id.ln);
            log = findViewById(R.id.log);
            left_parenthesis = findViewById(R.id.left_parenthesis);
            right_parenthesis = findViewById(R.id.right_parenthesis);
            pi = findViewById(R.id.pi);
            e = findViewById(R.id.e);
        }

        text = findViewById(R.id.text_view);

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
            square_root.setOnClickListener(this);
            cube_root.setOnClickListener(this);
            n_root.setOnClickListener(this);
            squaare.setOnClickListener(this);
            n_square.setOnClickListener(this);
            sin.setOnClickListener(this);
            cos.setOnClickListener(this);
            tan.setOnClickListener(this);
            ln.setOnClickListener(this);
            log.setOnClickListener(this);
            left_parenthesis.setOnClickListener(this);
            right_parenthesis.setOnClickListener(this);
            pi.setOnClickListener(this);
            e.setOnClickListener(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 保存editview中的内容
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("editview_text", inputStr);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 加载菜单
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        String[] data;
        switch (item.getItemId()){
            case R.id.length:
                data = new String[]{"厘米cm", "米m", "千米km", "英寸in"};
                intent = new Intent(MainActivity.this, Conversion.class);
                intent.putExtra("id", 1);
                intent.putExtra("name", "长度换算");
                intent.putExtra("data", data);
                startActivity(intent);
                finish();
                break;
            case R.id.volume:
                data = new String[]{"cm³", "m³", "毫升ml", "升L"};
                intent = new Intent(MainActivity.this, Conversion.class);
                intent.putExtra("id", 2);
                intent.putExtra("name", "体积换算");
                intent.putExtra("data", data);
                startActivity(intent);
                finish();
                break;
            case R.id.decimal:
                data = new String[]{"2进制", "8进制", "l0进制", "16进制"};
                intent = new Intent(MainActivity.this, Conversion.class);
                intent.putExtra("id", 3);
                intent.putExtra("name", "进制换算");
                intent.putExtra("data", data);
                startActivity(intent);
                finish();
                break;
            case R.id.rate:
                data = new String[]{"人民币", "美元", "汇率", "更新时间"};
                intent = new Intent(MainActivity.this, Conversion.class);
                intent.putExtra("id", 4);
                intent.putExtra("name", "实时汇率");
                intent.putExtra("data", data);
                startActivity(intent);
                finish();
                break;
            case R.id.date:
                Intent intent1 = new Intent(MainActivity.this, DataCalculator.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.hihstory:
                Intent intent2 = new Intent(MainActivity.this, HistoryCalculate.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.help:
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Help")
                        .setMessage("You can do ...")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this, "欢迎使用计算器",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                break;
            default:
                break;
        }
        return true;
    }

    // 保存计算历史
    private void saveHistory(String inputStr){
        LitePal.getDatabase();
        int nums = LitePal.findAll(History.class).size();
        History history = new History();
        history.setId(nums+1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd :HH-mm-ss");
        Date date = new Date(System.currentTimeMillis());
        history.setStr(inputStr + "   时间:" + simpleDateFormat.format(date));
        history.save();
    }

}

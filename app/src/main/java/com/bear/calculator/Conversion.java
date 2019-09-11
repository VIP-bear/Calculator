package com.bear.calculator;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bear.calculator.model.Rate;
import com.google.gson.Gson;

import static com.bear.calculator.util.Calculate.*;

public class Conversion extends AppCompatActivity {

    private TextView title;

    private TextView viewInput;     // 显示输入

    // 单位
    private TextView unit1;
    private TextView unit2;
    private TextView unit3;
    private TextView unit4;

    // 输入
    private EditText input1;
    private EditText input2;
    private EditText input3;
    private EditText input4;

    private Button con;     // 换算
    private Button empty;   // 清空
    private ImageButton back;   // 返回

    private String[] data;  // 单位
    private int id;
    private String name;

    private int flag = 0;   // 标记那个输入框输入了数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);

        // 去除标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 获取传过来的数据
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        name = intent.getStringExtra("name");
        data = intent.getStringArrayExtra("data");

        initLayout();
        setData();

        if (id == 4){
            input3.setFocusable(false);
            input4.setFocusable(false);
        }

        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealWithInput();
            }
        });
        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input1.setText("");
                input2.setText("");
                input3.setText("");
                input4.setText("");
                viewInput.setText("");
                flag = 0;
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Conversion.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void initLayout(){
        title = findViewById(R.id.name);
        viewInput = findViewById(R.id.view_input);
        unit1 = findViewById(R.id.unit1);
        unit2 = findViewById(R.id.unit2);
        unit3 = findViewById(R.id.unit3);
        unit4 = findViewById(R.id.unit4);
        input1 = findViewById(R.id.input1);
        input2 = findViewById(R.id.input2);
        input3 = findViewById(R.id.input3);
        input4 = findViewById(R.id.input4);
        con = findViewById(R.id.con);
        empty = findViewById(R.id.empty);
        back = findViewById(R.id.back);
    }

    private void setData(){
        title.setText(name);
        unit1.setText(data[0]);
        unit2.setText(data[1]);
        unit3.setText(data[2]);
        unit4.setText(data[3]);
    }

    private void dealWithInput(){
        int num = 0;    // 记录有几个输入框有数据
        String input = "";   // 记录输入
        if (!input1.getText().toString().equals("")){
            num++;
            flag = 1;
            input = input1.getText().toString();
        }
        if (!input2.getText().toString().equals("")){
            num++;
            flag = 2;
            input = input2.getText().toString();
        }
        if (!input3.getText().toString().equals("")){
            num++;
            flag = 3;
            input = input3.getText().toString();
        }
        if (!input4.getText().toString().equals("")){
            num++;
            flag = 4;
            input = input4.getText().toString();
        }

        if (num != 1){  // 多个输入框中有数据
            return;
        }

        double t = 0;
        if (id == 1 || id == 2 || id == 4){
            try{
                t = Double.parseDouble(input);
            }catch (NumberFormatException e){
                return;
            }
        }
        String[] results;
        if (id == 1){   // 长度转换
            results = lengthConversion(flag, t);
            viewData(results);
        }else if (id == 2){ // 体积转换
            results = volumeConversion(flag, t);
            viewData(results);
        }else if (id == 3){ // 进制转换
            results = decimalConversion(flag, input);
            if (results == null){
                return;
            }
            viewData(results);
        }else {     // 实时汇率
            sendRequestWithOkHttp(flag, t);
        }
        viewInput.setText(input + "(" + data[flag-1] + ")");

    }

    private void viewData(String[] results){
        input1.setText(results[0]);
        input2.setText(results[1]);
        input3.setText(results[2]);
        input4.setText(results[3]);
    }

    // 发送网络请求并获得返回的数据
    private void sendRequestWithOkHttp(final int flag, final double t){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://haobaoshui.com:8008/exchangerate/v1/rate?scur=usd&tcur=cny")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithGSON(responseData, flag, t);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 解析json数据,并且更新界面
    private void parseJSONWithGSON(String jsonData, int flag, double t){
        Gson gson = new Gson();
        //List<Rate> rateList = gson.fromJson(jsonData, new TypeToken<List<Rate>>(){}.getType());
        Rate rate = gson.fromJson(jsonData, Rate.class);
        final String[] results = rateConversion(rate, flag, t);

        // 切换到主线程更新界面
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewData(results);
            }
        });
    }

}

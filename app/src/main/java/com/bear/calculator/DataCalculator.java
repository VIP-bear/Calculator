package com.bear.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bear.calculator.util.Calculate;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DataCalculator extends AppCompatActivity implements View.OnClickListener {

    private TextView startDate; // 开始日期

    private TextView endDate;   // 截至日期

    private TextView subDate;   // 相差天数

    private Button calDate;     // 计算

    private ImageButton back;   // 返回

    private static final String TAG = "DataCalculator";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_calculator);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        startDate = findViewById(R.id.start_data);
        endDate = findViewById(R.id.end_data);
        subDate = findViewById(R.id.sub_date);
        calDate = findViewById(R.id.cal_date);
        back = findViewById(R.id.from_date_back);

        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        calDate.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_data:
                new DatePickerDialog(DataCalculator.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String start = year + "-" + (month+1) + "-" + dayOfMonth;
                        startDate.setText(start);
                    }
                }, 2019, 8, 11).show();
                break;
            case R.id.end_data:
                new DatePickerDialog(DataCalculator.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String end = year + "-" + (month+1) + "-" + dayOfMonth;
                        Log.d(TAG, "onDateSet: " + end);
                        endDate.setText(end);
                    }
                }, 2019, 8, 11).show();
                break;
            case R.id.from_date_back:
                Intent intent = new Intent(DataCalculator.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.cal_date:
                if (!startDate.getText().toString().isEmpty() && !endDate.getText().toString().isEmpty()){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String fromDate = startDate.getText().toString();
                    String toDate = endDate.getText().toString();
                    long from = 0;
                    long to = 0;
                    try {
                        from = simpleDateFormat.parse(fromDate).getTime();
                        to = simpleDateFormat.parse(toDate).getTime();
                    }catch (ParseException e){
                        e.printStackTrace();
                    }
                    int days = (int) ((to - from) /( 1000 * 60 * 60 * 24));
                    subDate.setText(String.valueOf(days));
                }
                break;
        }
    }
}

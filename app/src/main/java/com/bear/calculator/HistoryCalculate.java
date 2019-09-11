package com.bear.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.bear.calculator.model.History;

import org.litepal.LitePal;

import java.util.List;

public class HistoryCalculate extends AppCompatActivity {

    private ListView listView;

    private ImageButton back;   //返回

    private String[] data;      // 存储历史记录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_calculate);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        // 查询数据
        List<History> list = LitePal.findAll(History.class);
        data = new String[list.size()];
        for (int i = 0; i < list.size(); i++){
            data[i] = list.get(i).getStr();
        }
        listView = findViewById(R.id.list_view);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(HistoryCalculate.this,
                android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);

        back = findViewById(R.id.history_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryCalculate.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

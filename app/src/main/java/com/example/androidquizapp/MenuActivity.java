package com.example.androidquizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    private ArrayAdapter<String> Adapter;
    private ListView list;
    int rootNum;
    private TextView tv;
    private ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        list = findViewById(R.id.quizList);
        tv = findViewById(R.id.tv);
        tv.setText(R.string.app_name);

        data = new ArrayList<>();

        data.add("地理問題");
        data.add("計算問題");
        data.add("画像問題");
        data.add("音当て問題");
        Adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                data);
        list.setAdapter(Adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position,
                                    long id) {
                CharSequence msg = ((TextView) view).getText();
                int pos = Adapter.getPosition((String) msg);
                switch (pos) {
                    case 0:
                        tv.setText(Adapter.getItem(position));
                        rootNum = 1;
                        break;
                    case 1:
                        tv.setText(Adapter.getItem(position));
                        rootNum = 2;
                        break;
                    case 2:
                        tv.setText(Adapter.getItem(position));
                        rootNum = 3;
                        break;
                    case 3:
                        tv.setText(Adapter.getItem(position));
                        rootNum = 4;
                        break;
                }
            }
        });
    }

    public void btnNext(View view) {
        if (rootNum != 0) {
            switch (rootNum) {
                case 1:
                    Intent intent1 = new Intent(
                            this, StringQuizActivity.class);
                    startActivity(intent1);
                    finish();
                    break;
                case 2:
                    Intent intent2 = new Intent(
                            this, MathQuizActivity.class);
                    startActivity(intent2);
                    finish();
                    break;
                case 3:
                    Intent intent3 = new Intent(
                            this, ImageQuizActivity.class);
                    startActivity(intent3);
                    finish();
                    break;
                case 4:
                    Intent intent4 = new Intent(
                            this, SoundQuizActivity.class);
                    startActivity(intent4);
                    finish();
                    break;

            }

        }

    }
}

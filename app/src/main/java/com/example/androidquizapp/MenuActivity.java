package com.example.androidquizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private ArrayAdapter<String> Adapter;//アダプター
    private int rootNum;//メニューの問題番号
    private TextView tv;//テキストビュー
    private Button btnNext;//次へボタン
    //各問題を配列化
    private final static String[] questions={
            "地理問題","計算問題","画像問題","音当て問題"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //問題文リストレイアウトの編集用アダプター
        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(
                this, R.layout.list);

        //使用するViewの呼び出し
        ListView _list = findViewById(R.id.quizList);
        btnNext=findViewById(R.id.btnNext);
        tv = findViewById(R.id.tv);

        tv.setText(R.string.menu_title);//タイトル文

        //アダプターに問題タイトルを格納
        for (String str:questions) {
            arrayAdapter.add(str);
        }

        _list.setAdapter(arrayAdapter);

        //リスナーを設定
        _list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position,
                                    long id) {
                CharSequence msg = ((TextView) view).getText();
                int pos = arrayAdapter.getPosition((String) msg);
                //いずれかの問題が選択されたら次へ進むボタンを表示
                btnNext.setVisibility(View.VISIBLE);
                //rootNumに押されたボタンを割り当てて内容を表示
                switch (pos) {
                    case 0:
                        tv.setText(arrayAdapter.getItem(position));
                        rootNum = 1;
                        break;
                    case 1:
                        tv.setText(arrayAdapter.getItem(position));
                        rootNum = 2;
                        break;
                    case 2:
                        tv.setText(arrayAdapter.getItem(position));
                        rootNum = 3;
                        break;
                    case 3:
                        tv.setText(arrayAdapter.getItem(position));
                        rootNum = 4;
                        break;
                }
            }
        });
    }

    /**
     * 次へボタンをタップした際の処理
     * @param view view
     */
    public void btnNext(View view) {
        if (rootNum != 0) {
            //インテントにアクティビティを振り分けrootNumで制御
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

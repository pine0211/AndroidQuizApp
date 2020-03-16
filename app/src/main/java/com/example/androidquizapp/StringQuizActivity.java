package com.example.androidquizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;

public class StringQuizActivity extends AppCompatActivity {
    private TextView tv;//解説・説明テキスト部
    private TextView Q;//問題文表示部
    private Button next;//次へボタン
    private String trueAns;//該当する問題の正解
    private String pushAns;//ラジオボタンで選択された解答
    private int count;//問題の配列番号

    //ラジオグループとボタン
    private RadioGroup rGroup;
    private RadioButton btn1, btn2, btn3, btn4;

    private int ansCount;//正解数

    //ArrayList名qArrayを作成
    ArrayList<ArrayList<String>> qArray = new ArrayList<>();

    //問題文と解答をつなげた配列qTextを作成
    private final String[][] qText = {
            {"次のうち一番面積が大きいのは？\n", "北海道", "岩手県", "福島県", "長野県"},
            {"次のうち一番人口が多いのは？\n", "東京都", "神奈川県", "大阪府", "愛知県"},
            {"次のうち一番長い川は？\n", "信濃川", "利根川", "石狩川", "最上川"},
            {"次のうち一番多く島を持つのは？\n", "長崎県", "鹿児島県", "沖縄県", "東京都"},
            {"次のうち記録上一番台風が上陸したのは？\n", "鹿児島県", "高知県", "和歌山県", "沖縄県"},

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //使用するviewの呼び出し
        rGroup = findViewById(R.id.rGroup);
        Q = findViewById(R.id.ans);
        next = findViewById(R.id.btnNext);
        tv = findViewById(R.id.drill);
        btn1 = findViewById(R.id.rdBtn1);
        btn2 = findViewById(R.id.rdBtn2);
        btn3 = findViewById(R.id.rdBtn3);
        btn4 = findViewById(R.id.rdBtn4);

        //タイトルと案内文を表示
        tv.setText(R.string.q_str);
        Q.setText(R.string.menu_string);

        //問題に進むまで解答用ラジオボタンは非表示
        rGroup.setVisibility(View.GONE);

        //問題番号と点数、解答文字列を初期化しておく
        count = 0;
        ansCount = 0;
        pushAns = null;

        //ActionBarの生成
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * ActionBar内の「戻る」ボタンを押したときの処理
     *
     * @param item アクションバーのタップ判定
     * @return タップ判定のtrue/false
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();//タップしたら終了
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 次へボタンをタップした際の処理
     *
     * @param view 次へボタンのタップ判定
     */
    public void btnNext(View view) {
        //前の問題に解答している場合
        if (pushAns != null) {
            check();//判定メソッドへ
        }
        //解答ボタンを選択するまで「次へ」を隠す
        next.setVisibility(View.GONE);
        //すべての問題を解き終わったら
        if (count >= qText.length) {
            //DBに回答結果を書き込み
            SQLOpenHelper helper = new SQLOpenHelper(getApplicationContext());
            ContentValues values = new ContentValues();
            SQLiteDatabase dbw = helper.getWritableDatabase();
            values.put("score", ansCount);
            dbw.update("resultdb", values, "_id = 1", null);
            dbw.close();
            //結果画面へ
            Intent i = new Intent(this, ResultActivity.class);
            startActivity(i);
            finish();
            //まだ問題が残っている場合
        } else {
            question();//問題生成メソッドへ
        }
    }

    /**
     * 問題がまだ残っている場合の処理
     */
    private void question() {
        //問題文と解答を順番に格納し、リストqArrayに送る
        for (String[] string : qText) {
            ArrayList<String> tmpArray = new ArrayList<>();
            tmpArray.add(string[0]);
            tmpArray.add(string[1]);
            tmpArray.add(string[2]);
            tmpArray.add(string[3]);
            tmpArray.add(string[4]);

            qArray.add(tmpArray);
        }
        //qArrayのcount番目のデータを変数quizに入れておく
        final ArrayList<String> quiz = qArray.get(count);

        //問題文と解答用ボタンを表示し問題の番号表示
        Q.setVisibility(View.VISIBLE);
        rGroup.setVisibility(View.VISIBLE);
        String qNumber = "問題" + (count + 1);
        tv.setText(qNumber);
        Q.setText(quiz.get(0));//問題文をセット
        trueAns = quiz.get(1);//正解をセット
        quiz.remove(0);//問題文を削除

        //残った正解と解答文の順番をシャッフル
        Collections.shuffle(quiz);
        //ボタンにセット
        btn1.setText(quiz.get(0));
        btn2.setText(quiz.get(1));
        btn3.setText(quiz.get(2));
        btn4.setText(quiz.get(3));
        //4択部分の分岐処理
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdBtn1:
                        pushAns = quiz.get(0);
                        break;
                    case R.id.rdBtn2:
                        pushAns = quiz.get(1);
                        break;
                    case R.id.rdBtn3:
                        pushAns = quiz.get(2);
                        break;
                    case R.id.rdBtn4:
                        pushAns = quiz.get(3);

                }
                //未選択でなければ
                if (checkedId != -1) {
                    //次へボタンを表示
                    next.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    /**
     * ボタンが選択されpushAnsに解答がある場合の処理
     */
    private void check() {
        //選択された解答の正誤判定および加点
        if (pushAns.equals(trueAns)) {
            ansCount++;
        }
        count++;//次の問題番号に進める
        pushAns = null;//解答部分を初期化
        rGroup.clearCheck();//ボタン選択解除
    }

}

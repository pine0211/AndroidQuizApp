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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class ImageQuizActivity extends AppCompatActivity {
    private TextView tv;//解説・説明テキスト部
    private TextView Q;//問題文表示部
    private Button next;//次へボタン
    private String trueAns;//該当する問題の正解
    private String pushAns;//ラジオボタンで選択された解答
    private int count;//問題の配列番号
    //ラジオグループとボタン
    private RadioGroup rGroup;
    private RadioButton btn1, btn2, btn3, btn4;
    private ImageView view;

    private int ansCount;//正解数

    //ArrayList名qArrayを作成
    ArrayList<ArrayList<String>> qArray = new ArrayList<>();

    //問題文と解答をつなげた配列qTextを作成
    private final String[][] qText = {
            {"この犬の犬種は？\n", "チワワ", "柴犬", "ダックスフンド", "ゴールデンレトリバー"},
            {"この犬の犬種は？\n", "ダックスフンド", "柴犬", "プードル", "ゴールデンレトリバー"},
            {"この犬の犬種は？\n", "ゴールデンレトリバー", "チワワ", "プードル", "柴犬"},
            {"この犬の犬種は？\n", "プードル", "ダックスフンド", "チワワ", "ゴールデンレトリバー"},
            {"この犬の犬種は？\n", "柴犬", "ダックスフンド", "チワワ", "プードル"}
    };
    private Integer[] qImage;



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
        view = findViewById(R.id.img1);

        //タイトルと案内文を表示
        tv.setText(R.string.q_img);
        Q.setText(R.string.menu_img);

        //問題に進むまで解答用ラジオボタンと画像表示部分は非表示
        rGroup.setVisibility(View.GONE);
        view.setVisibility(View.GONE);

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
            finish();
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
            dbw.update("resultdb"
                    , values
                    , "_id = 3"
                    , null);
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
        //問題に対応する画像ファイルの場所を配列化
        Integer[] qImage ={
                R.drawable.dog_chihuahua
                , R.drawable.dog_dachshund_blacktan
                , R.drawable.dog_golden_retriever
                , R.drawable.dog_poodle
                , R.drawable.dog_shibainu_brown};

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
        view.setVisibility(View.VISIBLE);
        String qNumber="問題" + (count + 1);
        tv.setText(qNumber);

        Q.setText(quiz.get(0));//問題文をセット
        //画像をセット
        view.setImageResource(qImage[count]);

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

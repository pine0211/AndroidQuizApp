package com.example.androidquizapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class SoundQuizActivity extends AppCompatActivity {
    private TextView tv;//解説・説明テキスト部
    private TextView Q;//問題文表示部
    private Button next;//次へボタン
    private String trueAns;//該当する問題の正解
    private String pushAns;//ラジオボタンで選択された解答
    private int count;//問題の配列番号
    //ラジオグループとボタン
    private RadioGroup rGroup;
    private RadioButton btn1, btn2, btn3, btn4;
    private Button btnPlay;//再生ボタン
    private MediaPlayer mediaPlayer;//再生プレイヤー
    private Integer[] qSound;//ファイル格納用
    private int ansCount;//正解数

    //問題文と解答をつなげた配列qTextを作成
    private final String[][] qText = {
            {"この鳴き声の鳥は？\n", "カラス", "ニワトリ", "スズメ", "ウグイス"},
            {"この鳴き声の鳥は？\n", "ニワトリ", "スズメ", "ウグイス", "ウミネコ"},
            {"この鳴き声の鳥は？\n", "スズメ", "ウグイス", "ウミネコ", "カラス"},
            {"この鳴き声の鳥は？\n", "ウグイス", "ウミネコ", "カラス", "ニワトリ"},
            {"この鳴き声の鳥は？\n", "ウミネコ", "カラス", "ニワトリ", "スズメ"}
    };
    //ArrayList名qArrayを作成
    private final ArrayList<ArrayList<String>> qArray = new ArrayList<>();

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
        btnPlay = findViewById(R.id.btnPlay);

        //タイトルと案内文を表示
        tv.setText(R.string.q_sound);
        Q.setText(R.string.menu_sound);

        //問題に進むまで解答用ラジオボタンと再生ボタンは非表示
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
            dbw.update("resultdb", values, "_id = 4", null);
            dbw.close();
            //結果画面へ
            Intent i=new Intent(this, ResultActivity.class);
            startActivity(i);
            finish();
            //まだ問題が残っている場合
        } else {
            question();//問題生成メソッドへ
        }
    }

    /**
     * 再生ボタンが押された時の処理
     * @param view view
     */
    public void btnPlay(View view) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mediaPlayer.start();//セットされた順番のファイル再生
    }

    /**
     * 問題がまだ残っている場合の処理
     */
    private void question() {
        //問題に対応する音声ファイルの場所を配列化
        Integer[] qSound =
                {R.raw.hashibutogarasu
                        , R.raw.niwatori
                        , R.raw.suzume
                        , R.raw.uguisu
                        , R.raw.umineko
                };
        //問題に対応する音声ファイルをセット
        mediaPlayer = MediaPlayer.create(this, qSound[count]);

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
        String qNumber="問題" + (count + 1);
        tv.setText(qNumber);
        Q.setText(quiz.get(0));//問題文をセット
        btnPlay.setVisibility(View.VISIBLE);

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

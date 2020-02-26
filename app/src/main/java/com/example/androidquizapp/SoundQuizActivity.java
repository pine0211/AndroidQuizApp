package com.example.androidquizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
    private Button btnPlay;
    private MediaPlayer mediaPlayer;
    private Integer[] qSound;
    public int ansCount;//正解数

    //問題文と解答をつなげた配列qTextを作成
    private String[][] qText = {
            {"この鳴き声の鳥は？\n", "カラス", "ニワトリ", "スズメ", "ウグイス"},
            {"この鳴き声の鳥は？\n", "ニワトリ", "スズメ", "ウグイス", "ウミネコ"},
            {"この鳴き声の鳥は？\n", "スズメ", "ウグイス", "ウミネコ", "カラス"},
            {"この鳴き声の鳥は？\n", "ウグイス", "ウミネコ", "カラス", "ニワトリ"},
            {"この鳴き声の鳥は？\n", "ウミネコ", "カラス", "ニワトリ", "スズメ"}
    };



    //ArrayList名qArrayを作成
    ArrayList<ArrayList<String>> qArray = new ArrayList<>();

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

        //最初に案内文を表示
        tv.setText("次へをタップで開始します。");

        //問題に進むまで解答用ラジオボタンと問題文と再生ボタンは非表示
        Q.setVisibility(View.GONE);
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
     * @param item
     * @return
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
     * 次へボタンをタップした際の処理     *
     *
     * @param view
     */
    public void btnNext(View view) {
        //前の問題に解答している場合
        if (pushAns != null) {
            check();
        }
        //解答ボタンを選択するまで「次へ」を隠す
        next.setVisibility(View.GONE);
        //すべての問題を解き終わったら
        if (count >= qText.length) {
            //問題文と解答ボタンを非表示にして合計点表示
            Q.setVisibility(View.GONE);
            rGroup.setVisibility(View.GONE);
            btnPlay.setVisibility(View.GONE);
            tv.setText("全問終了\n" + ansCount + "点です。");
            //まだ問題が残っている場合
        } else {
            question();
        }
    }

    public void btnPlay(View view) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mediaPlayer.start();
    }


    /**
     * 問題がまだ残っている場合の処理
     */
    private void question() {
        Integer[] qSound =
                {R.raw.hashibutogarasu
                        , R.raw.niwatori
                        , R.raw.suzume
                        , R.raw.uguisu
                        , R.raw.umineko
                };

        mediaPlayer = MediaPlayer.create(this, qSound[count]);

        //問題文と解答を順番に格納し、リストqArrayに送る
        for (int i = 0; i < qText.length; i++) {
            ArrayList<String> tmpArray = new ArrayList<>();
            tmpArray.add(qText[i][0]);
            tmpArray.add(qText[i][1]);
            tmpArray.add(qText[i][2]);
            tmpArray.add(qText[i][3]);
            tmpArray.add(qText[i][4]);

            qArray.add(tmpArray);
        }
        //qArrayのcount番目のデータを変数quizに入れておく
        final ArrayList<String> quiz = qArray.get(count);
        //問題文と解答用ボタンを表示し問題の番号表示
        Q.setVisibility(View.VISIBLE);
        rGroup.setVisibility(View.VISIBLE);
        tv.setText("問題" + (count + 1));

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

                if (checkedId != -1) {
                    next.setVisibility(View.VISIBLE);
                    mediaPlayer.stop();
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

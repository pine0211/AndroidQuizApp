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
import java.util.Random;

public class MathQuizActivity extends AppCompatActivity {
    private TextView tv;//解説・説明テキスト部
    private TextView Q;//問題文表示部
    private Button next;//次へボタン
    private String trueAns;//該当する問題の正解
    private String pushAns;//ラジオボタンで選択された解答
    private int count;//問題の配列番号
    //ラジオグループとボタン
    private RadioGroup rGroup;
    private RadioButton btn1, btn2, btn3, btn4;

    //ランダム生成する2つの値
    private ArrayList<ArrayList<Integer>> randNum;
    //生成された値の並び替え
    private ArrayList<ArrayList<Integer>> randInt;
    //答えと問題文を生成する
    private ArrayList<ArrayList<ArrayList<String>>> answer;
    //四則演算のどれを使うかをランダム化
    private ArrayList<Integer> Qnum;
    //問題数分の正解をまとめる
    private ArrayList<ArrayList<String>> Anum;
    //表示する問題文と回答項目を格納
    private ArrayList<ArrayList<String>> AnsGroup;

    private int ansCount;//正解数をカウント
    private Random r = new Random();//ランダム処理

    private int qLength = 5;//問題数

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
        tv.setText(R.string.q_math);
        Q.setText(R.string.menu_math);

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
        if (count >= qLength) {
            //DBに回答結果を書き込み
            SQLOpenHelper helper = new SQLOpenHelper(getApplicationContext());
            ContentValues values = new ContentValues();
            SQLiteDatabase dbw = helper.getWritableDatabase();
            values.put("score", ansCount);
            dbw.update("resultdb", values, "_id = 2", null);
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
        //解答ボタンを選択するまで「次へ」を隠す
        next.setVisibility(View.GONE);
        //問題用のランダム数値2つを5問分用意
        randNum = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < qLength; i++) {//問題数分作成
            ArrayList<Integer> rnum = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                rnum.add(r.nextInt(9) + 1);
            }
            randNum.add(rnum);
        }
        //数値2つのうち大きい方を最初に置く
        randInt = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < qLength; i++) {
            ArrayList<Integer> num = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                if (j == 0) {
                    num.add(Math.max(randNum.get(i).get(j), randNum.get(i).get(j + 1)));
                } else {
                    num.add(Math.min(randNum.get(i).get(j), randNum.get(i).get(j - 1)));
                }
                randInt.add(num);
            }
        }
        //答えと問題文テンプレートを問題数分用意
        answer = new ArrayList<ArrayList<ArrayList<String>>>();
        for (int i = 0; i < qLength; i++) {
            ArrayList<ArrayList<String>> numlist = new ArrayList<ArrayList<String>>();
            //4種類の問題
            for (int j = 0; j < 4; j++) {
                ArrayList<String> ansStr = new ArrayList<>();
                //3つの問題文構成部品
                for (int k = 0; k < 3; k++) {
                    if (k == 0) {
                        //扱う四則演算記号
                        switch (j) {
                            case 0:
                                ansStr.add(" + ");
                                break;
                            case 1:
                                ansStr.add(" - ");
                                break;
                            case 2:
                                ansStr.add(" × ");
                                break;
                            case 3:
                                ansStr.add(" ÷ ");
                                break;
                        }

                    } else if (k == 1) {
                        //生成された値から得られた計算結果
                        switch (j) {
                            case 0:
                                ansStr.add(String.valueOf(
                                        randInt.get(i).get(0) + randInt.get(i).get(1)));
                                break;
                            case 1:
                                ansStr.add(String.valueOf(
                                        randInt.get(i).get(0) - randInt.get(i).get(1)));
                                break;
                            case 2:
                                ansStr.add(String.valueOf(
                                        randInt.get(i).get(0) * randInt.get(i).get(1)));
                                break;
                            case 3:
                                ansStr.add(String.valueOf(
                                        randInt.get(i).get(0) / randInt.get(i).get(1)));
                                break;
                        }
                    } else if (k == 2) {
                        //割り算をintで扱うための小数点切り捨て表示
                        switch (j) {
                            case 3:
                                ansStr.add("\n*小数点以下は切り捨てとする。");
                                break;
                            default:
                                ansStr.add("");
                        }
                    }
                }
                numlist.add(ansStr);
            }
            answer.add(numlist);
        }
        //問題4種類のどれを選ぶかランダム化
        Qnum = new ArrayList<Integer>();
        for (int i = 0; i < qLength; i++) {
            Qnum.add(r.nextInt(4));
        }
        //問題数分の正解を用意
        Anum = new ArrayList<>();
        for (int i = 0; i < qLength; i++) {
            ArrayList<String> num = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                num.add(answer.get(i).get(Qnum.get(count)).get(1));
            }
            Anum.add(num);
        }
        //問題文と回答文を格納
        AnsGroup = new ArrayList<ArrayList<String>>();
        int ans, num2, num3, num4;
        for (int i = 0; i < qLength; i++) {
            ArrayList<String> num = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                ans = Integer.parseInt(Anum.get(i).get(0));
                switch (j) {
                    case 0:
                        //問題文
                        num.add(randInt.get(i).get(0)
                                + answer.get(i).get(Qnum.get(count)).get(0)
                                + randInt.get(i).get(1)
                                + "= ?\n"
                                + answer.get(i).get(Qnum.get(count)).get(2));
                        break;
                    case 1:
                        //正解
                        num.add(String.valueOf(ans));
                        break;
                    case 2:
                        //回答1
                        num2 = Integer.parseInt((Anum.get(i).get(0)) + r.nextInt(9));
                        if (Integer.parseInt(Anum.get(i).get(0)) == num2) {
                            num2 = num2 + 1;
                        }
                        num.add(String.valueOf(num2));
                        break;
                    case 3:
                        //回答2
                        int minus = Integer.parseInt(Anum.get(i).get(0)) - 1;
                        minus = minus - r.nextInt(9);
                        num.add(String.valueOf(minus));
                        break;
                    case 4:
                        //回答3
                        num.add(String.valueOf(
                                Integer.parseInt(Anum.get(i).get(0))
                                        * (r.nextInt(3) + 2)));
                        break;
                }
                AnsGroup.add(num);
            }
        }

        //AnsGroupのcount番目のデータを変数quizに入れておく
        final ArrayList<String> quiz = AnsGroup.get(count);

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
                //押されたボタンの内容をpushAnsに送る
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

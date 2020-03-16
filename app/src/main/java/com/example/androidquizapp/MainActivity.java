package com.example.androidquizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{
    private TextView tv;//スタート画面テキスト

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ビューを用意し点滅表示を設定
        tv = findViewById(R.id.drill);

        blinkText(tv, 1000, 500);
    }
    /**
     * アニメーション機能で点滅させるためのメソッド
     *
     * @param txtView  　スタート画面テキスト
     * @param duration 　点滅の間隔
     * @param offset   　再開までの待機時間
     */
    private void blinkText(TextView txtView, long duration, long offset) {
        //不透明度と透明度設定
        Animation anm = new AlphaAnimation(0.0f, 1.0f);
        anm.setDuration(duration);
        anm.setStartOffset(offset);
        anm.setRepeatMode(Animation.REVERSE);//繰り返し
        anm.setRepeatCount(Animation.INFINITE);//無期限
        txtView.startAnimation(anm);
    }
    /**
     * 画面をタップしてメニューへ進む処理
     *
     * @param view
     */
    public void onStartClick(View view) {
        Intent i = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(i);
    }
}

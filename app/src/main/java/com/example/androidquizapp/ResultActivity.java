package com.example.androidquizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    private TextView textView;
    private TextView resultView;
    private SQLOpenHelper helper;
    private int total;
    private Button del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        //DB作成
        helper = new SQLOpenHelper(getApplicationContext());
        ContentValues values = new ContentValues();
        SQLiteDatabase db = helper.getReadableDatabase();
        SQLiteDatabase dbw = helper.getWritableDatabase();

        textView = findViewById(R.id.res_finish);
        resultView = findViewById(R.id.res_list);
        del = findViewById(R.id.btn_delete);

        Cursor cursor = db.query("resultdb",//DB名
                new String[]{"title", "score"},//カラム名
                null,//WHERE句の列名
                null,//WHERE句の値
                null,//GROUP BY句の値
                null,//HAVING句の値
                null//ORDER BY句の値
        );
        cursor.moveToFirst();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < cursor.getCount() - 1; i++) {
            if (cursor.getInt(1) == -1) {
                cursor.moveToNext();
                continue;
            }
            total = total + cursor.getInt(1);
            builder.append(cursor.getString(0));
            builder.append(cursor.getInt(1));
            builder.append("点\n\n");
            cursor.moveToNext();
        }
        builder.append(cursor.getString(0));
        builder.append(total);
        builder.append("点\n\n");
        cursor.close();
        resultView.setText(builder.toString());

        textView.setText(R.string.finish);

        //ActionBarの生成
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void delete(View view) {
        deleteDatabase(helper.getDatabaseName());
        Context context=getApplicationContext();
        deleteDatabase(helper.getDatabaseName());
        Toast.makeText(context, R.string.msg_del,Toast.LENGTH_SHORT).show();
        resultView.setVisibility(View.GONE);

    }


}

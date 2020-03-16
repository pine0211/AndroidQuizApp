package com.example.androidquizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    private TextView textView;
    private TextView resultView;
    private SQLOpenHelper helper;
    private int total;
    private Cursor cursor;
    private StringBuilder builder;
    private ContentValues values;
    private SQLiteDatabase db;
    private SQLiteDatabase dbw;
    private Button del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        //DB作成
        helper = new SQLOpenHelper(getApplicationContext());
        values = new ContentValues();
        db = helper.getReadableDatabase();
        dbw = helper.getWritableDatabase();

        textView = findViewById(R.id.res_finish);
        resultView = findViewById(R.id.res_list);
        del = findViewById(R.id.btn_delete);

        cursor = db.query("resultdb",//DB名
                new String[]{"title", "score"},//カラム名
                null,//WHERE句の列名
                null,//WHERE句の値
                null,//GROUP BY句の値
                null,//HAVING句の値
                null//ORDER BY句の値
        );
        cursor.moveToFirst();
        builder = new StringBuilder();
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
        textView.setText(R.string.msg_del);
        resultView.setVisibility(View.GONE);
        del.setVisibility(View.GONE);

    }


}

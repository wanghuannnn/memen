package com.example.memento2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;



import android.app.DatePickerDialog;
import android.database.Cursor;

import android.widget.CursorAdapter;
import android.widget.DatePicker;

import android.widget.SimpleCursorAdapter;
import android.widget.Toast;



import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private EditText date,subject,body;
    private ListView result;
    private LinearLayout title;
    private MyDatabaseHelper mHelper;
    private SQLiteDatabase mDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new MyDatabaseHelper(this, "memento.db",null, 1);
        mDB = mHelper.getReadableDatabase();

        setContentView(R.layout.activity_main);

        date = (EditText) findViewById(R.id.date);
        subject = (EditText) findViewById(R.id.subject);
        body = (EditText) findViewById(R.id.body);
        result = (ListView) findViewById(R.id.result);
        title = (LinearLayout) findViewById(R.id.title);
        title.setVisibility(View.INVISIBLE);//默认标题不可见

    }
    public void chooseDate(View view){
        Calendar c = Calendar.getInstance();//获取日期信息
        new DatePickerDialog(MainActivity.this,new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker view,int year,int month,int day){
                date.setText(year + "-" + (month + 1) + "-" + day);
            }
        },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void add(View view){
        mDB.execSQL("insert into memento_tb values(null,?,?,?)",new String[]{
                subject.getText().toString(),body.getText().toString(),date.getText().toString()
        });
        this.subject.setText("");
        this.body.setText("");
        this.date.setText("");
        title.setVisibility(View.INVISIBLE);
        Toast.makeText(MainActivity.this,"备忘录添加成功",Toast.LENGTH_SHORT).show();
        result.setAdapter(null);
    }


    public void delete(View view){
        mDB.execSQL("delete from memento_tb where subject=?",new String[]{
                subject.getText().toString()
        });
        this.subject.setText("");
        this.body.setText("");
        this.date.setText("");
        title.setVisibility(View.INVISIBLE);
        Toast.makeText(MainActivity.this,"备忘录删除成功",Toast.LENGTH_SHORT).show();
        result.setAdapter(null);
    }

    public void update(View view){
        mDB.execSQL("update memento_tb set subject=?,body=?,date=? where subject=?",new String[]{
                subject.getText().toString(),body.getText().toString(),date.getText().toString(),subject.getText().toString()
        });
        this.subject.setText("");
        this.body.setText("");
        this.date.setText("");
        title.setVisibility(View.INVISIBLE);
        Toast.makeText(MainActivity.this,"备忘录更改成功",Toast.LENGTH_SHORT).show();
        result.setAdapter(null);
    }


   public void query(View view){
        title.setVisibility(View.VISIBLE);
        Cursor cursor = mDB.rawQuery(
                "select * from memento_tb where subject like ? and body like ? and date like ?",
                new String[]{"%" + subject.getText().toString() + "%", "%" + body.getText().toString() + "%","%" +
                        date.getText().toString() + "%"});
        SimpleCursorAdapter resultAdapter = new SimpleCursorAdapter(
                MainActivity.this,R.layout.item,cursor,
                new String[]{"subject","body","date"},
                new int[]{R.id.memento_subject,R.id.memento_body,R.id.memento_date},CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        result.setAdapter(resultAdapter);
    }
    protected void onDestroy(){
        if(mDB !=null){
            mDB.close();
            mHelper.close();
        }
        super.onDestroy();
    }
}
///R.id.memento_num,"_id",


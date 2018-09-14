package com.example.io_demo;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // data/data/包名/files/文件名
//        saveFile();
//        readFile();
        // data/data/包名/Shared_prefs/xxx.xml
//        saveSP();
//        readSP();

        // data/data/包名/databases/xxx.db
        createDatabase();
        insertData();
//        deleteData();
//        updateData();
        queryData();
    }

    private void queryData() {
        Cursor cursor = mDb.query("student", null, null, null, null, null, null);
        if (cursor != null)
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                Log.d(getClass().getSimpleName(), id + "");
                Log.d(getClass().getSimpleName(), name);
            }
        cursor.close();
    }

    private void updateData() {
        ContentValues values = new ContentValues();
        values.put("age", 100);
        mDb.update("student", values, "age>?", new String[]{"10"});

    }

    private void deleteData() {
        mDb.delete("student", "age>?", new String[]{"15"});
    }

    private void insertData() {
        ContentValues values = new ContentValues();
        values.put("name", "Lucy");
        values.put("age", "17");
        values.put("grade", "Grade2");
        mDb.insert("student", null, values);

    }

    private void createDatabase() {
        MyHelper myHelper = new MyHelper(this, "student.db", null, 3);
        mDb = myHelper.getReadableDatabase();
    }

    public class MyHelper extends SQLiteOpenHelper {

        public String CREATE_STUDENT = "create table student(" +
                "id integer primary key autoincrement," +
                "name text," +
                "age integer," +
                "grade text)";
        public String CREATE_STUDENT2 = "create table student2(" +
                "id integer primary key autoincrement," +
                "name text," +
                "age integer," +
                "grade text)";

        public MyHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_STUDENT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL(CREATE_STUDENT2);
        }
    }

    private void readSP() {
        SharedPreferences sp = getSharedPreferences("tianyuPres", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Log.d(getClass().getSimpleName(),
                sp.getString("key1", "default1"));
        Log.d(getClass().getSimpleName(),
                sp.getBoolean("key2", false) + "");
        Log.d(getClass().getSimpleName(),
                sp.getInt("key3", 100) + "");
    }

    private void saveSP() {
        SharedPreferences sp = getSharedPreferences("tianyuPres", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("key1", "value1");
        editor.putBoolean("Key2", true);
        editor.commit();
    }

    private void readFile() {
        BufferedReader bufferedReader = null;

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = openFileInput("tianyu");
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Log.d(getClass().getName(), line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveFile() {
        BufferedWriter bufferedWriter = null;
        try {
            FileOutputStream fileOutputStream = openFileOutput("tianyu", Context.MODE_PRIVATE);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            bufferedWriter.write("无边落木萧萧下");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

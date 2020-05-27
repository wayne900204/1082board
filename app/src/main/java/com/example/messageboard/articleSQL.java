package com.example.messageboard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class articleSQL extends SQLiteOpenHelper {
    public static String DATEBASE_NAME = "messageboard";//資料整個庫的名稱
    private static final String TAG = "articleSQL";
    public static int version = 1;//版本
    public static String ARTICLEBOARD = "articleboard";//資料庫名稱

    public articleSQL(@Nullable Context context) {
        super(context, DATEBASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//建立資料庫
        String sql = "CREATE TABLE IF NOT EXISTS "+ARTICLEBOARD+"("+
                     " id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                     "TITLE TEXT,ARTICLE TEXT);";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//更新用的
//              ContentValues values = new ContentValues();
//              values.put("");
            db.execSQL(" DROP TABLE IF EXISTS "+ARTICLEBOARD);
            onCreate(db);
    }
    public long insertSETDATE(String title,String article){//塞值進去資料庫裡面的欄位
        ContentValues cv = new ContentValues();
        cv.put("Title",title); //這邊的字串要跟AddItem那邊的Key一樣
        cv.put("Article",article);

        long date = getWritableDatabase().insert(ARTICLEBOARD,null,cv);
        return date;
    }
    public boolean DELETEDATE(String title){

        long num = getWritableDatabase().delete(ARTICLEBOARD,"TITLE='"+title+"'",null);
        //注意這邊字串裡面會有 TITLE='   '這個'要記得，所以
        if(num!=0)
            return false;
        else
            return true;
    }
    public ArrayList<HashMap<String,String>> getDATA(){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();

        String sql = " SELECT * FROM "+ARTICLEBOARD+" ORDER BY ID ASC ";
        //從Articleboard裡面取資料，並排序，由小到大。
        Cursor c = getWritableDatabase().rawQuery(sql,null);
        //getWritable就是可以讀取 也可以寫入
        int num = c.getCount();
        if (num!=0){
            c.moveToFirst();
        }
        for (int i=0;i<num;i++){
            HashMap<String,String> map = new HashMap<>();//<key,value>
            map.put("ID",c.getInt(0)+"");
            map.put("Title",c.getString(1));
            map.put("Article",c.getString(2));
            list.add(map);
            c.moveToNext();
        }

        return list;
    }

}

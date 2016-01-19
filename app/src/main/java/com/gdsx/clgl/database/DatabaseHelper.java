package com.gdsx.clgl.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/1/18.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "clgld.db"; //数据库名称

    private static final int version = 1; //数据库版本

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context){
        super(context,DB_NAME,null,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table upload_record(id integer PRIMARY KEY autoincrement,path varchar(50),wc varchar(20),uploaded tinyint not null default 0,date createtime not null default (datetime('now','localtime')));";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //当版本号更新时，保存数据并更新表结构
    }
}


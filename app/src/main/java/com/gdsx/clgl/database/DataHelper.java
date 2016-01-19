package com.gdsx.clgl.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gdsx.clgl.entity.UploadRecord;

/**
 * Created by Administrator on 2016/1/19.
 */
public class DataHelper {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    public DataHelper(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insert(UploadRecord ur){
        db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("path",ur.getPath());
        cv.put("wc", ur.getWc());
        cv.put("uploaded",ur.getUploaded());
        long id = db.insert("upload_record",null,cv);
        Log.i("return long ",id+"");
        db.close();
        return id;
    }

    public UploadRecord queryfromId(long id){
        UploadRecord ur = new UploadRecord();
        db = dbHelper.getReadableDatabase();
        String sql = "select * from upload_record where id = "+id;
        Cursor curosr  = db.rawQuery(sql, null);
        while (curosr.moveToNext()){
            ur.setPath(curosr.getString(1));
            ur.setWc(curosr.getString(2));
            ur.setUploaded(curosr.getInt(3));
        }
        db.close();
        return ur;
    }

    public UploadRecord queryfromPath(String path){
        UploadRecord ur = new UploadRecord();
        db = dbHelper.getReadableDatabase();
        String sql = "select * from upload_record where path = '"+path+"'";
        Cursor curosr  = db.rawQuery(sql, null);
        while (curosr.moveToNext()){
            ur.setPath(curosr.getString(1));
            ur.setWc(curosr.getString(2));
            ur.setUploaded(curosr.getInt(3));
        }
        db.close();
        return ur;
    }


    public int update(String path){
        db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("uploaded", 1);
        int effect = db.update("upload_record",cv,"path=?",new String[]{path});
        return effect;
    }



    public void close(){
        db.close();
        dbHelper.close();
    }
}

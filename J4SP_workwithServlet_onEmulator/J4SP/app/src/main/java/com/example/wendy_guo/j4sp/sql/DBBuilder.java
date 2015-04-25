package com.example.wendy_guo.j4sp.sql;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.wendy_guo.j4sp.Constants;
import com.example.wendy_guo.j4sp.R;
import com.example.wendy_guo.j4sp.pojo.UploadRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wendy_Guo on 3/15/15.
 */
public class DBBuilder {

    private Context mContext;
    private SQLiteHelper mSQLiteHelper;
    private SharedPreferences sharedPref;

    public DBBuilder(Context context) {
        mContext = context;
        mSQLiteHelper = SQLiteHelper.getInstance(context);
        SQLiteDatabase database = mSQLiteHelper.getReadableDatabase();
        database.close();
    }

    private SQLiteDatabase open() {
        return mSQLiteHelper.getWritableDatabase();
    }

    private void close(SQLiteDatabase database) {
        database.close();
    }

    public void insertUploadRecord (UploadRecord record) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(mSQLiteHelper.COLUMN_BACKEND_ID, record.getrID());
        values.put(mSQLiteHelper.COLUMN_FILE_NAME, record.getName());
        values.put(mSQLiteHelper.COLUMN_DATE, record.getDate());
        values.put(mSQLiteHelper.COLUMN_TOTAL, record.getTotal());
        values.put(mSQLiteHelper.COLUMN_PATH, record.getPath());
        values.put(mSQLiteHelper.COLUMN_COMMENTS, record.getComments());
        long _ID = database.insert(mSQLiteHelper.TABLE_UPLOAD_HISTORY, null, values);
        Log.i("ID",record.getrID()+"");
        Log.i("ID",_ID+"");


        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }



    public void deleteUploadRecordByID(long rowID) {
        SQLiteDatabase database = open();
        database.beginTransaction();

//        database.delete(mSQLiteHelper.TABLE_UPLOAD_HISTORY,
//                String.format("%s=%s", mSQLiteHelper.COLUMN_FOREIGN_KEY_MEME, String.valueOf(rowID)),
//                null);
        database.delete(mSQLiteHelper.TABLE_UPLOAD_HISTORY,
                String.format("%s=%s", mSQLiteHelper.COLUMN_BACKEND_ID, String.valueOf(rowID)),
                null);
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public void deleteUploadRecordInfo(HashMap<String, String> map) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        StringBuilder sql = new StringBuilder();
        boolean flag = false;
        for(Map.Entry<String,String> e: map.entrySet()){
            if (flag == true) sql.append(" AND ");
            sql.append(e.getKey());
            sql.append(" = " + e.getValue());
            flag = true;
        }
        database.delete(mSQLiteHelper.TABLE_UPLOAD_HISTORY, sql.toString() , null);
        database.setTransactionSuccessful();
        database.endTransaction();
    }




    public void updateUploadRecordInfo(UploadRecord record) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(mSQLiteHelper.COLUMN_FILE_NAME, record.getName());
        values.put(mSQLiteHelper.COLUMN_TOTAL, record.getTotal());
        values.put(mSQLiteHelper.COLUMN_DATE, record.getDate());
        values.put(mSQLiteHelper.COLUMN_COMMENTS, record.getComments());
        database.update(mSQLiteHelper.TABLE_UPLOAD_HISTORY,
                values,
                String.format("%s=%s", mSQLiteHelper.COLUMN_BACKEND_ID, String.valueOf(record.getrID())), null);

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
        Log.i("DB UPDATE", String.valueOf(record.getrID()));
    }

    public void dropT(String tName){
        SQLiteDatabase database = open();
        database.beginTransaction();
        database.execSQL("DROP TABLE IF EXISTS "+ SQLiteHelper.TABLE_UPLOAD_HISTORY);
        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
        Log.i("DROP TABLE SUCCESS","DROP TABLE SUCCESS");
    }

    public Cursor getAllData() {
        SQLiteDatabase database = open();
        Cursor cursor = database.query(
                SQLiteHelper.TABLE_UPLOAD_HISTORY,
                new String [] {SQLiteHelper.COLUMN_BACKEND_ID,SQLiteHelper.COLUMN_FILE_NAME,
                        SQLiteHelper.COLUMN_TOTAL,
                        SQLiteHelper.COLUMN_DATE,
                        SQLiteHelper.COLUMN_PATH,
                        SQLiteHelper.COLUMN_COMMENTS},
                null, //selection
                null, //selection args
                null, //group by
                null, //having
                null); //order
        return cursor;
    }


    public ArrayList<UploadRecord> readUploadHistory() {
        sharedPref = mContext.getSharedPreferences(
                mContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String s = sharedPref.getInt(Constants.USER_ID, -1) + "_";
        Log.i("ID",s);
        SQLiteDatabase database = open();

        Cursor cursor = database.query(
                SQLiteHelper.TABLE_UPLOAD_HISTORY,
                new String [] {SQLiteHelper.COLUMN_BACKEND_ID,SQLiteHelper.COLUMN_FILE_NAME,
                        SQLiteHelper.COLUMN_TOTAL,
                        SQLiteHelper.COLUMN_DATE,
                        SQLiteHelper.COLUMN_PATH,

                        SQLiteHelper.COLUMN_COMMENTS},
                SQLiteHelper.COLUMN_PATH + "  LIKE ? ", //selection
                new String[] {s + "%" }, //selection args
                null, //group by
                null, //having
                null); //order

        ArrayList<UploadRecord> records = new ArrayList<UploadRecord>();
        if(cursor.moveToFirst()) {
            do {
                UploadRecord record = new UploadRecord(getColumnInfo(cursor, SQLiteHelper.COLUMN_FILE_NAME),
                        getColumnInfo(cursor, SQLiteHelper.COLUMN_DATE),
                        getColumnInfo(cursor, SQLiteHelper.COLUMN_TOTAL),
                        getColumnInfo(cursor, SQLiteHelper.COLUMN_COMMENTS),
                        getColumnInfo(cursor, SQLiteHelper.COLUMN_PATH),
                        getRowId(cursor,SQLiteHelper.COLUMN_BACKEND_ID));
                records.add(record);
            }while(cursor.moveToNext());
        }
        cursor.close();
        close(database);
        return records;
    }


    private String getColumnInfo(Cursor cur, String cName) {
        int columnIndex = cur.getColumnIndex(cName);
        return cur.getString(columnIndex);
    }
    private long getRowId(Cursor cur, String cName) {
        int columnIndex = cur.getColumnIndex(cName);
        return cur.getLong(columnIndex);
    }


}


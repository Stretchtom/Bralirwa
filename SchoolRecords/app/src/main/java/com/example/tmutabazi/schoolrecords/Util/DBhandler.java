package com.example.tmutabazi.schoolrecords.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tmutabazi.schoolrecords.Model.Student;

import java.util.ArrayList;

/**
 * Created by tmutabazi on 4/3/2015.
 */
public class DBhandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StudentDB";
    private static final int DATABASE_VERSION = 9;
    public static final String TABLE_DATA = "studentData";
    public static  final String COLUMN_ID = "_id";
    public static  final String COLUMN_Q1 = "quiz1";
    public static  final String COLUMN_Q2 = "quiz2";
    public static  final String COLUMN_Q3 = "quiz3";
    public static  final String COLUMN_Q4 = "quiz4";
    public static  final String COLUMN_Q5 = "quiz5";
    public static  final String COLUMN_SID = "SID";

 private static String table_create = "CREATE TABLE studentData"+
         "(_id integer primary key autoincrement,"+
         "SID integer," +
         "quiz1 integer," +
         "quiz2 integer," +
         "quiz3 integer," +
         "quiz4 integer," +
         " quiz5 integer);";


    public static String[] columns = new String[] {COLUMN_Q1,COLUMN_Q2,COLUMN_Q3,COLUMN_Q4,COLUMN_Q5};

    public DBhandler(Context context) {


        super(context, DATABASE_NAME,null,DATABASE_VERSION);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(table_create);

    }

    public void deleteTable(SQLiteDatabase db)
    {
        db.execSQL("DELETE FROM " +TABLE_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
        Log.i("DELETE TABLE", "SUCCESS");
        onCreate(db);

    }

    // inserts a new contact in the database
    public void insertData( ArrayList<Student> stu)
    {
        int i = 0;

        SQLiteDatabase db =  this.getWritableDatabase();
     //  db.execSQL("DROP TABLE IF EXISTS " +TABLE_DATA);
        ContentValues results = new ContentValues();
        while(i < stu.size())
        {
            results.put(COLUMN_SID, stu.get(i).getSID());
                results.put(COLUMN_Q1, stu.get(i).getScores()[0]);
                results.put(COLUMN_Q2, stu.get(i).getScores()[1]);
                 results.put(COLUMN_Q3, stu.get(i).getScores()[2]);
                 results.put(COLUMN_Q4, stu.get(i).getScores()[3]);
                  results.put(COLUMN_Q5, stu.get(i).getScores()[4]);
            Log.d("Database", "" + stu.get(i).getSID());
            i++;

        }


        db.insert(TABLE_DATA, null, results);

        db.close();
    }

    public ArrayList<Student> retrieve_records()
    {
        String selectQuery = "SELECT * FROM " + TABLE_DATA;
        int l =0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Student> stu = new ArrayList<Student>();
        cursor.moveToFirst();

           while(!cursor.isAfterLast())
           {
               int[] quiz = new int[5];
               int SID;

               SID = cursor.getInt(cursor.getColumnIndex(COLUMN_SID));
               Log.d("retDatabase", "" +cursor.getPosition());

               quiz[0] = cursor.getInt(cursor.getColumnIndex(columns[0]));
               quiz[1] = cursor.getInt(cursor.getColumnIndex(columns[1]));
               quiz[2] = cursor.getInt(cursor.getColumnIndex(columns[2]));
               quiz[3] = cursor.getInt(cursor.getColumnIndex(columns[3]));
               quiz[4] = cursor.getInt(cursor.getColumnIndex(columns[4]));


               stu.add(new Student(SID, quiz));

               cursor.moveToNext();

           }

        cursor.deactivate();
        db.close();
        return stu;
    }


}

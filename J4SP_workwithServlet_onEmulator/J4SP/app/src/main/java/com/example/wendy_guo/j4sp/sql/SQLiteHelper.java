package com.example.wendy_guo.j4sp.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Wendy_Guo on 3/15/15.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "J4SP.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_UPLOAD_HISTORY = "upload_history";
    public static final String TABLE_DOWNLOAD_HISTORY = "fileCache";
    public static final String COLUMN_FILE_NAME = "name";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TOTAL = "total";
    public static final String COLUMN_COMMENTS = "review";
    public static final String COLUMN_FILE = "file";
    public static final String COLUMN_FILE_TYPE = "type";
    public static final String COLUMN_PATH = "file_path";


    public static final String COLUMN_BACKEND_ID = "rID";

    private static String CREATE_HISTORY_TABLE =
            "CREATE TABLE " + TABLE_UPLOAD_HISTORY + "("
                    + COLUMN_BACKEND_ID + " INTEGER NOT NULL PRIMARY KEY," +
                    COLUMN_FILE_NAME +" TEXT," +
                    COLUMN_DATE + " TEXT,"+
                    COLUMN_TOTAL+ " TEXT,"+
                    COLUMN_PATH+ " TEXT,"+
                    COLUMN_COMMENTS +" TEXT)";


    private static final String CREATE_CACHE_TABLE = "CREATE TABLE " + TABLE_DOWNLOAD_HISTORY + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_FILE_NAME + " TEXT, " +
            COLUMN_DATE + " TEXT, " +
            COLUMN_FILE + " TEXT, " +
            COLUMN_FILE_TYPE + " TEXT " + ")";



    private static SQLiteHelper sSQLiteHelper;

    public static SQLiteHelper getInstance(Context context) {

        // don't leak an Activity's context.
        if (sSQLiteHelper == null) {
            sSQLiteHelper = new SQLiteHelper(context.getApplicationContext());
        }
        return sSQLiteHelper;
    }
    private SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_HISTORY_TABLE);
        Log.i("CREATE_HISTORY_TABLE", "success");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
    public void c(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(CREATE_HISTORY_TABLE);
        Log.i("TAB","TAB CRE");
    }
}

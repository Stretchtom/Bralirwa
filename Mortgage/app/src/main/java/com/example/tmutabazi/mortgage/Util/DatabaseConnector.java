package com.example.tmutabazi.mortgage.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tmutabazi.mortgage.Model.Mortgage;

/**
 * Created by tmutabazi on 3/30/2015.
 */


    public class DatabaseConnector extends SQLiteOpenHelper
    {

        private static final String DATABASE_NAME = "MortgageCalculatordb";
       private static final int DATABASE_VERSION = 2;
        public static final String TABLE_DATA = "data";
        public static  final String COLUMN_ID = "_id";

      // private  SQLiteDatabase db;

        public DatabaseConnector(Context context) {

            super(context, DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            // query to create a new table named contacts
            String createQuery = "CREATE TABLE data " +
                      "(_id integer primary key autoincrement," +
                    "purchase double, mortgageTerm integer, interestRate double," +
                    "propertyTax double, propertyInsurance double);";

            db.execSQL(createQuery); // execute the query
        } // end method onCreate

        // inserts a new contact in the database
        public void insertData(Mortgage mort)
        {
            ContentValues newCalculation = new ContentValues();
            newCalculation.put("purchase", mort.getPurchasePrice());
            newCalculation.put("mortgageTerm", mort.getMortgageTerm());
            newCalculation.put("interestRate", mort.getInterestRate());
            newCalculation.put("propertyTax", mort.getPropertyTax());
            newCalculation.put("propertyInsurance", mort.getPropertyInsurance());

            SQLiteDatabase db =  this.getWritableDatabase();

            db.insert(TABLE_DATA, null, newCalculation);
            Log.d("DB operation", "Record inserted");
            db.close();
        } // end method insertContact
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion,
                                  int newVersion)
            {
            }



        }





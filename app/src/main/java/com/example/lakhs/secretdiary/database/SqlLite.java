package com.example.lakhs.secretdiary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SqlLite extends SQLiteOpenHelper {

    // variables for user tables
    public static final String user_tb = "user_tb";
    public static final String id = "id";
    public static final String user_name = "user_name";
    public static final String user_email = "user_email";
    public static final String user_password = "user_password";
    public static final String phone_id = "phone_id";

    //variables for diary tables;
    public static final String diary_tb = "diary_tb";
    public static final String user_id = "user_id";
    public static final String diary_title = "diary_title";
    public static final String diary_text = "diary_text";
    public static final String create_date = "create_date";
    public static final String create_day = "create_day";
    public static final String create_month = "create_month";
    public static final String create_year =  "create_year";
    public static final String color = "color";


    //creates two tables
    private static final String create_user_tb = "CREATE TABLE IF NOT EXISTS "
            + user_tb + " ( " + id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + phone_id + " text, "
            + user_name + " text not null, "
            + user_password + " text not null, "
            + user_email + " text not null, "
            + create_date + " long);";

    private static final String create_diary_tb = "CREATE TABLE IF NOT EXISTS "
           + diary_tb + " ( " + id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
           + user_id + " integer, "
           + diary_title + " text not null, "
           + diary_text + " text not null, "
           + create_date + " long, "
           + create_day + " text, "
           + create_month + " text, "
           + create_year + " text, "
           + color + " int);";

    private static final String DB_NAME = "Diary.db";
    private static final int DB_VERSION = 1;

    /*public SqlLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
         super(context, name, factory, version);
    }*/
    public SqlLite(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_user_tb);
        db.execSQL(create_diary_tb);
        Log.d("database", "create both database");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + user_tb);
        db.execSQL("DROP TABLE IF EXISTS " + diary_tb);
        onCreate(db);
    }
}

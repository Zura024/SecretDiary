package com.example.lakhs.secretdiary.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lakhs.secretdiary.model.Diary;
import com.example.lakhs.secretdiary.model.User;

import java.util.ArrayList;
import java.util.List;

public class DbService  {

    private SqlLite sqlLite;

    public DbService(SqlLite sqlLite){
        this.sqlLite = sqlLite;
    }

    public User checkMobileOfUser(String phone_id){
        SQLiteDatabase db = sqlLite.getWritableDatabase();
        User user = null;
        String query = "Select * from user_tb where phone_id = '" + phone_id + "' order by " + SqlLite.create_date + " desc";;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            user = new User();
            int id = cursor.getColumnIndex("id");
            user.setId(cursor.getInt(id));
            int phoneIdIndex = cursor.getColumnIndex("phone_id");
            user.setPhone_id(cursor.getString(phoneIdIndex));
            int emailIndex = cursor.getColumnIndex("user_email");
            user.setUserEmail(cursor.getString(emailIndex));
            int userNameIndex = cursor.getColumnIndex("user_name");
            user.setUserEmail(cursor.getString(userNameIndex));
            int passIndex = cursor.getColumnIndex("user_password");
            user.setPassword(cursor.getString(passIndex));
        }
//        Log.d("ifUser",user.toString());
        db.close();
        return user;
    }

    public long insertUser(User user){
        SQLiteDatabase db = sqlLite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_name",user.getUserName());
        values.put("user_email",user.getUserEmail());
        values.put("user_password",user.getPassword());
        values.put("phone_id",user.getPhone_id());
        values.put("create_date",System.currentTimeMillis());
        long insert = db.insert(SqlLite.user_tb,null,values);
        db.close();
        return insert;
    }


    public long updateUser(User user){
        SQLiteDatabase db = sqlLite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlLite.user_name,user.getUserName());
        values.put(SqlLite.user_email,user.getUserName());
        values.put(SqlLite.user_password,user.getPassword());
        long success = db.update(SqlLite.user_tb,values,"id = " + user.getId(),null);
        db.close();
        return success;
    }


    public long deleteUser(int id){
        SQLiteDatabase db = sqlLite.getWritableDatabase();
        deleteAllDiaryOnUser(id,db);
        long success = db.delete(SqlLite.user_tb,"id =" + id,null);
        return success;
    }

    private void deleteAllDiaryOnUser(int id, SQLiteDatabase db) {
        String select = "select * from " + SqlLite.diary_tb + " where " + SqlLite.user_id + " = " + id;
        Cursor cursor = db.rawQuery(select,null);
        if(cursor.moveToFirst()){
            do{
                deleteRow(id,db);
            }while(cursor.moveToNext());
        }

    }

    private boolean deleteRow(int id,SQLiteDatabase db){
        String where = SqlLite.user_id + " = " + id;
        return  db.delete(SqlLite.diary_tb,where,null) != 0;
    }

    public long insertDiary(Diary diary){
        SQLiteDatabase db = sqlLite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlLite.user_id, diary.getUser_id());
        values.put(SqlLite.diary_title, diary.getTitle());
        values.put(SqlLite.diary_text, diary.getText());
        values.put(SqlLite.create_date, System.currentTimeMillis());
        values.put(SqlLite.create_day, diary.getDay());
        values.put(SqlLite.create_month, diary.getMonth());
        values.put(SqlLite.create_year, diary.getYear());
        values.put(SqlLite.color, diary.getColor());
        Log.d("blblaa",diary.getId() + " ");
        long insertOrUpdate = 0;
        if(diary.getId() != 0){
            insertOrUpdate = db.update(SqlLite.diary_tb,values,"id = " + diary.getId(),null);
        }else {
            insertOrUpdate = db.insert(SqlLite.diary_tb, null, values);
        }
        db.close();
        return insertOrUpdate;
    }

    public User ifExistUser(String username,String password){
        SQLiteDatabase db = sqlLite.getWritableDatabase();
        User user = null;
        String query = "Select * from user_tb where user_name = '" + username + "' and user_password = '" + password +"'";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            user = new User();
            int id = cursor.getColumnIndex("id");
            user.setId(cursor.getInt(id));
            int phoneIdIndex = cursor.getColumnIndex("phone_id");
            user.setPhone_id(cursor.getString(phoneIdIndex));
            int emailIndex = cursor.getColumnIndex("user_email");
            user.setUserEmail(cursor.getString(emailIndex));
            int userNameIndex = cursor.getColumnIndex("user_name");
            user.setUserName(cursor.getString(userNameIndex));
            int passIndex = cursor.getColumnIndex("user_password");
            user.setPassword(cursor.getString(passIndex));
        }
        db.close();
        return user;
    }

    public long removeDiary(int diaryId){
        SQLiteDatabase db = sqlLite.getWritableDatabase();
        long delete = db.delete(SqlLite.diary_tb,SqlLite.id + "=" + diaryId,null);
        db.close();
        return delete;
    }

    public User getUserFromPP(String password,String phoneId){
        SQLiteDatabase db = sqlLite.getWritableDatabase();
        User user = null;
        String query = "Select * from user_tb where phone_id = '" + phoneId +"' and user_password = '" +password + "'";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            user = new User();
            int id = cursor.getColumnIndex("id");
            user.setId(cursor.getInt(id));
            int phoneIdIndex = cursor.getColumnIndex("phone_id");
            user.setPhone_id(cursor.getString(phoneIdIndex));
            int emailIndex = cursor.getColumnIndex("user_email");
            user.setUserEmail(cursor.getString(emailIndex));
            int userNameIndex = cursor.getColumnIndex("user_name");
            user.setUserEmail(cursor.getString(userNameIndex));
            int passIndex = cursor.getColumnIndex("user_password");
            user.setPassword(cursor.getString(passIndex));
            Log.d("ddddddddddd","ddddddddddd");
        }
//        Log.d("ifUser",user.toString());
        db.close();
        return user;
    }


    public List<Diary> getDiariesFromUser(User user){
        List<Diary> diaryList = new ArrayList<>();
        SQLiteDatabase db = sqlLite.getWritableDatabase();
        String query = "Select * from " + SqlLite.diary_tb + " where " + SqlLite.user_id + " = '" + user.getId() +"' order by " + SqlLite.create_date + " desc";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do {
                Diary diary = new Diary();
                int id = cursor.getColumnIndex(SqlLite.id);
                diary.setId(cursor.getInt(id));
                int userId = cursor.getColumnIndex(SqlLite.user_id);
                diary.setUser_id(cursor.getInt(userId));
                int title = cursor.getColumnIndex(SqlLite.diary_title);
                diary.setTitle(cursor.getString(title));
                int text = cursor.getColumnIndex(SqlLite.diary_text);
                diary.setText(cursor.getString(text));
                int time = cursor.getColumnIndex(SqlLite.create_date);
                diary.setDate(cursor.getLong(time));
                int day = cursor.getColumnIndex(SqlLite.create_day);
                diary.setDay(cursor.getString(day));
                int month = cursor.getColumnIndex(SqlLite.create_month);
                diary.setMonth(cursor.getString(month));
                int year = cursor.getColumnIndex(SqlLite.create_year);
                diary.setYear(cursor.getString(year));
                int color = cursor.getColumnIndex(SqlLite.color);
                diary.setColor(cursor.getInt(color));
                diaryList.add(diary);
            } while (cursor.moveToNext());
        }
        db.close();
        return diaryList;
    }

}

package com.example.lakhs.secretdiary.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Diary implements Parcelable{

    private int id;
    private int user_id;
    private String title;
    private String text;
    private long date;
    private int color;

    private String day;
    private String month;
    private String year;

    public static final Creator CREATOR =
            new Creator() {
                public Diary createFromParcel(Parcel in) {
                    return new Diary(in);
                }

                public Diary[] newArray(int size) {
                    return new Diary[size];
                }
            };

    public Diary(Parcel parcel){
        this.id = parcel.readInt();
        this.user_id = parcel.readInt();
        this.title = parcel.readString();
        this.text = parcel.readString();
        this.date = parcel.readLong();
        this.day = parcel.readString();
        this.month = parcel.readString();
        this.year = parcel.readString();
        this.color = parcel.readInt();
    }

    public  Diary(){}

    public Diary(int user_id,String title,String text,long date,String day,String month,String year,int color){
        this.user_id = user_id;
        this.title = title;
        this.text = text;
        this.date = date;
        this.day = day;
        this.month = month;
        this.year = year;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public long getDate() {
        return date;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(user_id);
        dest.writeString(title);
        dest.writeString(text);
        dest.writeLong(date);
        dest.writeString(day);
        dest.writeString(month);
        dest.writeString(year);
        dest.writeInt(color);
    }
}

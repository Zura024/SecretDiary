package com.example.lakhs.secretdiary.model;

import android.os.Parcel;
import android.os.Parcelable;


public class User implements Parcelable {

    private int id;
    private String userName;
    private String userEmail;
    private String password;
    private String phone_id;
    private long create_date;

    public static final Creator CREATOR =
            new Creator() {
                public User createFromParcel(Parcel in) {
                    return new User(in);
                }

                public User[] newArray(int size) {
                    return new User[size];
                }
            };

    public User(int id,String userName,String userEmail){
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public  User(){}

    public User(Parcel parcel){
        this.id = parcel.readInt();
        this.userName = parcel.readString();
        this.userEmail = parcel.readString();
        this.password = parcel.readString();
        this.phone_id = parcel.readString();
        this.create_date = parcel.readLong();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone_id(String phone_id) {
        this.phone_id = phone_id;
    }

    public String getPhone_id() {
        return phone_id;
    }

    public long getCreate_date() {
        return create_date;
    }

    public void setCreate_date(long create_date) {
        this.create_date = create_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(userName);
        dest.writeString(userEmail);
        dest.writeString(password);
        dest.writeString(phone_id);
        dest.writeLong(create_date);
    }
}

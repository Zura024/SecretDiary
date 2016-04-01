package com.example.lakhs.secretdiary;

import android.app.Application;
import android.content.Context;

import com.example.lakhs.secretdiary.database.SqlLite;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;




public class App extends Application {

    private static Context context;
    private static SqlLite sqlLite;
    private  String phone_id;
    private HashMap<Integer,Integer> mapColor= new HashMap<>();
    public static int maxLength = 15;

    public static int SET_PASSWORD = 1;
  //  public static int SET_REMAINDER = 2;
    public static int BACKUP_DATA = 3;
    public static int ACTION_LOGOUT = 4;
    public static int DELETE_ACCOUNT = 5;


    private int remDay = Calendar.getInstance().DAY_OF_MONTH;


    @Override
    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();
        createInit();
    }


    public void setRemainderDay(int day){
        this.remDay = day;
    }

    public int getRemainderDay(){
        return this.remDay;
    }


    private void createInit() {
        sqlLite = new SqlLite(App.context);

        mapColor.put(1,R.color.green);
        mapColor.put(2,R.color.blue);
        mapColor.put(3,R.color.red);
        mapColor.put(4,R.color.pink);
        mapColor.put(5,R.color.yellow);
       /* Log.d("createApp", "App");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
               FindMobileId findMobileId = new FindMobileId();
               findMobileId.execute(sqlLite);
            }
        });*/
    }

    public int getRandomColor(){
        Random random = new Random();
        int randomColorIndex = mapColor.get(random.nextInt(5) + 1);
        return randomColorIndex;
    }



    public static SqlLite getSqlLite(){
        return sqlLite;
    }

    public  String getPhone_id(){
        return this.phone_id;
    }

    public void setPhone_id(String phone_id){
        this.phone_id = phone_id;
    }
    /*class FindMobileId extends AsyncTask<SqlLite,Void,User>{
        @Override
        protected User doInBackground(SqlLite... params) {
            DbService db = new DbService(params[0]);
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String phoneId = telephonyManager.getDeviceId();
            User getUser = db.checkMobileOfUser(phoneId);
            phone_id = phoneId;
            return getUser;
        }
        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if(findUserListener != null) {
                findUserListener.onFindUser(user);
            }
        }
    }
*/



}

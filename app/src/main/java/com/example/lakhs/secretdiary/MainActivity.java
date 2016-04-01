package com.example.lakhs.secretdiary;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.View;

import com.example.lakhs.secretdiary.activity.ActivatedActivity;
import com.example.lakhs.secretdiary.activity.LoginActivity;
import com.example.lakhs.secretdiary.database.DbService;
import com.example.lakhs.secretdiary.database.SqlLite;
import com.example.lakhs.secretdiary.model.User;


public class MainActivity extends ActionBarActivity {

    // private User user;
    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //    Log.d("callOtherIntent","now2");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                FindMobileId findMobileId = new FindMobileId();
                findMobileId.execute(App.getSqlLite());
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }



    private void callActivityByUser(User user) {
        //   Log.d("callOtherIntent","aqq");
        if(user != null){
            Intent activated = new Intent(this, ActivatedActivity.class);
            activated.putExtra("User",user);
            startActivity(activated);
        }else{
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
        }
    }


    class FindMobileId extends AsyncTask<SqlLite,Void,User> {

        @Override
        protected User doInBackground(SqlLite... params) {
            DbService db = new DbService(params[0]);
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String phoneId = telephonyManager.getDeviceId();
            App myApp = (App)getApplicationContext();
            myApp.setPhone_id(phoneId);
            User getUser = db.checkMobileOfUser(phoneId);
            return getUser;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            callActivityByUser(user);
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }
    }
}

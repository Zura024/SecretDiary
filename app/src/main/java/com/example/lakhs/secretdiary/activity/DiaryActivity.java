package com.example.lakhs.secretdiary.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lakhs.secretdiary.App;
import com.example.lakhs.secretdiary.R;
import com.example.lakhs.secretdiary.adapters.DiaryAdapter;
import com.example.lakhs.secretdiary.adapters.SettingAdapter;
import com.example.lakhs.secretdiary.database.DbService;
import com.example.lakhs.secretdiary.model.Diary;
import com.example.lakhs.secretdiary.model.Setting;
import com.example.lakhs.secretdiary.model.User;
import com.example.lakhs.secretdiary.service.AlarmService;
import com.example.lakhs.secretdiary.service.Validation;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.validation.Validator;




public class DiaryActivity extends ActionBarActivity {

    private ListView listView;
    private User user;
    private List<Diary> diaries;
    private LinearLayout emptyLayout;
    private DiaryAdapter diaryAdapter;
    private String searchWord;
    private Boolean exit = false;
    private TextView dateText;

    private ListView mDrawerList;
    private List<Setting> settings = new ArrayList<>();
    private SettingAdapter settingAdapter;

    private String curDay;
    private String curYear;
    private String curMonth;


    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_activity);

        user = getIntent().getExtras().getParcelable("User");
        listView = (ListView) findViewById(R.id.diary_list);
        mDrawerList = (ListView)findViewById(R.id.navList);
        listView.setLongClickable(true);
        findUserHasDiary();
        addDrawerItems();
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(DiaryActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();*/
                chooseAction(settings.get(position));
            }

            private void chooseAction(Setting setting) {
                switch (setting.getAction()){
                    case 1:
                        Log.d("hsowpop",""+setting.getAction());
                        doPassword();
                        break;
                   /* case 2:
                        doReminder();
                        break;*/
                    case 3:
                        doBackup();
                        break;
                    case 4:
                        doLogout();
                        break;
                    case 5:
                        doDeleteAccount();
                        break;
                    default:
                        break;
                }
            }
        });

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        setupDrawer();
        mDrawerToggle.setDrawerIndicatorEnabled(true);
    }


    private void doBackup(){

        Intent intentEmail = new Intent(Intent.ACTION_SEND);
        intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{user.getUserEmail()});
        String text = getBackupMessagesUser();
        intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Get your diaries");
        intentEmail.putExtra(Intent.EXTRA_TEXT, text);
        intentEmail.setType("message/rfc822");
        startActivity(Intent.createChooser(intentEmail, "Choose an email provider :"));
    }

    private String getBackupMessagesUser(){
        String retText = "";

        for(int i = 0; i <  diaries.size(); i++){
            retText += i + ". title  '"  +diaries.get(i).getTitle() + "' \n";
            retText += diaries.get(i).getMonth() + "/" + diaries.get(i).getDay() + "/ " + diaries.get(i).getYear() + "\n";
            retText += diaries.get(i).getText() + " \n";
            retText +="\n";
        }
        return retText;
    }

    private void doDeleteAccount() {
        LinearLayout viewGroup = (LinearLayout) findViewById(R.id.deleted_account_layout);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.delete_account_layout, viewGroup);

        final PopupWindow popupWindow = new PopupWindow();
        popupWindow.setContentView(layout);
        popupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);

        TextView txt = (TextView) layout.findViewById(R.id.delete_account_text);
        txt.setText(R.string.sure_delete);

        Button ok = (Button) layout.findViewById(R.id.delete_account_yes);
        Button no = (Button) layout.findViewById(R.id.delete_account_no);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbService db = new DbService(App.getSqlLite());
                long success = db.deleteUser(user.getId());
                if(success > 0) {
                    Toast.makeText(DiaryActivity.this, "Delete your account",
                            Toast.LENGTH_SHORT).show();
                    doLogout();
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(layout, Gravity.TOP,0,150);
    }




    private void doLogout(){
        Intent loginActivity = new Intent(DiaryActivity.this,LoginActivity.class);
        startActivity(loginActivity);
    }

    private void doReminder() {

        // this is already ready for start remainder
       /* LinearLayout viewGroup = (LinearLayout) findViewById(R.id.password_change_layout);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.change_password_layout, viewGroup);

        final PopupWindow popupWindow = new PopupWindow();
        popupWindow.setContentView(layout);
        popupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);

        dateText = (TextView) layout.findViewById(R.id.remainder_date);
        Button set = (Button) layout.findViewById(R.id.remainder_yes);
        Button cancel = (Button) layout.findViewById(R.id.remainder_no);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                 curYear = String.valueOf(year);
                 curMonth = getMonth(month + 1);
                 curDay = String.valueOf(year);
                dateText.setText(curMonth + " | " + curDay + " | " + curYear);
            }
        };

        dateText.setOnClickListener(new View.OnClickListener() {
            Calendar myCalendar = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                    DatePickerDialog datePickerDialog =  new DatePickerDialog(DiaryActivity.this,R.style.Base_Theme_AppCompat_Light_DarkActionBar,date,myCalendar
                            .get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                    datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    datePickerDialog.show();
            }
        });

        Intent myIntent = new Intent(this, AlarmService.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);
        Calendar calendar = Calendar.getInstance();*/

        // only set calendar
       /* Toast.makeText(DiaryActivity.this, R.string.alarm_notify,
                Toast.LENGTH_SHORT).show();*/



    }

    public String getMonth(int i) {
        return new DateFormatSymbols().getMonths()[i];
    }

    private void doPassword() {
        LinearLayout viewGroup = (LinearLayout) findViewById(R.id.password_change_layout);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.change_password_layout, viewGroup);

        final PopupWindow popupWindow = new PopupWindow();
        popupWindow.setContentView(layout);
        popupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);

        Button ok = (Button) layout.findViewById(R.id.change_password_btn);
        final EditText tx = (EditText) layout.findViewById(R.id.change_password_text);
        tx.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validation.hasEnoughLength(tx.getText().toString(),6)) {
                    user.setPassword(tx.getText().toString());
                    updateUserPassword();
                    popupWindow.dismiss();
                }else{
                    Toast.makeText(DiaryActivity.this,R.string.password_notify,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button cancel = (Button) layout.findViewById(R.id.cancel_password_btn);
        cancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(layout, Gravity.TOP,0,150);
    }

    private void updateUserPassword() {
        DbService db = new DbService(App.getSqlLite());
        long success = db.updateUser(user);
        if(success > 0){
            Toast.makeText(this, "Updated your password",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Setting");
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void addDrawerItems() {
        fillSettingList();
        settingAdapter = new SettingAdapter(getApplicationContext(),settings);
        mDrawerList.setAdapter(settingAdapter);
    }

    private void fillSettingList() {

        Setting settingPassword = new Setting(R.string.password_sett_title,R.string.password_sett_description,App.SET_PASSWORD);
     //   Setting settingRemainder = new Setting(R.string.remainder_sett_title,R.string.remainder_sett_description,App.SET_REMAINDER);
        Setting backupData = new Setting(R.string.backup_sett_title, R.string.backup_sett_description,App.BACKUP_DATA);
        Setting settingDelete = new Setting(R.string.delete_sett_title,R.string.delete_sett_description, App.DELETE_ACCOUNT);
        Setting settingLogout = new Setting(R.string.logout_sett_title,R.string.logout_sett_description,App.ACTION_LOGOUT);

        settings.add(settingPassword);
     //   settings.add(settingRemainder);
        settings.add(backupData);
        settings.add(settingLogout);
        settings.add(settingDelete);

    }


    private void findUserHasDiary() {
        DbService db = new DbService(App.getSqlLite());
        diaries = db.getDiariesFromUser(user);
        emptyLayout = (LinearLayout) findViewById(R.id.empty_diary);
        emptyLayout.setVisibility(View.GONE);
        if(diaries.size() == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
          /*  View hidden = getLayoutInflater().inflate(R.layout.empty_diary_layout,emptyLayout,false);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);*/
            Button btn = (Button) findViewById(R.id.create_first_diary);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myDiary = new Intent(DiaryActivity.this, MyDiaryActivity.class);
                    myDiary.putExtra("User",user);
                    DiaryActivity.this.startActivity(myDiary);
                }
            });

        }else{
            diaryAdapter = new DiaryAdapter(diaries,getApplicationContext());
            listView.setAdapter(diaryAdapter);
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myDiary = new Intent(DiaryActivity.this, MyDiaryActivity.class);
                myDiary.putExtra("User",user);
                myDiary.putExtra("Diary",diaries.get(position));
                DiaryActivity.this.startActivity(myDiary);
            }
        });
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_add).setVisible(!drawerOpen);
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.diary_controll_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public void onBackPressed() {
        if (exit) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        } else {
             Toast.makeText(this, R.string.close_app,
                        Toast.LENGTH_SHORT).show();
             exit = true;
             new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
             }, 3 * 1000);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add:
                openDiaryActivity();
                return true;
            case R.id.action_search:
                startFindDiary();
                return true;
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startFindDiary() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.find_diary);
        alert.setMessage(R.string.search_diary);
        final EditText input = new EditText(this);
        if(searchWord != null){
            input.setText(searchWord);
        }
        alert.setView(input);
        final List<Diary> filter = new ArrayList<>();

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for(Diary filterDiary : diaries){
                    Log.d("filter",filterDiary.getTitle());
                    if(filterDiary.getTitle().toLowerCase().contains(s.toString().toLowerCase())){
                        if(!filter.contains(filterDiary)) {
                            filter.add(filterDiary);
                        }
                    }
                }
                searchWord = s.toString();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                  listView.setAdapter(new DiaryAdapter(filter,getApplicationContext()));
            }
        });

        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                input.setText("");
                listView.setAdapter(new DiaryAdapter(filter,getApplicationContext()));
            }
        });
        alert.show();
    }

    private void openDiaryActivity() {
        Intent myDiary = new Intent(DiaryActivity.this, MyDiaryActivity.class);
        myDiary.putExtra("User",user);
        DiaryActivity.this.startActivity(myDiary);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

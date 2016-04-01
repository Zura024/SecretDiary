package com.example.lakhs.secretdiary.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lakhs.secretdiary.App;
import com.example.lakhs.secretdiary.R;
import com.example.lakhs.secretdiary.database.DbService;
import com.example.lakhs.secretdiary.model.Diary;
import com.example.lakhs.secretdiary.model.User;
import com.example.lakhs.secretdiary.service.Validation;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class MyDiaryActivity extends ActionBarActivity {

    private EditText titleTxt;
    private TextView dateTxt;
    private EditText textTxt;
    private User user;
    private Diary curDiary;
    private String curDay;
    private String curMonth;
    private String curYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_elements);
        user = getIntent().getExtras().getParcelable("User");
        curDiary = getIntent().getExtras().getParcelable("Diary");
        titleTxt = (EditText) findViewById(R.id.diary_title);
        dateTxt = (TextView) findViewById(R.id.date_show);
        setPropertyOnDate();
        textTxt = (EditText) findViewById(R.id.diary_text);
        if(curDiary != null){
            configurationDiaryActivity();
        }
        setCalendar();
    }

    private void configurationDiaryActivity() {
        titleTxt.setText(curDiary.getTitle());
        dateTxt.setText(curDiary.getMonth() + " | " + curDiary.getDay() + " | " + curDiary.getYear());
        textTxt.setText(curDiary.getText());
    }

    private void setPropertyOnDate() {
        Calendar cur = Calendar.getInstance();
        curDay = String.valueOf(cur.get(Calendar.DAY_OF_MONTH));
        curMonth = getMonth(cur.get(Calendar.MONTH));
        curYear =  String.valueOf(cur.get(Calendar.YEAR));
        String dateFormat = curMonth + " | " + curDay + " | " + curYear;
        dateTxt.setText(dateFormat);
        dateTxt.setMovementMethod(new ScrollingMovementMethod());
    }

    private void setCalendar() {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                 curYear = String.valueOf(year);
                 curMonth = getMonth(month + 1);
                 curDay = String.valueOf(day);
                dateTxt.setText(curMonth + " | " + curDay + " | " + curYear);
            }
        };

        dateTxt.setOnClickListener(new View.OnClickListener() {
                Calendar myCalendar = Calendar.getInstance();
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog =  new DatePickerDialog(MyDiaryActivity.this,R.style.Base_Theme_AppCompat_Light_DarkActionBar,date,myCalendar
                            .get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                    datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    datePickerDialog.show();
                }
        });
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_diary_menu,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item= menu.findItem(R.id.remove_item);
        if(curDiary != null) {
            item.setVisible(true);
            item.setEnabled(true);
        }else{
            item.setVisible(false);
            item.setEnabled(false);
        }
        super.onPrepareOptionsMenu(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_save:
                saveDiary();
                return true;
            case R.id.remove_item:
                removeDiaryFromDb();
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeDiaryFromDb() {
        DbService db = new DbService(App.getSqlLite());
        long k  = db.removeDiary(curDiary.getId());
        if(k > 0) {
            Intent diaryActivity = new Intent(this, DiaryActivity.class);
            diaryActivity.putExtra("User", user);
            startActivity(diaryActivity);
        }
    }

    public void saveDiary() {
        DbService db = new DbService(App.getSqlLite());
        String title = titleTxt.getText().toString();
        String text = textTxt.getText().toString();
        if(curDiary == null) {
           int res = ((App)getApplicationContext()).getRandomColor();
           curDiary =  new Diary(user.getId(),title,text,System.currentTimeMillis(),curDay,curMonth,curYear,res);
        }else{
            curDiary.setText(text);
            curDiary.setTitle(title);
            curDiary.setDay(curDay);
            curDiary.setMonth(curMonth);
            curDiary.setYear(curYear);
        }
        if(!Validation.hasText(titleTxt) || !Validation.hasText(textTxt)){
            return;
        }
        long k  = db.insertDiary(curDiary);
        if(k > 0) {
            Intent diaryActivity = new Intent(this, DiaryActivity.class);
            diaryActivity.putExtra("User", user);
            startActivity(diaryActivity);
        }
    }
}

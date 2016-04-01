package com.example.lakhs.secretdiary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lakhs.secretdiary.App;
import com.example.lakhs.secretdiary.R;
import com.example.lakhs.secretdiary.database.DbService;
import com.example.lakhs.secretdiary.model.User;
import com.example.lakhs.secretdiary.service.Validation;


public class RegisterActivity extends Activity {

    private EditText username;
    private EditText password;
    private EditText email;
    private Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        username = (EditText) findViewById(R.id.login_edit_text);
        password = (EditText) findViewById(R.id.register_edit_text);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        email = (EditText) findViewById(R.id.email_edit_text);
        register = (Button) findViewById(R.id.register_btn_login);

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                   Validation.hasText(username);
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                    Validation.isEmailAddress(email,true);
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    Validation.hasEnoughLength(password,6);
            }
        });

    }



    public void registerUser(View view){
        if(!Validation.hasText(username) || !Validation.hasEnoughLength(password,6) || !Validation.isEmailAddress(email,true)){
             return;
        }
        DbService db = new DbService(App.getSqlLite());
        User user = new User();
        user.setUserName(username.getText().toString());
        user.setUserEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        user.setPhone_id(((App)getApplicationContext()).getPhone_id());
       // Log.d("phone_idsss",user.getPhone_id());
        long inserted =  db.insertUser(user);
        resetFields();
        if(inserted > 0 ){
            Intent singUpActivity = new Intent(view.getContext(),SingUpActivity.class);
            startActivity(singUpActivity);
        }
    }

    private void resetFields() {
        username.setText("");
        email.setText("");
        password.setText("");
    }


}

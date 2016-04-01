package com.example.lakhs.secretdiary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lakhs.secretdiary.App;
import com.example.lakhs.secretdiary.R;
import com.example.lakhs.secretdiary.activity.DiaryActivity;
import com.example.lakhs.secretdiary.database.DbService;
import com.example.lakhs.secretdiary.model.User;



public class SingUpActivity extends Activity {

    private EditText username;
    private EditText password;
    private Button btn;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sing_up_activity);

        username = (EditText) findViewById(R.id.sing_up_text);
        password = (EditText) findViewById(R.id.sing_up_password);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        btn = (Button) findViewById(R.id.sing_up_btn);

    }


    public void singUpUser(View view){
        String userN = username.getText().toString();
        String userP = password.getText().toString();
        DbService db = new DbService(App.getSqlLite());
        resetFields();
        User user = db.ifExistUser(userN,userP);
        if(user != null){
            Intent activated = new Intent(this, DiaryActivity.class);
            activated.putExtra("User",user);
            startActivity(activated);
        }else{
            Log.d("incorrect","pass");
        }

    }

    private void resetFields() {
        username.setText("");
        password.setText("");
    }


}

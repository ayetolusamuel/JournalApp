package com.setnumd.technologies.journalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity {
private Button btn_Login;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        configView();

    }

    private void configView() {
        btn_Login = findViewById(R.id.btn_login);
    }

    public void loginUser(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    public void registerUser(View view){
        System.out.println("Register User......");
    }
}

package com.test.atn;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class WelcomeUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff8a0b")));

        setContentView(R.layout.activity_welcome_user);

        Intent intent = getIntent();
        String message = intent.getStringExtra(Login.MESSAGE);
        String messageS = message.substring(0,8) + message.substring(8,9).toUpperCase() + message.substring(9);
        TextView welcomeMessage = (TextView) findViewById(R.id.welcomeMessage);
        welcomeMessage.setText(messageS);
    }
}

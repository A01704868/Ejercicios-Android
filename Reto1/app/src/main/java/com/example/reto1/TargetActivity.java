package com.example.reto1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class TargetActivity extends AppCompatActivity {

    TextView targetBox;
    TextView targetBox2;
    TextView targetBox3;
    TextView targetBox4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);

        targetBox = findViewById(R.id.textBox);
        targetBox2 = findViewById(R.id.textBox2);
        targetBox3 = findViewById(R.id.textBox3);
        targetBox4 = findViewById(R.id.textBox4);
        Intent currentIntent = getIntent();

        String nameX;
        String phoneX;
        String passwordX;
        int passwordY;
        Boolean checkJavaX;

        nameX = currentIntent.getStringExtra("name");
        phoneX = currentIntent.getStringExtra("phone");
        passwordX = currentIntent.getStringExtra("password");
        checkJavaX = currentIntent.getBooleanExtra("checkJava", false);

        passwordY = passwordX.hashCode();

        targetBox.setText("User name = "+nameX);
        targetBox2.setText("Phone number = "+phoneX);
        targetBox3.setText("Password = "+passwordY);
        targetBox4.setText("User knows java = "+checkJavaX.toString());

    }
}
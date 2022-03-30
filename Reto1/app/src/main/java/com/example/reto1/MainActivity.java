package com.example.reto1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private Button buttonSend;
    private EditText name;
    private EditText phone;
    private EditText password;
    private CheckBox checkbox1;
    private Switch switchCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSend = findViewById(R.id.button);
        name = findViewById(R.id.TextName);
        phone = findViewById(R.id.TextPhone);
        password = findViewById(R.id.TextPassword);
        checkbox1 = findViewById(R.id.checkBox);
        switchCheck = findViewById(R.id.switch1);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switchCheck.isChecked()){
                    Intent intent = new Intent(MainActivity.this, TargetActivity.class);
                    intent.putExtra("name", name.getText().toString());
                    intent.putExtra("phone", phone.getText().toString());
                    intent.putExtra("password", password.getText().toString());
                    intent.putExtra("checkJava", checkbox1.isChecked());
                    startActivity(intent);
                }else{
                    switchCheck.setTextColor(0xffff0000);
                }
            }
        });
    }
}
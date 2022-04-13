package mx.tec.osmproj;
// Code Challenge
// Leverage the following code to create a "Panic Button" application.
// In a first activity, you should simply add a button to trigger an alarm.
// In a successive activity, you should obtain your position from the GPS antenna
// and render an OpenStreetMaps and place a marker in your position. Finally
// you should provide the means to feed a destination number to send a pre-defined
// SOS message via SMS Management.

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    Context context;

        @Override public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            context = getApplicationContext();


            Button gpsButton= findViewById(R.id.button);
            gpsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(context, Activity2.class);
                    startActivity(i);
                }
            });
    }
}
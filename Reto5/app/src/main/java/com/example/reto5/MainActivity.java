package com.example.reto5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Target;

public class MainActivity extends AppCompatActivity implements LocationListener {
    protected LocationManager locationManager;
    private volatile boolean value;

    TextView locationName;
    TextView locationTemp;
    TextView locationDescription;
    RadioButton rb;
    TextView txtvC;
    RadioGroup radioGroup;
    Button myButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Location Based Services require "hot permission granting" upon installing the app
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, 1);
        }

        value = true;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        locationName = findViewById(R.id.locName);
        locationTemp = findViewById(R.id.locTemp);
        locationDescription = findViewById(R.id.locDes);

        myButton = findViewById(R.id.button);
        radioGroup = findViewById(R.id.radioGroup3);
        txtvC = findViewById(R.id.textBox);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = txtvC.getText().toString();
                String units;
                int selectedId = radioGroup.getCheckedRadioButtonId();
                rb = findViewById(selectedId);
                units = rb.getText().toString();

                Intent myIntent = new Intent(MainActivity.this, TargetActivity.class);
                myIntent.putExtra("city", city);
                myIntent.putExtra("units", units);
                startActivity(myIntent);
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        if(value){
            String uri = "https://api.openweathermap.org/data/2.5/weather?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&units=metric&appid=" + "ca7bbd394ebffd4667a8215044935aef";
            // Get JSON response
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            String scity;
                            String stemp;
                            String sdesc;

                            //store reponse segments in variables
                            try {
                                scity = response.getString("name");
                                stemp = response.getJSONObject("main").getString("temp");
                                sdesc = response.getJSONArray("weather").getJSONObject(0).getString("description");

                                locationName.setText(scity);
                                locationTemp.setText("Temp en celcius->"+stemp);
                                locationDescription.setText(sdesc);

                            }catch(JSONException e){
                                Log.d( "I","JSONException");
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
            value = false;
            //add request to queue
            RequestQueue myqueue = Volley.newRequestQueue(this);
            myqueue.add(jsonObjectRequest);
        }
    }
}
package com.example.reto5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class TargetActivity extends AppCompatActivity {

    TextView myTxt;

    TextView cityView;
    TextView tempView;
    TextView descriptionView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);

        Intent myIntent = getIntent();
        String city = myIntent.getStringExtra("city");
        String units = myIntent.getStringExtra("units");

        myTxt = findViewById(R.id.textView);
        cityView = findViewById(R.id.textView2);
        tempView = findViewById(R.id.textView4);
        descriptionView = findViewById(R.id.textView5);

        myTxt.setText(city + ".." + units);

        if(units == "radioButton"){
            units = "metric";
        }
        else{
            units = "imperial";
        }

        //prepare my URI
        String uri = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=" + units + "&appid=" + "ca7bbd394ebffd4667a8215044935aef";

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

                           cityView.setText(scity);
                           tempView.setText(stemp);
                           descriptionView.setText(sdesc);

                       }catch(JSONException e){
                           Log.d( "I","JSONException");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //add request to queue
        RequestQueue myqueue = Volley.newRequestQueue(this);
        myqueue.add(jsonObjectRequest);

    }
}
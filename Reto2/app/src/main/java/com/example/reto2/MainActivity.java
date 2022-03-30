package com.example.reto2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Context context;

    private Toast myToast;

    private AlertDialog.Builder dialogConf;
    private AlertDialog myDialog;
    private String texto;
    private String[] listItems;

    private NotificationCompat.Builder myBuilder;
    private NotificationManager myNotificationManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //contexto
        context = getApplicationContext();

        //notification channel
        NotificationChannel myChannel = new NotificationChannel("CHANNEL_ID", "CHANNEL_NAME", NotificationManager.IMPORTANCE_DEFAULT);
        Intent intent = new Intent(getApplicationContext(), ChildActivity.class);
        PendingIntent myPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //choice list
        listItems = new String[]{"Apples","Oranges","Bananas"};
        final EditText choice = new EditText(this);

        //dialog builder
        dialogConf = new AlertDialog.Builder(this);
        dialogConf.setTitle("Fill in your Preferences");

        //set up input text for name
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialogConf.setView(input);

        dialogConf.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                choice.setText(listItems[i]);
            }
        });

        dialogConf.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        texto=input.getText().toString();
                        myToast.makeText(context, "Number is:"+texto, Toast.LENGTH_SHORT).show();
                        myBuilder = new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_ID")
                                .setSmallIcon(R.mipmap.ic_launcher_round) // notification icon
                                .setContentTitle("Number is: "+texto)
                                .setContentText("You chose "+ choice.getText().toString())
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(myPendingIntent)
                                .addAction(0, "Click Me", myPendingIntent)
                                .setAutoCancel(false);

                        myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        myNotificationManager.createNotificationChannel(myChannel);

                        myNotificationManager.notify(0, myBuilder.build());
                    }
                });
        dialogConf.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        myToast.makeText(context, "You Clicked No", Toast.LENGTH_SHORT).show();
                    }
                });
        dialogConf.setNeutralButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        myToast.makeText(context, "You Clicked Close", Toast.LENGTH_SHORT).show();
                    }
                });
        myDialog = dialogConf.create();
        myDialog.show();

    }


}
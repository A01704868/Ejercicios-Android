// Code Challenge: Use the following code as baseline to
// create an Android application that in a single activity has a button
// to download a large file (for instance, 1GB). Save that file in the
// local storage. Add the necessary functionality to survey the environment
// via BroadcastReceivers to let the app go ahead with the download only if the
// following conditions are satisfied:
//    1) The device should be at least 95% of battery level OR
//    2) the device is plugged to USB/AC power sources AND
//    3) the device is online via WiFi (not mobile network)

package mx.tec.ch_04_2022_01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView batteryLevel;
    private int batteryLevelInt;
    private TextView powerStatus;
    private TextView networkStatus;
    private int plugged;
    private int networkKind;


    private BroadcastReceiver powerInfoReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

            if(plugged == BatteryManager.BATTERY_PLUGGED_AC) {
                powerStatus.setText("Plugged to AC");
            }
            else if(plugged == BatteryManager.BATTERY_PLUGGED_USB){
                powerStatus.setText("Plugged to USB");
            }
            else if (plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS)
                powerStatus.setText("Plugged to Wireless Charger");
            else
                powerStatus.setText("Not plugged");
        }
    };

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            batteryLevelInt = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            batteryLevel.setText("Battery Level: " + String.valueOf(batteryLevelInt) + "%");


        }
    };

    private BroadcastReceiver netWorkStatus = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {

            String networkType = "";

            Context myContext = getApplicationContext();
            ConnectivityManager cm = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);

            try{
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork.isConnectedOrConnecting()) {

                    if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        networkKind = ConnectivityManager.TYPE_WIFI;
                        networkType = "WiFi";
                    }
                    else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                        networkKind = ConnectivityManager.TYPE_MOBILE;
                        networkType = "Mobile";
                    }
                    else {
                        networkType = "Other";
                    }

                }
                networkStatus.setText("Network Type: " + networkType);
            }
            catch (Exception e){
                networkStatus.setText("Device is not online");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        batteryLevel = findViewById(R.id.batteryTxt);
        powerStatus = findViewById(R.id.powerStatus);
        networkStatus = findViewById(R.id.connStatusTxt);
        Button descargar = findViewById(R.id.button);

        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        this.registerReceiver(this.powerInfoReciever, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        this.registerReceiver(this.netWorkStatus, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(batteryLevelInt >= 95 || plugged == 1 || plugged == 2 || plugged == 4){
                    if(networkKind == 1 || networkKind == 0){
                        //Local storage management requires runtime permission granting upon installing the app
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, 1);

                        // This code uses DownloadManager.Request to prepare a URL request to obtain a file from the internet
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("https://sabnzbd.org/tests/internetspeed/50MB.bin"));
                        request.setDescription("Downloading file " + "testfile.bin");
                        request.setTitle("Downloading");
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "testfile.bin");

                        //Once with the request prepared and configured, we obtain the proper system service and start the download
                        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                        manager.enqueue(request);

                        // We register a Broadcast Receiver (explained below)
                        registerReceiver(new DonwloadCompleteReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                    }
                }
            }
        });

    }

    // This broadcast receiver expects a signal when an active downloading status changes.
    // When the file completes the download, it will pop out a toast
    public class DonwloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
                Toast.makeText(context,"Download completed", Toast.LENGTH_LONG).show();

            }
        }
    }

}
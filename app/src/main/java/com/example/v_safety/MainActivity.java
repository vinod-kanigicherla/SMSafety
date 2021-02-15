package com.example.v_safety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageButton alertButton;
    ImageButton settingsButton;
    TextView titleTextView;

    LocationManager locationManager;
    LocationListener locationListener;

    String latitude;
    String longitude;

    public void setContacts(View view) {
        Intent intent = new Intent(getApplicationContext(), SetContactsActivity.class);
        startActivity(intent);
    }

    public void alert(View view) {
        new AlertDialog.Builder(view.getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Alert")
                .setMessage("Are you sure you want to send the alert message?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                            sendAlertMessage();
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void sendAlertMessage() {
        if (SetContactsActivity.contactNamesList != null) {
            SmsManager smsManager = SmsManager.getDefault();

            for (int i = 0; i < SetContactsActivity.contactNamesList.size(); i++) {
                String phoneNumber = SetContactsActivity.phoneNumsList.get(i);
                smsManager.sendTextMessage(phoneNumber, null, "Hi! I am in danger. This is my location: https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude, null, null);
                Toast.makeText(this, "SMS sent successfully to all contacts!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "You have to add contacts to send the alert message!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alertButton = findViewById(R.id.alertButton);
        settingsButton = findViewById(R.id.settingsButton);
        titleTextView = findViewById(R.id.titleTextView);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latitude = Double.toString(location.getLatitude());
                longitude = Double.toString(location.getLongitude());
            }
        };


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                sendAlertMessage();
        } else {
            Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }
}
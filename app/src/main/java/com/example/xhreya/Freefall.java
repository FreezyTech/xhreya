package com.example.xhreya;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Freefall extends AppCompatActivity {

    private static final int PERMISSION_CODE = 123;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        TextView status = findViewById(R.id.statusText);
        Button start = findViewById(R.id.startBtn);
        Button stop = findViewById(R.id.stopBtn);

        // Ask permissions
        String[] perms = {Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, perms, PERMISSION_CODE);
        }

        start.setOnClickListener(v -> {
            Intent i = new Intent(this, FallDetectionService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(i);
            else startService(i);
            status.setText("Fall Detection RUNNING");
        });

        stop.setOnClickListener(v -> {
            stopService(new Intent(this, FallDetectionService.class));
            status.setText("Fall Detection STOPPED");
        });
    }
}

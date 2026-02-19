package com.example.xhreya;

import android.Manifest;
import android.app.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.*;
import android.net.Uri;
import android.os.*;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.*;

public class FallDetectionService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Vibrator vibrator;

    private boolean inFreeFall = false;
    private long freeFallStart = 0;

    private static final float FREE_FALL_THRESHOLD = 2.0f;
    private static final float IMPACT_THRESHOLD = 25.0f;

    private FusedLocationProviderClient fusedLocationClient;

    // Nepal country code
    private String whatsappNumber = "9779865382699";

    @Override
    public void onCreate() {
        super.onCreate();

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_GAME
        );

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        createChannel();
        startForeground(1, getNotification("Monitoring for falls..."));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        double acc = Math.sqrt(x * x + y * y + z * z);

        if (acc < FREE_FALL_THRESHOLD) {
            if (!inFreeFall) {
                inFreeFall = true;
                freeFallStart = System.currentTimeMillis();
            }
        }

        if (inFreeFall && acc > IMPACT_THRESHOLD) {
            long time = System.currentTimeMillis() - freeFallStart;
            double height = 0.5 * 9.8 * Math.pow(time / 1000.0, 2);

            handleFall(height);
            inFreeFall = false;
        }
    }

    private void handleFall(double height) {

        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(
                        1500,
                        VibrationEffect.DEFAULT_AMPLITUDE
                ));
            } else {
                vibrator.vibrate(1500);
            }
        }

        sendWhatsAppMessage(height);
    }

    private void sendWhatsAppMessage(double height) {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) return;

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {

            String message = "⚠️ FALL DETECTED ⚠️\n"
                    + "Estimated Height: " + String.format("%.2f", height) + " m";

            if (location != null) {
                message += "\nLocation:\nhttps://maps.google.com/?q="
                        + location.getLatitude() + "," + location.getLongitude();
            }

            String url = "https://wa.me/" + whatsappNumber +
                    "?text=" + Uri.encode(message);

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    private Notification getNotification(String text) {
        return new NotificationCompat.Builder(this, "fall")
                .setContentTitle("Fall Detection")
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "fall",
                    "Fall Detection",
                    NotificationManager.IMPORTANCE_LOW
            );
            getSystemService(NotificationManager.class)
                    .createNotificationChannel(channel);
        }
    }

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    @Override public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }
}

package edu.csulb.android.arttherapy;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class DrawActivity extends AppCompatActivity implements SensorEventListener {

    DrawView dv;
    Thread thread;
    private SensorManager sensorManager;
    private long lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dv = new DrawView(this);
        setContentView(dv);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.v("MainActivity", "displayAccelerometer" + "X" + event.values[0] + "Y" + event.values[1] + "Z" + event.values[2]);
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            checkShake(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void checkShake(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float accelarationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

        long actualTime = System.currentTimeMillis();

        if (accelarationSquareRoot >= 2) {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
            Toast.makeText(this, "Don't shake me!", Toast.LENGTH_SHORT).show();
            dv.onClear();
            new SoundThread("musicPlayer", this);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

    }

    public void openDrawActivity(View view) {
        startActivity(new Intent(getApplicationContext(), DrawActivity.class));
    }

}
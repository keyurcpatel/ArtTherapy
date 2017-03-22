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
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

public class MainActivity extends AppCompatActivity implements SensorEventListener   {

    TextView textX, textY, textZ;
    private SensorManager sensorManager;
    private long lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        textX = (TextView) findViewById(R.id.textX);
        textY = (TextView) findViewById(R.id.textY);
        textZ = (TextView) findViewById(R.id.textZ);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        lastUpdate = System.currentTimeMillis();

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.v("MainActivity","displayAccelerometer"+"X"+event.values[0]+"Y"+event.values[1]+"Z"+event.values[2]);
        textX.setText(String.valueOf(event.values[0]));
        textY.setText(String.valueOf(event.values[1]));
        textZ.setText(String.valueOf(event.values[2]));
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            checkShake(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void displayAccelerometer(SensorEvent event) {
        textX.setText("X axis" + "\t\t" + event.values[0]);
        textY.setText("Y axis" + "\t\t" + event.values[1]);
        textZ.setText("Z axis" + "\t\t" + event.values[2]);
    }

    private void checkShake(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float accelarationSquareRoot = ( x*x + y*y + z*z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

        long actualTime = System.currentTimeMillis();

        if(accelarationSquareRoot >= 2) {
            if(actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
            Toast.makeText(this, "Don't shake me!", Toast.LENGTH_SHORT).show();

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

    public void  openDrawActivity(View view) {
        startActivity(new Intent(getApplicationContext(), DrawActivity.class));
    }
}

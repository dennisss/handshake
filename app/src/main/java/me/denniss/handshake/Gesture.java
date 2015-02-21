package me.denniss.handshake;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class Gesture  implements SensorEventListener{

    private SensorManager sensorManager;
    private Sensor accel;
    private Sensor magn;
    private Sensor gyro;

    private float[] accel_data;
    private float[] gyro_data;
    private float[] magn_data;

    public Gesture(Activity a){
        sensorManager = (SensorManager)a.getSystemService(Context.SENSOR_SERVICE);

        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magn = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

    }

    private int n = 0;

    @Override
    public final void onSensorChanged(SensorEvent event) {

        n++;

        if(n % 100 == 0)
            Log.i("gesture", "got some events");

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            accel_data = event.values;
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            magn_data = event.values;
        if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
            gyro_data = event.values;

        /*
        if (accel_data != null && magn_data != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, accel_data, magn_data);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimuth = orientation[1]; // orientation contains: azimut, pitch and roll

                azimuth = (float) (azimuth * (180.0 / Math.PI));

            }
        }
        */
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }


    public void resume(){
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
        //sensorManager.registerListener(this, magn, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void pause(){
        sensorManager.unregisterListener(this);
    }

}
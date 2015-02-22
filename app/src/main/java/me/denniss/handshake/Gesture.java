package me.denniss.handshake;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import org.jtransforms.fft.FloatFFT_1D;

import java.util.ArrayList;
import java.util.Date;

public class Gesture  implements SensorEventListener{

    public enum Type{
        HANDSHAKE
    }


    private FloatFFT_1D fft = new FloatFFT_1D(32);

    private SensorManager sensorManager;
    private Sensor accel;
    private Sensor magn;
    private Sensor gyro;

    private float[] accXBuffer = new float[32];
    private float[] accYBuffer = new float[32];
    private float[] accZBuffer = new float[32];

    private float[] accel_data;
    private float[] gyro_data;
    private float[] magn_data;

    public Gesture(Activity a){
        sensorManager = (SensorManager)a.getSystemService(Context.SENSOR_SERVICE);

        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magn = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    /* Add a sample to a rolling buffer (shifting it by one) and return the fft of it */
    private float[] applySampleFFT(float[] in, float a){

        // Shift the data
        for(int i = 1; i < in.length; i++){
            in[i-1] = in[i];
        }


        in[in.length - 1] = a;

        float[] fftBuf = in.clone();
        float[] bins = new float[in.length / 2];

        fft.realForward(fftBuf);

        for(int i = 0; i < fftBuf.length; i += 2){
            float hypot = (float) Math.hypot(fftBuf[i], fftBuf[i+1]);

            bins[i/2] = hypot;
        }

        return bins;
    }

    /* Find the index of the highest value in an array */
    private int maxIndex(float[] in){
        int maxi = 0;
        float maxval = Float.MIN_VALUE;

        for(int i = 0; i < in.length; i++){
            if(in[i] > maxval){
                maxi = i;
                maxval = in[i];
            }
        }

        return maxi;
    }



    private int n = 0;

    private boolean seqeunce = false;
    private int nshakes = 0;

    @Override
    public final void onSensorChanged(SensorEvent event) {

        n++;

        //if(n % 100 == 0)
        //    Log.i("gesture", "got some events");

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accel_data = event.values;

/*
            Log.i("gesture", accel_data[0] + " " + accel_data[1] + " " + accel_data[2]);

            float val = (float) (accel_data[0])  + (accel_data[1]) + (accel_data[2]) ;

            Log.i("gesture", Float.toString(val));
*/

            float[] accXFFT = applySampleFFT(accXBuffer, accel_data[0]);
            float[] accYFFT = applySampleFFT(accYBuffer, accel_data[1]);

            accXFFT[0] = 0;
            accYFFT[0] = 0;

            int maxxi = maxIndex(accXFFT);
            int maxyi = maxIndex(accYFFT);


            if(maxyi == 4){
                nshakes++;

                if(nshakes > 6 && !seqeunce){
                    Log.i("gesture", "HANDSHAKE " + (new Date()));
                    this.emitGesture(Type.HANDSHAKE);
                    seqeunce = true;
                }
            }
            else {
                nshakes = 0;
                seqeunce = false;
            }

            //Log.i("gesture", Integer.toString(maxyi));


        }
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            magn_data = event.values;
        if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
            gyro_data = event.values;


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    private void emitGesture(Type t){
        for(GestureListener l : listeners){
            l.onGesture(t);
        }
    }

    private ArrayList<GestureListener> listeners = new ArrayList<GestureListener>();
    public void addGestureListener(GestureListener listener){
        this.listeners.add(listener);
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
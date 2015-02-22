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
        HANDSHAKE,
        WAVE
    }

    private FeatureExtractor fe = new FeatureExtractor();
    private Learn learn = new Learn();
    private DataCollect datac = new DataCollect();

    private boolean trainMode = false;
    private boolean predictMode = true;



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

    public void noState(){
        if(trainMode){
            datac.save_data();
            learn.train(datac.samples);
            learn.save_model();
        }

        trainMode = false;
        predictMode = false;
    }

    private int curlabel;

    /* Set the sensor callback in training mode: append data points to a file */
    public void trainingState(int label){
        trainMode = true;
        predictMode = false;

        curlabel = label;
    }

    /* Set the sensor callback in prediction mode */
    public void predictState(){
        trainMode = false;
        predictMode = true;
    }


    /* Capture the feature to a csv file */
    public void do_train() {

        float[] sample = fe.getVector(curlabel);

        datac.add_sample(sample);

    }


    private boolean seqeunce = false;
    private int nshakes = 0;
    public void do_basic_predict(){

            if(fe.maxyi == 5){
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


        Log.i("gesture", "" + fe.maxyi);

    }


    public void do_predict(){



    }




    private int n = 0;

    @Override
    public final void onSensorChanged(SensorEvent event) {

        n++;

        //if(n % 100 == 0)
        //    Log.i("gesture", "got some events");

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accel_data = event.values;

            //Log.i("gesture", accel_data[0] + " " + accel_data[1] + " " + accel_data[2]);

/*
            float val = (float) (accel_data[0])  + (accel_data[1]) + (accel_data[2]) ;
            Log.i("gesture", Float.toString(val));
*/
            fe.update(accel_data);


            if(predictMode){
                do_basic_predict();
                // do_predict()  // TODO: Uncomment this when
            }
            else if(trainMode){
                do_train();
            }

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
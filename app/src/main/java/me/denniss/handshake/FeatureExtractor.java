package me.denniss.handshake;


import android.util.Log;

import org.jtransforms.fft.FloatFFT_1D;

import java.util.Date;

/*
    Each sample should have:
        3 axis acceleration vector (or orienetation?)
        3 accelerometer FFTs (index of the max bin)
*/


/* Extracts features from a raw acceleration vector */
public class FeatureExtractor {

    private FloatFFT_1D fft = new FloatFFT_1D(32);

    private float[] accXBuffer = new float[32];
    private float[] accYBuffer = new float[32];
    private float[] accZBuffer = new float[32];


    public float[] acc_vec;
    public int maxxi;
    public int maxyi;
    public int maxzi;

    private boolean seqeunce = false;
    private int nshakes = 0;
    public void update(float[] acc_vec){

        this.acc_vec = acc_vec;

        float[] accXFFT = applySampleFFT(accXBuffer, acc_vec[0]);
        float[] accYFFT = applySampleFFT(accYBuffer, acc_vec[1]);
        float[] accZFFT = applySampleFFT(accZBuffer, acc_vec[2]);

        accXFFT[0] = 0;
        accYFFT[0] = 0;
        accZFFT[0] = 0;


        maxxi = maxIndex(accXFFT);
        maxyi = maxIndex(accYFFT);
        maxzi = maxIndex(accZFFT);

    }

    /* Get a 6-dimensional +1 label sample vector */
    public float[] getVector(int label){
        return new float[]{ acc_vec[0], acc_vec[1], acc_vec[2], maxxi, maxyi, maxzi, label };
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

}

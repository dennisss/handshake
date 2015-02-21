package me.denniss.handshake;

public class Gesture {

    public native void start();
    public native void stop();


    static{
        System.loadLibrary("gesture");
    }

}

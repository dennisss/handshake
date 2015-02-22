package me.denniss.handshake;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DataCollect {

    private static final String FILE = "/sdcard/data.txt";

    public ArrayList<float[]> samples = new ArrayList<>();


    public void add_sample(float[] s){
        samples.add(s);
    }

    public void save_data(){
        try
        {
            FileOutputStream fileOut = new FileOutputStream(FILE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(samples);
            out.close();
            fileOut.close();

        }catch(IOException i)
        {
            Log.i("datacollect", "Failed to save data");
        }


    }

    public void load_data(){
        try
        {
            FileInputStream fileIn = new FileInputStream(FILE);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            samples = (ArrayList<float[]>) in.readObject();
            in.close();
            fileIn.close();
        }catch(IOException i)
        {
            samples = new ArrayList<>();
            i.printStackTrace();
            return;
        }catch(ClassNotFoundException c)
        {
            samples = new ArrayList<>();
            c.printStackTrace();
            return;
        }
    }


}

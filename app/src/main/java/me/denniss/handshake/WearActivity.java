package me.denniss.handshake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.view.WatchViewStub;
import android.view.WindowManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WearActivity extends Activity implements GestureListener{

    private Button listner,startTraining,arrowLeft,arrowRight;
    private GoogleApiClient mApiClient;
    private static final String START_ACTIVITY = "/start_activity";
    TextView currentGesture;
    int curGestureIndex = 0;

    private Gesture g;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        initGoogleApiClient();

        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                 listner = (Button)findViewById(R.id.wearListeningButton);
                startTraining = (Button)findViewById(R.id.wearTrainingButton);
                arrowLeft = (Button)findViewById(R.id.gestureLeftButton);
                arrowRight = (Button)findViewById(R.id.gestureRightButton);
                currentGesture = (TextView)findViewById(R.id.gestureSelected);

                arrowRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        curGestureIndex++;
                        currentGesture.setText(Integer.toString(curGestureIndex));
                    }
                });

                arrowLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        curGestureIndex--;
                        currentGesture.setText(Integer.toString(curGestureIndex));
                    }
                });

                startTraining.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                startedTraining();
                            }
                        }, 2000);
                    }
                });

                listner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendMessage( START_ACTIVITY, "1");
                    }
                });
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        g = new Gesture(this);

    }

    private void startedTraining()
    {
        Toast.makeText(getApplicationContext(),"STARTED TRAINING!",Toast.LENGTH_SHORT).show();

        g.trainingState(curGestureIndex);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                stopTraining();
            }
        }, 10000);
    }

    private void stopTraining()
    {
        g.noState();

        // Save data

        Toast.makeText(getApplicationContext(),"STOPPED TRAINING!",Toast.LENGTH_SHORT).show();
    }

    private void initGoogleApiClient() {
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .build();

        mApiClient.connect();
    }



    private void sendMessage( final String path, final String text ) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApiClient ).await();
                for(Node node : nodes.getNodes()) {
                     Wearable.MessageApi.sendMessage( mApiClient, node.getId(), path, text.getBytes() ).await();

                }

            }
        }).start();
    }



    @Override
    protected void onResume() {
        super.onResume();
        g.resume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        g.pause();

    }


    @Override
    public void onGesture(Gesture.Type t) {
        sendMessage( START_ACTIVITY, Integer.toString((t.ordinal())));

    }
}

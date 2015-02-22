package me.denniss.handshake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
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
import java.util.concurrent.TimeUnit;

public class WearActivity extends Activity implements GoogleApiClient.ConnectionCallbacks{

    private Button listner;

<<<<<<< HEAD
    private Gesture g = new Gesture();
    private GoogleApiClient mApiClient;
    private static final String START_ACTIVITY = "/start_activity";
=======
    private Gesture g;
>>>>>>> origin/master

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
                listner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendMessage( START_ACTIVITY, "MESSAGE" );
                    }
                });
            }
        });

<<<<<<< HEAD
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
=======
        g = new Gesture(this);
>>>>>>> origin/master
    }

    @Override
   public void onConnected(android.os.Bundle bundle)
    {

    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }




    @Override
    protected void onResume() {
        super.onResume();

<<<<<<< HEAD
        //g.start();
=======
        g.resume();
>>>>>>> origin/master
    }

    @Override
    protected void onPause() {
        super.onPause();

<<<<<<< HEAD
        //g.stop();
=======
        g.pause();
>>>>>>> origin/master
    }



}

package me.denniss.handshake;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;


public class MainActivity extends ActionBarActivity{

    private BusinessCard mBusinessCard;
    private final int PICK_CARD = 1;
    private GoogleApiClient mApiClient;
    private static final String START_ACTIVITY = "/start_activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addCard = (Button)findViewById(R.id.addBusinessCard);
        initGoogleApiClient();
        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AddBusinessCard.class);
                if (mBusinessCard != null) {
                    i.putExtra("card", mBusinessCard);
                }
                //startActivityForResult(i, PICK_CARD);
                sendMessage(START_ACTIVITY,"RESPONSE");
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CARD && resultCode == RESULT_OK) {
            mBusinessCard = (BusinessCard) data.getSerializableExtra("card");
        }
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

}

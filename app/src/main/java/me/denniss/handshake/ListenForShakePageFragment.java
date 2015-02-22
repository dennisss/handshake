package me.denniss.handshake;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by Tomer on 2/22/2015.
 */
public class ListenForShakePageFragment extends Fragment implements GestureListener{


    private GoogleApiClient mApiClient;
    private static final String START_ACTIVITY = "/start_activity";
    boolean listeneing = false;
    Button listner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View parentView = inflater.inflate(R.layout.gesture_fragment, container, false);
        initGoogleApiClient();

        listner = (Button)parentView.findViewById(R.id.wearListeningButton);



        listner.setOnClickListener(
               new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       //sendMessage(START_ACTIVITY,"1");
                       if (!listeneing) {
                           WearActivity.g.predictState();
                           listner.setText("Stop Listening");
                       } else {
                           WearActivity.g.noState();
                           listner.setText("Start Listening");
                       }

                       listeneing = !listeneing;
                   }
               });




        WearActivity.g.addGestureListener(this);

        WearActivity.g.noState();

        return parentView;
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

    private void initGoogleApiClient() {
        mApiClient = new GoogleApiClient.Builder( getActivity() )
                .addApi( Wearable.API )
                .build();

        mApiClient.connect();
    }


    @Override
    public void onGesture(Gesture.Type t) {

        sendMessage( START_ACTIVITY, Integer.toString((t.ordinal())));
        Vibrator v = (Vibrator)getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
        v.vibrate(500);

    }
}

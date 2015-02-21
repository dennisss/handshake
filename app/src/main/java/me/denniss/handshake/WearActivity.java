package me.denniss.handshake;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

public class WearActivity extends Activity {

    private TextView mTextView;

    private Gesture g = new Gesture();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });

        Gesture g = new Gesture();
    }

    @Override
    protected void onResume() {
        super.onResume();

        g.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        g.stop();
    }
}

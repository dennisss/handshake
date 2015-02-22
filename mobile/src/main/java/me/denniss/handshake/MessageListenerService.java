package me.denniss.handshake;

import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Tomer on 2/21/2015.
 */

public class MessageListenerService extends WearableListenerService {

    String nodeId;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        nodeId = messageEvent.getSourceNodeId();
        showToast(new String(messageEvent.getData()));
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
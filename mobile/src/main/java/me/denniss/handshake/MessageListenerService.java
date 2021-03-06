package me.denniss.handshake;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tomer on 2/21/2015.
 */

public class MessageListenerService extends WearableListenerService {
    private static final String START_ACTIVITY = "/start_activity";
    String nodeId;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        nodeId = messageEvent.getSourceNodeId();
        //Toast.makeText(getApplicationContext(),"STARTED REQUEST",Toast.LENGTH_LONG).show();
        BusinessCard mBusinessCard = Select.from(BusinessCard.class)
                .where(Condition.prop("is_you").eq("1")).list().get(0);
        
        RequestUtil.sendGesture(new String(messageEvent.getData()), mBusinessCard, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                RequestUtil.requests--;
                JSONObject res = (JSONObject)response;
               // Toast.makeText(getApplicationContext(),"RESPONSE REQUEST",Toast.LENGTH_LONG).show();
                BusinessCard b = new BusinessCard();
                try {
                    b.setAddress(res.getString("address"));
                    b.setBusinessName(res.getString("businessName"));
                    b.setName(res.getString("name"));
                    b.setEmail(res.getString("email"));
                    b.setNumber(res.getString("phone"));
                    b.setFax(res.getString("fax"));
                    b.setJobTitle(res.getString("jobTitle"));
                    b.setWebsite(res.getString("website"));

                    String imageEncoded = res.getString("imageBase64");
                    byte[] decodedString = Base64.decode(imageEncoded, Base64.DEFAULT);


                    File filePath = new File(getFilesDir() + "/" + res.get("imageName"));

                    if(filePath.exists())
                    {
                        filePath.delete();
                    }
                    FileOutputStream os = new FileOutputStream(filePath, false);
                    os.write(decodedString);
                    os.flush();
                    os.close();

                    b.setImageUrl(filePath.getAbsolutePath());

                    b.save();
                    
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent("businessCardReceived");
                // You can also include some extra data.
                intent.putExtra("card",b );
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                sendMessage(START_ACTIVITY,"success");
                //Toast.makeText(getApplicationContext(), ((JSONObject) response).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        
        
        



       // showToast(new String(messageEvent.getData()));
    }


    private void sendMessage( final String path, final String text ) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( RequestUtil.mApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    Wearable.MessageApi.sendMessage( RequestUtil.mApiClient, node.getId(), path, text.getBytes() ).await();

                }

            }
        }).start();
    }

    private void reply(String message) {
        GoogleApiClient client = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Wearable.API)
                .build();
       // client.blockingConnect(1000, TimeUnit.MILLISECONDS);
        Wearable.MessageApi.sendMessage(client, nodeId, message, null);
        client.disconnect();
    }

}
package me.denniss.handshake;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.android.gms.location.LocationServices;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONObject;


public class MainActivity extends ActionBarActivity{

    private BusinessCard mBusinessCard;
    private final int CONFIG_CARD = 1;
    private final int CONFIG_OTHER_CARD = 2;

    public static final int RESULT_DELETE=900;

    private List<BusinessCard> mBusinessCardList;
    private BusinessCardAdapter mCardAdapter;
    private static final String START_ACTIVITY = "/start_activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RequestUtil.init(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("businessCardReceived"));
        /*
        BusinessCard.deleteAll(BusinessCard.class);
        finish();
*/

/*
        BusinessCard card = new BusinessCard();
        card.setName("Bob");
        card.save();

        BusinessCard card1 = new BusinessCard();
        card1.setName("Billy");
        card1.save();

        finish();
        */


        mBusinessCardList = Select.from(BusinessCard.class)
                .where(Condition.prop("is_you").eq("0")).list();
                //.list();//.where(Condition.prop("is_you").eq("0")).list();

        mCardAdapter = new BusinessCardAdapter(this,
                R.layout.business_card_item_row,
                mBusinessCardList);
        ListView lvBusinessCards = (ListView) findViewById(R.id.cardsList);
        lvBusinessCards.setAdapter(mCardAdapter);
        lvBusinessCards.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(),AddBusinessCard.class);
                i.putExtra("card", mBusinessCardList.get(position));
                i.putExtra("position", position);
                startActivityForResult(i, CONFIG_OTHER_CARD);
                return false;
            }
        });


        Button addCard = (Button)findViewById(R.id.addBusinessCard);
        initGoogleApiClient();
        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AddBusinessCard.class);
                if (mBusinessCard != null) {
                    i.putExtra("card", mBusinessCard);
                }

                startActivityForResult(i, CONFIG_CARD);
            }
        });

        List<BusinessCard> businessCards = Select.from(BusinessCard.class)
                .where(Condition.prop("is_you").eq("1")).list();
        if (businessCards.size() > 0) {
            mBusinessCard = businessCards.get(0);
        }
    }



    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            BusinessCard message = (BusinessCard)intent.getSerializableExtra("card");
            if(mBusinessCardList!=null)
            {
                mBusinessCardList.add(0,message);
                //message.save();
                mCardAdapter.notifyDataSetChanged();
                sendMessage(START_ACTIVITY,"success");
            }

        }
    };




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CONFIG_CARD) {
                BusinessCard newCard = (BusinessCard) data.getSerializableExtra("card");
                if (mBusinessCard == null)
                    mBusinessCard = newCard;
                else
                    mBusinessCard.setCard(newCard);
                mBusinessCard.isYou(true);
                mBusinessCard.save();


            } else if (requestCode == CONFIG_OTHER_CARD) {
                int position = data.getIntExtra("position", -1);
                if (position == -1)
                    return;
                BusinessCard newCard = (BusinessCard) data.getSerializableExtra("card");
                BusinessCard oldCard = mBusinessCardList.get(position);
                oldCard.setCard(newCard);
                oldCard.save();
                mCardAdapter.notifyDataSetChanged();
            }
        }
        else if (resultCode == RESULT_DELETE && requestCode == CONFIG_OTHER_CARD) {
            int position = data.getIntExtra("position", -1);
            if (position == -1)
                return;
            mBusinessCardList.get(position).delete();
            mBusinessCardList.remove(position);
            mCardAdapter.notifyDataSetChanged();
        }
    }

    private void initGoogleApiClient() {
        RequestUtil.mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addApi(LocationServices.API)
                .build();

        RequestUtil.mApiClient.connect();
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


    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

}

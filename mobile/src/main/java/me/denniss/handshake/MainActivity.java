package me.denniss.handshake;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;


public class MainActivity extends ActionBarActivity{

    private BusinessCard mBusinessCard;
    private final int CONFIG_CARD = 1;
    private final int CONFIG_OTHER_CARD = 2;

    private List<BusinessCard> mBusinessCardList;
    private BusinessCardAdapter mCardAdapter;
    private GoogleApiClient mApiClient;
    private static final String START_ACTIVITY = "/start_activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBusinessCardList = Select.from(BusinessCard.class)
                .where(Condition.prop("is_you").eq("0")).list();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CONFIG_CARD) {
                BusinessCard newCard = (BusinessCard) data.getSerializableExtra("card");
                if (mBusinessCard == null)
                    mBusinessCard = newCard;
                else
                    mBusinessCard.setCard(newCard);
                mBusinessCard.save();
            } else if (requestCode == CONFIG_OTHER_CARD) {
                int position = data.getIntExtra("position", -1);
                if (position == -1)
                    return;
                BusinessCard newCard = (BusinessCard) data.getSerializableExtra("card");
                BusinessCard oldCard = mBusinessCardList.get(0);
                oldCard.delete();
                oldCard.setCard(newCard);
                oldCard.save();
                mCardAdapter.notifyDataSetChanged();
            }
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

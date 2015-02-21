package me.denniss.handshake;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class AddBusinessCard extends ActionBarActivity {

    private final int PICK_IMAGE = 1;
    String imageUri = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business_card);

        imageUri = "";
        final EditText businessName = (EditText)findViewById(R.id.businessName);
        final EditText name = (EditText)findViewById(R.id.name);
        final EditText email = (EditText)findViewById(R.id.email);
        final EditText jobTitle = (EditText)findViewById(R.id.jobTitle);
        final EditText address = (EditText)findViewById(R.id.address);
        final EditText number = (EditText)findViewById(R.id.phoneNumber);
        number.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        final EditText fax = (EditText)findViewById(R.id.fax);
        fax.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        final EditText website = (EditText)findViewById(R.id.website);
        final Button imageButton = (Button)findViewById(R.id.selectImageButton);

        BusinessCard card = (BusinessCard) getIntent().getSerializableExtra("card");
        if (card != null) {
            businessName.setText(card.businessName);
            name.setText(card.name);
            email.setText(card.email);
            jobTitle.setText(card.jobTitle);
            address.setText(card.address);
            number.setText(card.number);
            fax.setText(card.fax);
            website.setText(card.website);
            imageUri = card.imageUrl;
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_IMAGE);
            }
        });

        final Button cancel = (Button)findViewById(R.id.businessCardCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button save = (Button)findViewById(R.id.businessCardSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusinessCard card = new BusinessCard();
                card.businessName = businessName.getText().toString();
                card.name = name.getText().toString();
                card.email = email.getText().toString();
                card.jobTitle = jobTitle.getText().toString();
                card.address = address.getText().toString();
                card.number = number.getText().toString();
                card.fax = fax.getText().toString();
                card.website = website.getText().toString();
                card.imageUrl = imageUri;
                Intent intent = new Intent();
                intent.putExtra("card", card);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK)
        {
            Uri selectedImage = data.getData();

            imageUri = getRealPathFromURI(this,selectedImage);
        }

        super.onActivityResult(requestCode,resultCode,data);
    }


    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}

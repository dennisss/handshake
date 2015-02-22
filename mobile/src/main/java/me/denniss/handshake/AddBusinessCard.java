package me.denniss.handshake;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddBusinessCard extends ActionBarActivity {

    private final int PICK_IMAGE = 1;
    private String imageUri = "";
    private EditText businessName;
    private EditText name;
    private EditText email;
    private EditText jobTitle;
    private EditText address;
    private EditText number;
    private EditText fax;
    private EditText website;
    private Button imageButton;
    private BusinessCard currentCard;
    /**
     * Used for knowing the position of the item when passing back to the main activity.
     * (If modifying a business card that does not belong to the user)
     */
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business_card);

        businessName = (EditText)findViewById(R.id.businessName);
        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        jobTitle = (EditText)findViewById(R.id.jobTitle);
        address = (EditText)findViewById(R.id.address);
        number = (EditText)findViewById(R.id.phoneNumber);
        fax = (EditText)findViewById(R.id.fax);
        website = (EditText)findViewById(R.id.website);
        imageButton = (Button)findViewById(R.id.selectImageButton);

        imageUri = "";
        number.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        fax.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        currentCard = (BusinessCard) getIntent().getSerializableExtra("card");
        fillCardFields(currentCard);
        position = getIntent().getIntExtra("position", -1);

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
                updateBusinessCard();
                Intent intent = new Intent();
                intent.putExtra("card", currentCard);
                intent.putExtra("position",position);
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

    /**
     * Fill the fields of this activity with the values from the given business card
     * @param card The business card to get values from
     */
    public void fillCardFields(BusinessCard card) {
        if (card == null) {
            return;
        }
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

    /**
     * Create a business card from the values in the field
     */
    public void updateBusinessCard() {
        if (currentCard == null)
            currentCard = new BusinessCard();
        currentCard.businessName = businessName.getText().toString();
        currentCard.name = name.getText().toString();
        currentCard.email = email.getText().toString();
        currentCard.jobTitle = jobTitle.getText().toString();
        currentCard.address = address.getText().toString();
        currentCard.number = number.getText().toString();
        currentCard.fax = fax.getText().toString();
        currentCard.website = website.getText().toString();
        currentCard.imageUrl = imageUri;

    }

}

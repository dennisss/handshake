package me.denniss.handshake;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by Tomer on 2/21/2015.
 */
public class BusinessCard extends SugarRecord<BusinessCard> implements Serializable{
    public String name;
    public String email;
    public String businessName;
    public String jobTitle;
    public String address;
    public String number;
    public String fax;
    public String website;
    public String imageUrl;

    private boolean isYou = false;

    // ---------------------------------------------
    // Getters
    // ---------------------------------------------

    public boolean isYou() {
        return isYou;
    }

    // ---------------------------------------------
    // Setters
    // ---------------------------------------------

    public void isYou(boolean isYou) {
        this.isYou = isYou;
    }

    /**
     * Set the values of this business card to match that of another.
     * @param card The card to copy information from
     */
    public void setCard(BusinessCard card) {
        name = card.name;
        email = card.email;
        businessName = card.businessName;
        jobTitle = card.jobTitle;
        address = card.address;
        number = card.number;
        fax = card.fax;
        website = card.website;
        imageUrl = card.imageUrl;
    }
}

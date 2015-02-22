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

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getBusinessName() { return businessName; }
    public String getJobTitle() { return jobTitle; }
    public String getAddress() { return address; }
    public String getNumber() { return number; }
    public String getFax() { return fax; }
    public String getWebsite() { return website; }
    public String getImageUrl() { return imageUrl; }
    
    /**
     * Returns whether or not this is the user's business card
     * @return true if this is the user's business card
     */
    public boolean isYou() {
        return isYou;
    }

    // ---------------------------------------------
    // Setters
    // ---------------------------------------------

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public void setAddress(String address) { this.address = address; }
    public void setNumber(String number) { this.number = number; }
    public void setFax(String fax) { this.fax = fax; }
    public void setWebsite(String website) { this.website = website; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    /**
     * Set whether or not this is the users business card
     * @param isYou Whether or not this is the user's business card
     */
    public void isYou(boolean isYou) {
        this.isYou = isYou;
    }

    /**
     * Set the values of this business card to match that of another.
     * @param card The card to copy information from
     */
    public void setCard(BusinessCard card) {
        setName(card.getName());
        setEmail(card.getEmail());
        setBusinessName(card.getBusinessName());
        setJobTitle(card.getJobTitle());
        setAddress(card.getAddress());
        setNumber(card.getNumber());
        setFax(card.getFax());
        setWebsite(card.getWebsite());
        setImageUrl(card.getImageUrl());
    }
}

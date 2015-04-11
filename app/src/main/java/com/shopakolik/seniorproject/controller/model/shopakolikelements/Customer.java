package com.shopakolik.seniorproject.controller.model.shopakolikelements;

import java.util.ArrayList;

public class Customer extends User {
    private int customerId;
    private String name;
    private String surname;
    private boolean locationNotification;
    private boolean campaignNotification;
    private ArrayList<Store> favoriteStores;
    private ArrayList<Campaign> favoriteCampaigns;

    public Customer(String email, String password, String name, String surname) {
        super(email, password);
        this.name = name;
        this.surname = surname;
        locationNotification = true;
        campaignNotification = true;
        favoriteStores = new ArrayList<>();
        favoriteCampaigns = new ArrayList<>();
    }

    public Customer(int userId, String email, String password, int customerId, String name,
                    String surname, boolean locationNotification, boolean campaignNotification ) {
        super(userId, email, password);
        this.customerId = customerId;
        this.name = name;
        this.surname = surname;
        this.locationNotification = locationNotification;
        this.campaignNotification = campaignNotification;
        favoriteStores = new ArrayList<>();
        favoriteCampaigns = new ArrayList<>();
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isLocationNotification() {
        return locationNotification;
    }

    public void setLocationNotification(boolean locationNotification) {
        this.locationNotification = locationNotification;
    }

    public boolean isCampaignNotification() {
        return campaignNotification;
    }

    public void setCampaignNotification(boolean campaignNotification) {
        this.campaignNotification = campaignNotification;
    }

    public ArrayList<Store> getFavoriteStores() {
        return favoriteStores;
    }

    public void setFavoriteStores(ArrayList<Store> favoriteStores) {
        this.favoriteStores = favoriteStores;
    }

    public void addFavoriteStore(Store store){
        favoriteStores.add(store);
    }

    public void removeFavoriteStore(Store store){
        favoriteStores.remove(store);
    }

    public ArrayList<Campaign> getFavoriteCampaigns() {
        return favoriteCampaigns;
    }

    public void setFavoriteCampaigns(ArrayList<Campaign> favoriteCampaigns) {
        this.favoriteCampaigns = favoriteCampaigns;
    }

    public void addFavoriteCampaign(Campaign campaign){
        favoriteCampaigns.add(campaign);
    }

    public void removeFavoriteCampaign(Campaign campaign){
        favoriteCampaigns.remove(campaign);
    }
}

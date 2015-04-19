package com.shopakolik.seniorproject.model.shopakolikelements;

import java.util.ArrayList;

public class Store extends User {
    private int storeId;
    private String name;
    private String logo;
    private ArrayList<Category> categories;
    private ArrayList<Location> locations;
    private ArrayList<Campaign> campaigns;

    public Store(String email, String password, String name, String logo, ArrayList<Category> categories,
                 ArrayList<Location> locations) {
        super(email, password);
        this.name = name;
        this.logo = logo;
        this.categories = categories;
        this.locations = locations;
    }

    public Store(int userId, String email, String password, int storeId, String name, String logo,
                 ArrayList<Category> categories, ArrayList<Location> locations) {
        super(userId, email, password);
        this.storeId = storeId;
        this.name = name;
        this.logo = logo;
        this.categories = categories;
        this.locations = locations;
    }

    public Store(int storeId, String name, String logo,
                 ArrayList<Category> categories, ArrayList<Location> locations) {
        super(-1, null, null);
        this.storeId = storeId;
        this.name = name;
        this.logo = logo;
        this.categories = categories;
        this.locations = locations;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public void addCategory(Category category){
        categories.add(category);
    }

    public void removeCategory(String category){
        categories.remove(category);
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }

    public void addLocation(Location location){
        locations.add(location);
    }

    public void removeLocation(Location location){
        locations.remove(location);
    }

    public ArrayList<Campaign> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(ArrayList<Campaign> campaigns) {
        this.campaigns = campaigns;
    }

    public void addCampaign(Campaign campaign){
        campaigns.add(campaign);
    }

    public void removeCampaign(Campaign campaign){
        campaigns.remove(campaign);
    }
}

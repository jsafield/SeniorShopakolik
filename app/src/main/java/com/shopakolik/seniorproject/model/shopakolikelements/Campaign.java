package com.shopakolik.seniorproject.model.shopakolikelements;

import java.util.Date;

public class Campaign extends Store {
    private int campaignId;
    private Date startDate;
    private Date endDate;
    private String image;
    private CampaignType type;
    private String condition;
    private String details;
    private int percentage;
    private float amount;
    private int storeId;

    public Campaign(Date startDate, Date endDate, String image, CampaignType type, String condition,
                    String details, int percentage, float amount, int storeId) {
        super(storeId, null, null);
        this.startDate = startDate;
        this.endDate = endDate;
        this.image = image;
        this.type = type;
        this.condition = condition;
        this.details = details;
        this.percentage = percentage;
        this.amount = amount;
        this.storeId = storeId;
    }
    public Campaign(Date startDate, Date endDate, String image, CampaignType type, String condition,
                    String details, int percentage, float amount) {
        super(-1, null, null);
        this.startDate = startDate;
        this.endDate = endDate;
        this.image = image;
        this.type = type;
        this.condition = condition;
        this.details = details;
        this.percentage = percentage;
        this.amount = amount;
        this.storeId = storeId;
    }

    public Campaign(int campaignId, Date startDate, Date endDate, String image, CampaignType type,
                    String condition, String details, int percentage, float amount, int storeId) {
        super(storeId, null, null);
        this.campaignId = campaignId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.image = image;
        this.type = type;
        this.condition = condition;
        this.details = details;
        this.percentage = percentage;
        this.amount = amount;
        this.storeId = storeId;
    }

    public Campaign(int campaignId, Date startDate, Date endDate, String image, CampaignType type,
                    String condition, String details, int percentage, float amount, int storeId,
                    String name, String logo) {
        super(storeId, name, logo);
        this.campaignId = campaignId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.image = image;
        this.type = type;
        this.condition = condition;
        this.details = details;
        this.percentage = percentage;
        this.amount = amount;
        this.storeId = storeId;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CampaignType getType() {
        return type;
    }

    public void setType(CampaignType type) {
        this.type = type;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
}
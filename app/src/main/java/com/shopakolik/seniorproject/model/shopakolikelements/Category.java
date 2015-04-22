package com.shopakolik.seniorproject.model.shopakolikelements;

import java.io.Serializable;

public class Category implements Serializable {
    private int categoryId;
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public Category(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString (){
        return this.name;
    }
}

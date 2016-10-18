package com.foodcity;

/**
 * Created by 000407 on 10/17/16.
 */
public class StockItem {
    private String id, name, unit;
    private float unitPrice;

    //CONSTRUCTORS
    StockItem(String id, String name, String unit, float unitPrice){
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.unitPrice = unitPrice;
    }

    //GETTERS & SETTERS

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    String getUnit() {
        return unit;
    }

    float getUnitPrice() {
        return unitPrice;
    }
}

package com.foodcity;

/**
 * Created by 000407 on 10/17/16.
 */
public class StockItem {
    private String id, name;
    private Unit unit;
    private float unitPrice;

    //CONSTRUCTORS
    StockItem(String id, String name, String unit, float unitPrice) throws InvalidUnitException{
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        for(Unit u : Unit.values()){
            if(u.getUnitName().equals(unit)) {
                this.unit = u;
                return;
            }
        }
        if(this.unit ==  null)
            throw new InvalidUnitException();
    }

    //GETTERS & SETTERS

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    Unit getUnit() {
        return unit;
    }

    float getUnitPrice() {
        return unitPrice;
    }
}

package com.foodcity;

/**
 * Created by hsenid on 10/19/16.
 */
public enum Unit {
    LIQUID_MILI("ml", 10.0f),
    LIQUID("l", 0.5f),
    WEIGHT("g", 1.0f),
    WEIGHT_KILO("kg", 0.5f),
    UNIT("", 1.0f);

    private final String unitName;
    private final float minValue;

    Unit(String name, float min){
        this.unitName = name;
        this.minValue = min;
    }

    public String getUnitName() {
        return unitName;
    }

    public float getMinValue() {
        return minValue;
    }

    public static Unit getUnitOfName(String name){
        for(Unit u : Unit.values()){
            if(u.getUnitName().equals(name))
                return u;
        }
        return null;
    }
}
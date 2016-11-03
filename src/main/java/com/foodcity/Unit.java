package com.foodcity;

/**
 * Enumeration that holds various data about the Units used for measurements.
 * Created by 000407 on 10/19/16.
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

    /**
     * Retrieves the name of the unit.
     * @return Returns a string containing the name of the unit.
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * Retrieves the minimum buy-able quantity of a unit.
     * @return Returns a float that is the minimum buy-able amount from the particular unit.
     */
    public float getMinValue() {
        return minValue;
    }

    /**
     * Retrieves the unit instance that has a particular name.
     * @param name name of the unit that needs to be retrieved.
     * @return Returns the com.foodcity.Unit instance in the enumeration if found, null otherwise.
     * @deprecated will be removed in the next release.
     */
    public static Unit getUnitOfName(String name){
        for(Unit u : Unit.values()){
            if(u.getUnitName().equals(name))
                return u;
        }
        return null;
    }
}
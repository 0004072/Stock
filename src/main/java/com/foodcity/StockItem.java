package com.foodcity;

/**
 * Class that defines the blueprint of a stock item, in the system.
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

    StockItem(String name, String unit, float unitPrice) throws InvalidUnitException{
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

    public void setId(String id) {
        this.id = id;
    }

    //Overrode equals method for comparison.

    /**
     * Overrides the public boolean equals(@Nullable Object obj) method in java.lang.Object class. Allows comparing two StockItem objects for their similarity in parameters. Two instances are considered equal if they are instances of StockItem class and they have similar values for all of their attributes.
     * @param obj java.lang.Object instance that needs to be compared with this
     * @return True, if they are equal as mentioned above, false otherwise.
     */
    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof StockItem))
            return false;

        StockItem item = (StockItem)obj;

        boolean equals = item.getId().equals(this.getId()) && item.getName().equals(this.getName()) && item.getUnit().equals(this.getUnit()) && (item.getUnitPrice() == this.getUnitPrice());
        return equals;
    }

    /**
     * Overrides the public String toString() method in java.lang.Object, to display the content of the object instance.
     * @return Returns a string representation of the object that includes its parameters and their values.
     */
    @Override
    public String toString(){
        return String.format("Item info:\nID:%s\nName:%s\nUnit Price:%.2f per %s\n", this.getId(), this.getName(), this.getUnitPrice(), this.getUnit().equals(Unit.UNIT) ? "Unit" : this.getUnit().getUnitName());
    }
}

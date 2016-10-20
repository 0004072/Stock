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
    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof StockItem))
            return false;

        StockItem item = (StockItem)obj;

        boolean equals = item.getId().equals(this.getId()) && item.getName().equals(this.getName()) && item.getUnit().equals(this.getUnit()) && (item.getUnitPrice() == this.getUnitPrice());
        return equals;
    }

    @Override
    public String toString(){
        return String.format("Item info:\nID:%s\nName:%s\nUnit Price:%.2f per %s\n", this.getId(), this.getName(), this.getUnitPrice(), this.getUnit().equals(Unit.UNIT) ? "Unit" : this.getUnit().getUnitName());
    }
}

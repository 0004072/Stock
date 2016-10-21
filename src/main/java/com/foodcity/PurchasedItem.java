package com.foodcity;

/**
 * Class that defines the blueprint of a purchased item, in the system.
 * Created by hsenid on 10/17/16.
 */
public class PurchasedItem {
    private StockItem item;
    private float qty;

    public PurchasedItem(StockItem item, float qty){
        this.item = item;
        this.setQty(qty);
    }

    /**
     * Getter for this.qty.
     * @return Returns this.qty.
     */
    public float getQty() {
        return qty;
    }

    /**
     * Setter for this.
     * @param qty value that should be assigned to this.qty.
     */
    public void setQty(float qty) {
        this.qty = qty;
    }

    public StockItem getItem() {
        return item;
    }
}

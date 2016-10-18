package com.foodcity;

/**
 * Created by hsenid on 10/17/16.
 */
public class PurchasedItem {
    private StockItem item;
    private float qty;

    public PurchasedItem(StockItem item, float qty){
        this.item = item;
        this.setQty(qty);
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public StockItem getItem() {
        return item;
    }
}

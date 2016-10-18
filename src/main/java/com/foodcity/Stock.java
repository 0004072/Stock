package com.foodcity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Class that implements the structure of the stock.
 * Created by 000407 on 10/17/2016.
 */
public class Stock
{
    private Map<String, Map<StockItem, Float>> stock = new HashMap<>();

    /*public static void main( String[] args )
    {
        *//*Stock testStock = new Stock();
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        testStock.addItem(new StockItem("I00001", "Wheat Flour", "kg", 55.0f), 100.0f);
        testStock.addItem(new StockItem("I00002", "Sugar", "kg", 45.0f), 100.0f);
        testStock.addItem(new StockItem("I00003", "Milk Powder(400g)", "", 395.0f), 50.0f);
        testStock.viewCurrentStock();
        StockItem item = new StockItem("I00001", "Wheat Flour", "kg", 50.0f);
        testStock.addItem(item, 200.0f, keyboard);
        testStock.viewCurrentStock();*//*
    }*/

    /**
     * Adds a pre-defined StockItem object to the stock.
     * @param item The StockItem instance that needs to be added
     * @param qty Quantity of the stock item
     */
    void addItem(StockItem item, float qty){
        Map<StockItem, Float> obj = new HashMap<>();
        obj.put(item, qty);
        this.stock.putIfAbsent(item.getId(), obj);
    }

    /**
     * Interacts with the user to verify the overwriting of existing objects.
     * @param item StockItem instance need to be present in the stock
     * @param qty Quantity that should be available
     * @param keyboard java.io.BufferedReader instance that handles capturing user inputs.
     */
    void addItem(StockItem item, float qty, BufferedReader keyboard){
        Map<StockItem, Float> itemQuantity = new HashMap<>();
        boolean itemAlreadyExists = this.stock.containsKey(item.getId());
        if(itemAlreadyExists){
            System.out.println("Item already exists!");
            Map.Entry existingEntry = (Map.Entry)this.stock.get(item.getId()).entrySet().iterator().next();
            StockItem existingItem = (StockItem)existingEntry.getKey();
            Float existingQty = (Float)existingEntry.getValue();
            System.out.println(String.format("%1$-"+20+"s", "Existing")+" "+String.format("%1$-"+20+"s", "New"));
            System.out.println(String.format("%1$-"+20+"s", existingItem.getId())+" "+String.format("%1$-"+20+"s", item.getId()));
            System.out.println(String.format("%1$-"+20+"s", existingItem.getName())+" "+String.format("%1$-"+20+"s", item.getName()));
            System.out.println(String.format("%1$-"+20+"s", existingItem.getUnitPrice())+" "+String.format("%1$-"+20+"s", item.getUnitPrice()));
            System.out.println(String.format("%1$-"+20+"s", existingQty)+" "+String.format("%1$-"+20+"s", qty));
            System.out.print("Would you like to overwrite the existing data?(y/n)");
            System.out.flush();
            try {
                String res = keyboard.readLine();
                if(res.equals("y")){
                    itemQuantity.put(item, qty);
                    this.stock.put(item.getId(), itemQuantity);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            this.stock.putIfAbsent(item.getId(), itemQuantity);
    }

    StockItem getItem(String id){
        StockItem item = getStockItem(id).getKey();
        return item;
    }

    private Map.Entry<StockItem, Float> getStockItem(String id){
        Map<StockItem, Float> entry = this.stock.get(id);
        Map.Entry<StockItem, Float> stockItem = entry.entrySet().iterator().next();
        return stockItem;
    }

    /**
     * Checks out the item from the stock and update the stock status. Here, the stock status is validated before the checkout is completed.
     * @param id ID of the item needs to be checked out
     * @param qty Quantity that needs to be bought
     * @return Returns a new PurchasedItem instance contains a StockItem instance and the requested quantity.
     * @throws ZeroStockLevelException if the requested StockItem is out of stock.
     * @throws InsufficientQuantityException if the requested quantity is lesser than the available quantity.
     */
    public PurchasedItem buyItem(String id, float qty) throws ZeroStockLevelException, InsufficientQuantityException{
        Map.Entry<StockItem, Float> stockItem = getStockItem(id);
        StockItem currentItem = stockItem.getKey();
        float currentLevel = stockItem.getValue();

        if(currentLevel == 0.0f)
            throw new ZeroStockLevelException();

        if(currentLevel < qty)
            throw new InsufficientQuantityException();

        stockItem.setValue(currentLevel - qty);
        return new PurchasedItem(currentItem, qty);
    }

    /**
     * Finds the stock in hand for a desired StockItem
     * @param id ID of the StockItem that needs to be examined
     * @return the stock in hand as a float value.
     */
    public float getCurrentStockLevel(String id){
        Map.Entry<StockItem, Float> stockItem = getStockItem(id);
        return stockItem.getValue();
    }

    /**
     * Displays the existing stock status in a formatted, human readable manner.
     */
    public void viewCurrentStock(){
        Iterator<Map<StockItem, Float>> it = stock.values().iterator();
        while(it.hasNext()){
            Map<StockItem, Float> itemEntry = it.next();
            Map.Entry<StockItem, Float> item = itemEntry.entrySet().iterator().next();
            StockItem itemData = item.getKey();
            // TODO: 10/18/16 Format this output to be a little more pleasant! :P 
            System.out.println(itemData.getId()+" "+itemData.getName()+" "+itemData.getUnitPrice()+" "+item.getValue());
        }
    }
}

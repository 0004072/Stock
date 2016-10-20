package com.foodcity;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Class that implements the structure of the stock.
 * Created by 000407 on 10/17/2016.
 */

public class Stock
{
    private Map<String, Map<StockItem, Float>> stock;
    private int nextId;
    private String prefix;

    public Stock(BufferedReader keyboard) throws IOException{
        System.out.print("Enter the prefix for the item IDs:");
        System.out.flush();
        this.prefix = keyboard.readLine().toUpperCase();
        this.stock = new HashMap<>();
        this.nextId = 1;
    }
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
        DecimalFormat idFormat = new DecimalFormat("00000");
        StringBuffer itemId = new StringBuffer(this.prefix);
        itemId.append(idFormat.format(this.nextId));
        this.nextId++;
        item.setId(itemId.toString());
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
        StockItem existingItem = null;
        Float existingQty = null;
        Map.Entry existingEntry = null;
        Map<StockItem, Float> newEntry = new HashMap<>();
        Iterator<Map<StockItem, Float>> stockIterator = stock.values().iterator();
        boolean itemAlreadyExists = false;
        while(stockIterator.hasNext()){
            existingEntry = stockIterator.next().entrySet().iterator().next();
            existingItem = (StockItem)existingEntry.getKey();
            existingQty = (Float)existingEntry.getValue();
            if(existingItem.getName().equals(item.getName())){
                itemAlreadyExists = true;
                break;
            }
        }

        if(itemAlreadyExists){
            System.out.println(existingItem.getId());
            System.out.println("Item already exists!");
            System.out.println(String.format("%1$-"+20+"s", "Existing")+" "+String.format("%1$-"+20+"s", "New"));
            System.out.println(String.format("%1$-"+20+"s", existingItem.getName())+" "+String.format("%1$-"+20+"s", item.getName()));
            System.out.println(String.format("%1$-"+20+"s", existingItem.getUnitPrice())+" "+String.format("%1$-"+20+"s", item.getUnitPrice()));
            System.out.println(String.format("%1$-"+20+"s", existingQty)+" "+String.format("%1$-"+20+"s", qty));
            System.out.print("Would you like to overwrite the existing data?(y/n)");
            System.out.flush();
            try {
                String res = keyboard.readLine();
                if(res.equals("y")){
                    newEntry.put(item, qty);
                    this.stock.put(existingItem.getId(), newEntry);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            this.stock.putIfAbsent(item.getId(), newEntry);
    }

    StockItem getItem(String id){
        Map.Entry<StockItem, Float> stockItem = getStockItem(id);

        if(stockItem == null)
            return null;

        StockItem item = stockItem.getKey();

        return item;
    }

    private Map.Entry<StockItem, Float> getStockItem(String id){
        Map<StockItem, Float> entry = this.stock.get(id);
        if(entry == null)
            return null;

        return entry.entrySet().iterator().next();
    }

    /**
     * Checks out the item from the stock and update the stock status. Here, the stock status is validated before the checkout is completed.
     * @param id ID of the item needs to be checked out
     * @param qty Quantity that needs to be bought
     * @return Returns a new PurchasedItem instance contains a StockItem instance and the requested quantity.
     * @throws ZeroStockLevelException if the requested StockItem is out of stock.
     * @throws InsufficientQuantityException if the requested quantity is lesser than the available quantity.
     */
    public PurchasedItem buyItem(String id, float qty) throws ZeroStockLevelException, InsufficientQuantityException, InvalidUnitException{
        Map.Entry<StockItem, Float> stockItem = getStockItem(id);
        StockItem currentItem = stockItem.getKey();
        float currentLevel = stockItem.getValue();
        Unit unit = currentItem.getUnit();
        if((qty % unit.getMinValue()) != 0)
            throw new InvalidUnitException(qty, unit.getMinValue(), unit.getUnitName());
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
        for (Map<StockItem, Float> itemEntry : stock.values()) {
            Map.Entry<StockItem, Float> item = itemEntry.entrySet().iterator().next();
            StockItem itemData = item.getKey();
            // TODO: 10/18/16 Format this output to be a little more pleasant! :P 
            System.out.println(itemData.getId() + " " + itemData.getName() + " " + itemData.getUnitPrice() + " " + item.getValue());
        }
    }

    /**
     * Tops up the stock level by the specified amount. Then generates the report for stock update operation.
     * @param item StockItem instance that needs to be topped up.
     * @param qty Quantity that the selected StockItem needs to be topped up by.
     */
    public void updateStockLevel(StockItem item, float qty){
        Map.Entry<StockItem, Float> stockItem = this.stock.get(item.getId()).entrySet().iterator().next();
        StringBuffer message = new StringBuffer();
        message.append(stockItem.getKey().toString());
        message.append(stockItem.getValue()+" => ");
        stockItem.setValue(qty + stockItem.getValue());
        message.append(stockItem.getValue());
        System.out.println(message.toString());
    }
}

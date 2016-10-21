package com.foodcity;

import java.io.*;
import java.util.*;

/**
 * Contains the main method that runs the application.
 * Created by 000407 on 10/17/16.
 */
public class FoodCityHome {
    public static void main(String[] args) throws IOException, InvalidUnitException{
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        Stock stock = new Stock(keyboard);

        Map<String, ShoppingCart> shoppingCarts = new HashMap<>();

        //Hard coded stock data. Reading the files/database could be implemented here.
        stock.addItem(new StockItem("Wheat Flour", "kg", 55.0f), 100.0f);
        stock.addItem(new StockItem("Sugar", "kg", 45.0f), 100.0f);
        stock.addItem(new StockItem("Milk Powder(400g)", "", 395.0f), 50.0f);
        stock.addItem(new StockItem("Soap", "", 65.0f), 100.0f);
        stock.addItem(new StockItem("Tooth Paste (75g)", "", 67.5f), 100.0f);
        stock.addItem(new StockItem("Tea Leaves (100g)", "", 120.0f), 100.0f);
        stock.addItem(new StockItem("Jam (200g)", "", 230.75f), 50.0f);
        stock.addItem(new StockItem("Laundry Soap", "", 55.0f), 100.0f);
        stock.addItem(new StockItem("Curry Powder (100g)", "", 120.0f), 50.0f);
        stock.addItem(new StockItem("Chillie Powder (100g)", "", 110.0f), 50.0f);

        System.out.println("FoodCity :: Point of Sale");
        System.out.println("V1.0");
        System.out.println("-------------------------");
        String cmd;
        ui:
        while(true){
            System.out.print("FOODCITY>");
            System.out.flush();
            cmd = keyboard.readLine();
            switch(cmd){
                case "newstock":
                    System.out.print("Item Name:");
                    System.out.flush();
                    String newItemName = keyboard.readLine();

                    System.out.print("Item Unit Price:");
                    System.out.flush();
                    Float newItemUnitPrice = Float.parseFloat(keyboard.readLine());

                    System.out.print("Unit measured in:");
                    System.out.flush();
                    String newItemUnit = keyboard.readLine();

                    System.out.print("Stock Qty:");
                    System.out.flush();
                    Float newItemQty = Float.parseFloat(keyboard.readLine());

                    StockItem newItem = new StockItem(newItemName, newItemUnit, newItemUnitPrice);
                    stock.addItem(newItem, newItemQty, keyboard);
                    break;

                case "topup":
                    topup:
                    while(true){
                        System.out.print("Item ID:");
                        System.out.flush();
                        String itemId = keyboard.readLine().toUpperCase();
                        StockItem item = stock.getItem(itemId);
                        if(item == null){
                            System.out.print("Invalid ID! Retry?(y/n)");
                            System.out.flush();
                            String retryConsent = keyboard.readLine();
                            switch(retryConsent){
                                case "y":
                                    continue topup;

                                case "n":
                                    break topup;

                                default:
                                    System.out.println("Invalid choice! Assumed YES!");
                                    continue topup;
                            }
                        }
                        System.out.println(item.getId()+" "+item.getName());
                        while(true){
                            System.out.print("Quantity to add:");
                            System.out.flush();
                            String addQty = keyboard.readLine();
                            if(addQty.matches("[\\d]+(\\.[\\d]+)?")){
                                stock.updateStockLevel(item, Float.parseFloat(addQty));
                                break topup;
                            }
                            else{
                                System.out.println("Invalid quantity! Please retry...");
                            }
                        }
                    }
                    break;

                case "showstock":
                    stock.viewCurrentStock();
                    break;

                case "showcart":

                    break;

                case "buy":
                    ShoppingCart sc = new ShoppingCart();
                    System.out.print("Enter cart label:");
                    System.out.flush();
                    String lbl = keyboard.readLine();
                    if(shoppingCarts.putIfAbsent(lbl, sc) == null)
                        sc = shoppingCarts.get(lbl);

                    additem:
                    while(true){
                        System.out.print("Item ID:");
                        System.out.flush();
                        String id = keyboard.readLine().toUpperCase();
                        StockItem item = stock.getItem(id);
                        if(item == null)
                            System.out.println("Invalid ID!");

                        else {
                            System.out.println(item.getName() + " : " + item.getUnitPrice() + " per " + (item.getUnit().getUnitName().equals("") ? "unit" : item.getUnit().getUnitName()) + ". " + stock.getCurrentStockLevel(id) + " in stock.");
                            System.out.print("Quantity:");
                            System.out.flush();
                            float qty = Float.parseFloat(keyboard.readLine());//Should be refactored to implement validations
                            System.out.println(item.getName() + " : " + item.getUnitPrice() + " X " + qty + item.getUnit().getUnitName());
                            try {
                                PurchasedItem pi = stock.buyItem(id, qty);
                                sc.addToCart(pi);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                                if(e instanceof InsufficientQuantityException){
                                    System.out.print("would you like to buy the remaining quantity?(y/n)");
                                    String consent = keyboard.readLine();
                                    if(consent.equals("y")){
                                        try {
                                            PurchasedItem pi = stock.buyItem(id, stock.getCurrentStockLevel(id));
                                            sc.addToCart(pi);
                                        }
                                        catch(Exception ex){
                                            //Should never reach here
                                            ex.printStackTrace();
                                        }
                                    }
                                    else {
                                        System.out.println("Item was not added to the cart!");
                                    }
                                }
                                else if(e instanceof InvalidUnitException){
                                    System.out.println("Item was not added to the cart!");
                                }
                            }
                        }

                        while(true){
                            System.out.print("Continue adding items?(y/n):");
                            System.out.flush();
                            String input = keyboard.readLine().toLowerCase();
                            switch(input){
                                case "y":
                                    continue additem;

                                case "n":
                                    break additem;

                                default:
                                    System.out.println("Invalid input!");
                            }
                        }
                    }
                    break;

                case "bill":
                    System.out.print("Enter cart label:");
                    System.out.flush();
                    String lblp = keyboard.readLine();
                    ShoppingCart scp = shoppingCarts.get(lblp);
                    if(scp == null)
                        System.out.println("No shopping cart found!");
                    else {
                        if(scp.generateBill(keyboard))
                            shoppingCarts.remove(scp);
                    }
                    break;

                case "exit":
                    //Flushing the stock data into files could be implemented here
                    System.out.println("bye!");
                    break ui;

                default:
                    System.out.println("Invalid command!");
            }
        }
    }
}

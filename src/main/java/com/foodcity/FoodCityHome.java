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
        Stock stock = new Stock();

        Map<String, ShoppingCart> shoppingCarts = new HashMap<>();

        stock.addItem(new StockItem("I00001", "Wheat Flour", "kg", 55.0f), 100.0f);
        stock.addItem(new StockItem("I00002", "Sugar", "kg", 45.0f), 100.0f);
        stock.addItem(new StockItem("I00003", "Milk Powder(400g)", "", 395.0f), 50.0f);
        stock.addItem(new StockItem("I00004", "Soap", "", 65.0f), 100.0f);
        stock.addItem(new StockItem("I00005", "Tooth Paste (75g)", "", 67.5f), 100.0f);
        stock.addItem(new StockItem("I00006", "Tea Leaves (100g)", "", 120.0f), 100.0f);
        stock.addItem(new StockItem("I00007", "Jam (200g)", "", 230.75f), 50.0f);
        stock.addItem(new StockItem("I00008", "Laundry Soap", "", 55.0f), 100.0f);
        stock.addItem(new StockItem("I00009", "Curry Powder (100g)", "", 120.0f), 50.0f);
        stock.addItem(new StockItem("I00010", "Chillie Powder (100g)", "", 110.0f), 50.0f);

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
                case "addstock":
                    System.out.print("Item ID:");
                    System.out.flush();
                    String newItemId = keyboard.readLine();

                    System.out.print("Item Name:");
                    System.out.flush();
                    String newItemName = keyboard.readLine();

                    System.out.print("Item Unit Price:");
                    System.out.flush();
                    Float newItemUnitPrice = Float.parseFloat(keyboard.readLine());

                    System.out.print("Unit:");
                    System.out.flush();
                    String newItemUnit = keyboard.readLine();

                    System.out.print("Item Qty:");
                    System.out.flush();
                    Float newItemQty = Float.parseFloat(keyboard.readLine());

                    StockItem newItem = new StockItem(newItemId, newItemName, newItemUnit, newItemUnitPrice);
                    stock.addItem(newItem, newItemQty, keyboard);
                    break;

                case "showstock":
                    stock.viewCurrentStock();
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
                            System.out.println(item.getName() + " : " + item.getUnitPrice() + "X" + qty + item.getUnit().getUnitName());
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

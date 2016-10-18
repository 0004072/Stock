package com.foodcity;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * Class that implements the shopping cart functions.
 * Created by 000407 on 10/17/16.
 */
public class ShoppingCart {

    private Set<PurchasedItem> cart;

    /**
     * Default constructor overrode to instantiate the cart.
     */
    public ShoppingCart(){
        this.cart = new HashSet<>();
    }

    /**
     * Adds the PurchasedItem to the cart.
     * @param item PurchasedItem to be present in the cart.
     */
    public void addToCart(PurchasedItem item){
        if(cart.contains(item)){
            cart.remove(item);
            item.setQty(item.getQty()*2);
            cart.add(item);
        }
        else
            cart.add(item);
    }

    /**
     * Getter for the cart instance. Used for testing purposes.
     * @return the cart instance.
     * @deprecated Will be removed in the next release.
     */
    public Set<PurchasedItem> getCart() {
        return cart;
    }

    /**
     * Generated the bill for a given cart.
     * @param keyboard a BufferedReader instance to handle the user inout capturing.
     * @throws IOException in case of an IOException is occurred.
     */
    public void generateBill(BufferedReader keyboard) throws IOException{
        System.out.println("~Food City~\nOn your way home...");
        System.out.println("No. 221B, Baker Street, London.");
        System.out.println(" # " + String.format("%1$-"+20+"s", "Name") + String.format("%1$-"+12+"s", "Unit Price") + String.format("%1$-"+7+"s", "Qty.") + String.format("%1$-"+12+"s", "Price"));
        int i = 1;
        Float total = 0.0f;
        for(PurchasedItem itm : cart){
            String name = itm.getItem().getName();
            String unitPrice = ((Float)itm.getItem().getUnitPrice()).toString().replace('f', '0');
            String qty = ((Float)itm.getQty()).toString().replace('f', '0');
            String unit = itm.getItem().getUnit();
            Float price = itm.getItem().getUnitPrice() * itm.getQty();
            total += price;
            String billPrice = (price).toString().replace('f', '0');
            System.out.println(String.format("%1$"+3+"s", (i + " ")) + String.format("%1$-"+20+"s", name) + String.format("%1$"+12+"s", unitPrice) + String.format("%1$"+7+"s", (qty+""+unit)) + String.format("%1$"+12+"s", billPrice));
            i++;
        }

        String displayTot = String.format("%1$"+12+"s", total);
        displayTot = "Sub Total :" + displayTot;
        System.out.println(String.format("%1$"+54+"s", displayTot));
        while(true){
            System.out.print("Cash?");
            System.out.flush();
            // TODO: 10/18/16 Input should be validated! 
            Float cash = Float.parseFloat(keyboard.readLine());
            if(cash < total){
                System.out.println("Insufficient funds! You need to pay "+total+"LKR in cash!");
                continue;
            }
                float change = cash - total;
            System.out.println("Change:" + change);
            countChange(change);
        }
    }

    /**
     * Counts the required notes to deliver the change amount.
     * @param change change amount needs to be given away with money
     * @return int[] array containing the number of notes in each note type starting from 5000 to 0.50 LKR.
     */
    public int[] countChange(float change){
        float notes[] = {5000, 2000, 1000, 500, 100, 50, 20, 10, 5, 2, 1, 0.5f};
        int count[] = new int[12];
        float rem;
        for(int i = 0; i < notes.length; i++){
            rem = change % notes[i];
            int noOfNotes = (int)(change / notes[i]);
            change = rem;
            count[i] = noOfNotes;
        }

        for(int i = 0; i < count.length; i++){
            if(count[i] == 0)
                continue;
            System.out.println(String.format("%1$"+6+"s", notes[i]) + " X " + String.format("%1$"+2+"s",count[i]));
        }
        return count;
    }
}

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
        System.out.println(cart.contains(item));
        if(cart.contains(item)){
            System.out.println("Item already exists!");
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
    public boolean generateBill(BufferedReader keyboard) throws IOException{
        boolean success = false;
        StringBuilder bill = new StringBuilder();
        bill.append("~Food City~\nOn your way home...");
        bill.append("\nNo. 221B, Baker Street, London.");
        bill.append("\n===RECEIPT===\n");
        bill.append("\n # " + String.format("%1$-"+20+"s", "Name") + String.format("%1$-"+12+"s", "Unit Price") + String.format("%1$-"+7+"s", "Qty.") + String.format("%1$-"+12+"s", "Price"));
        int i = 1;
        Float total = 0.0f;
        for(PurchasedItem itm : cart){
            String name = itm.getItem().getName();
            String unitPrice = ((Float)itm.getItem().getUnitPrice()).toString().replace('f', '0');
            String qty = ((Float)itm.getQty()).toString().replace('f', '0');
            String unit = itm.getItem().getUnit().getUnitName();
            Float price = itm.getItem().getUnitPrice() * itm.getQty();
            total += price;
            String billPrice = (price).toString();
            bill.append("\n" + String.format("%1$"+3+"s", (i + " ")) + String.format("%1$-"+20+"s", name) + String.format("%1$"+12+"s", unitPrice) + String.format("%1$"+7+"s", (qty+""+unit)) + String.format("%1$"+12+"s", billPrice));
            i++;
        }

        //Rounding of total
        float cents = total % 1;

        if(cents > 0.5f)
            total = total - cents + 1;

        else if(cents < 0.5f)
            total -= cents;

        String displayTot = String.format("%1$"+12+"s", total);
        displayTot = "Sub Total :" + displayTot;
        bill.append("\n" + String.format("%1$"+54+"s", displayTot));
        while(true){
            System.out.println("Total : "+total);
            discounts:
            while(true){
                System.out.print("Discounts/Taxes?(y/n)");
                System.out.flush();
                String choice = keyboard.readLine();
                switch(choice){
                    case "y":
                        discountamount:
                        while(true){
                            System.out.print("Discount/tax rate(discount rate < 0 && taxt rate > 0):");
                            System.out.flush();
                            String rate = keyboard.readLine();
                            if(rate.matches("(-)?[01](\\.[\\d])?")){
                                float discountedTotal = discount(total, Float.parseFloat(rate));
                                if(discountedTotal == total){
                                    System.out.print("No discounts/taxes added! Retry?(y/n)");
                                    System.out.flush();
                                    String continueDiscount = keyboard.readLine();
                                    switch(continueDiscount) {
                                        case "y":
                                            continue discountamount;
                                            
                                        case "n":
                                            break discounts;
                                        
                                        default:
                                            System.out.println("Invalid choice! Assumed NO!");
                                            // TODO: 10/19/16 Start over from here! 
                                    }
                                    
                                }
                                break;
                            }
                            else
                                System.out.println("Invalid discount rate! Try again!");
                        }
                        break;

                    case "n":
                        break discounts;

                    default:
                        System.out.println("Invalid choice.");
                }
            }
            System.out.print("Cash?");
            System.out.flush();
            // TODO: 10/18/16 Input should be validated! 
            Float cash = Float.parseFloat(keyboard.readLine());
            if(cash < total){
                System.out.println("Insufficient funds! You need to pay "+total+"LKR in cash!");
                continue;
            }
                float change = cash - total;
            bill.append("\nCash: " + cash);
            bill.append("\nChange: " + change);
            countChange(change);
            System.out.println(bill.toString());
            success = true;
            break;
        }
        return success;
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

    public float discount(float amount, float rate){
        if(rate < -1.0f || rate > 1.0f){
            System.out.println("Invalid discount rate! Should be between -1.0 and +1.0");
            return amount;
        }
        amount += (amount * rate);
        return amount;
    }
}

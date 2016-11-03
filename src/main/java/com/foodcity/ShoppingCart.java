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
        boolean itemAlreadyContains = false;
        Iterator<PurchasedItem> cartContent = this.cart.iterator();
        PurchasedItem pi = null;
        while(cartContent.hasNext()){
            pi = cartContent.next();
            if(pi.getItem().getId().equals(item.getItem().getId())){
                itemAlreadyContains = true;
                break;
            }
        }
        if(itemAlreadyContains){
            pi.setQty(pi.getQty() + item.getQty());
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
     * @return true if the process completed successfully, false otherwise.
     * @throws IOException in case of an IOException is occurred.
     */
    public boolean generateBill(BufferedReader keyboard) throws IOException{
        boolean success = false;
        StringBuilder bill = new StringBuilder();
        bill.append("~Food City~\nOn your way home...");
        bill.append("\nNo. 221B, Baker Street, London.");
        bill.append("\n\n===RECEIPT===\n");

        RowCell colSerNo = new RowCell(3, "-", "#");
        RowCell colName = new RowCell(30, "-", "Name");
        RowCell colUnitPrice = new RowCell(12, "-", "Unit Price");
        RowCell colQty = new RowCell(7, "-", "Qty.");
        RowCell colPrice = new RowCell(12, "-", "Price");
        Table billTable = new Table(colSerNo, colName, colUnitPrice, colQty, colPrice);

        billTable.addSectionBreak(1);
        int i = 1;
        Float total = 0.0f;
        for(PurchasedItem itm : cart){
            String name = itm.getItem().getName();
            String unitPrice = String.format("%.2f", itm.getItem().getUnitPrice());
            String qty = String.format("%.2f", itm.getQty());
            String unit = itm.getItem().getUnit().getUnitName();
            float price = itm.getItem().getUnitPrice() * itm.getQty();
            total += price;
            String billPrice = String.format("%.2f", price);
            billTable.addRow(String.valueOf(i), name, unitPrice, String.format("%s%s", qty, unit), billPrice);
            i++;
        }

        //Rounding of total
        float cents = total % 1;

        if(cents > 0.5f)
            total = total - cents + 1;

        else if(cents < 0.5f)
            total -= cents;

        billTable.addSectionBreak();
        billTable.addRow("", "", "", "@align:right@Sub Total :", String.format("%.2f", total));
        billTable.mergeCells(billTable.getNumberOfRows()-1, 3, -3);
        billTable.addSectionBreak();

        while(true){
            System.out.println(String.format("Total : %.2f", total));
            discounts:
            while(true){
                System.out.print("Discounts/Taxes?(y/n)");
                System.out.flush();
                String choice = keyboard.readLine();
                switch(choice){
                    case "y":
                        discountAmount:
                        while(true){
                            System.out.print("Discount/tax rate(discount rate < 0 && tax rate > 0):");
                            System.out.flush();
                            String rate = keyboard.readLine();
                            if(rate.matches("(-)?[01](\\.[\\d])?")){
                                while(true) {
                                    float rateAsFloat = Float.parseFloat(rate);
                                    float discountedTotal = discount(total, rateAsFloat);
                                    System.out.print(String.format("Current total is %.2f. Discounted/taxed total will be %.2f. Confirm?(y)es | (n)o | (c)ancel)", total, discountedTotal));
                                    System.out.flush();
                                    String confirmDiscount = keyboard.readLine();
                                    switch (confirmDiscount) {
                                        case "y":
                                            total = discountedTotal;

                                            //Discount rate added to the billTable
                                            billTable.addRow("", "", "", "@align:right@Discount Rate :", String.format("%.2f%%", (rateAsFloat * 100)));
                                            billTable.mergeCells(billTable.getNumberOfRows()-1, 3, -3);
                                            billTable.addSectionBreak();

                                            //Discounted total added to the billTable
                                            billTable.addRow("", "", "", "@align:right@Net Total :", String.format("%.2f", discountedTotal));
                                            billTable.mergeCells(billTable.getNumberOfRows()-1, 3, -3);
                                            billTable.addSectionBreak();
                                            break discounts;

                                        case "n":
                                            continue discountAmount;

                                        case "c":
                                            break discounts;

                                        default:
                                            System.out.println("Invalid choice! Retry...");
                                    }
                                }
                            }
                            else
                                System.out.println("Invalid discount rate! Try again!");
                        }
                    case "n":
                        break discounts;

                    default:
                        System.out.println("Invalid choice.");
                }
            }
            float cash, change;

            while(true){
                System.out.print("Cash?");
                System.out.flush();
                String cashInput = keyboard.readLine();
                if(!cashInput.matches("[\\d]+(\\.[\\d]{1,2})?")){
                    System.out.println("Invalid input! Check your input...");
                    continue;
                }
                cash = Float.parseFloat(cashInput);
                if(cash < total){
                    System.out.println("Insufficient funds! You need to pay "+total+"LKR in cash!");
                    continue;
                }
                change = cash - total;
                break;
            }

            //Cash Paid added to the billTable
            billTable.addRow("", "", "", "@align:right@Cash :", String.format("%.2f", cash));
            billTable.mergeCells(billTable.getNumberOfRows()-1, 3, -3);
            billTable.addSectionBreak();

            //Change amount added to the bill table
            billTable.addRow("", "", "", "@align:right@Change :", String.format("%.2f", change));
            billTable.mergeCells(billTable.getNumberOfRows()-1, 3, -3);
            billTable.addSectionBreak();

            countChange(change);
            bill.append("\n").append(billTable.tableToString());
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

    /**
     * Calculates the discount/taxation amount for a given total, at a given rate.
     * @param amount Amount that the discount/taxation should be accounted as a float value
     * @param rate Rate that the discount/taxation should be calculated at, as a float value within the range -1.0 and +1.0 inclusive.
     * @return the discounted amount as a float if the discount rate is valid, initial amount otherwise.
     */
    public float discount(float amount, float rate){
        if(rate < -1.0f || rate > 1.0f){
            System.out.println("Invalid discount rate! Should be between -1.0 and +1.0");
            return amount;
        }
        amount += (amount * rate);
        return amount;
    }
}

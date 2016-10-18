package com.foodcity;

/**
 * Created by hsenid on 10/18/16.
 */
public class InsufficientQuantityException extends Exception {
    @Override
    public String getMessage() {
        return "The requested qty is not available in the stock.";
    }
}

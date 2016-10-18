package com.foodcity;

/**
 * Created by hsenid on 10/18/16.
 */
public class ZeroStockLevelException extends Exception{
    @Override
    public String getMessage(){
        return "The requested item is out of stock!";
    }
}

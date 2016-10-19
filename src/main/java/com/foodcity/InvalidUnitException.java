package com.foodcity;

/**
 * Created by hsenid on 10/19/16.
 */
public class InvalidUnitException extends Exception {
    private String message;

    public InvalidUnitException(float qty, float unit, String unitname){
        this.message = String.format("%f%s cannot be bought. Minimum amount available is %f%s!", qty, unitname, unit, unitname);
    }

    public InvalidUnitException(){
        this.message = "Invalid unit!";
    }

    public String getMessage(){
        return this.message;
    }
}

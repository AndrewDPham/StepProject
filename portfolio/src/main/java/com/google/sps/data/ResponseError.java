package com.google.sps.data;

/**
 * The ResponseError class conatins the error for NumberFormatException
 */
public class ResponseError{
    
    private String description;
    private int errorCode;

    public ResponseError(String description, int errorCode){
        this.description = description;
        this.errorCode = errorCode;
    }
    
    public String getDescription(){
        return this.description;
    }

    public int getErrorCode(){
        return this.errorCode;
    }

}

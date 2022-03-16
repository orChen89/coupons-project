package com.or.errors;

public class UserValidationException extends ApplicationException{

    public UserValidationException(){
        super("Invalid input - Please enter according to the correct format");
    }
}

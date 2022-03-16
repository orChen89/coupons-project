package com.or.errors;

public class ApplicationException extends Exception {

    //This is the "father" exception that thrown - All other exceptions inherit from it
    public ApplicationException(final String msg) {
        super(msg);
    }
}

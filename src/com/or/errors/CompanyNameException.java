package com.or.errors;

public class CompanyNameException extends ApplicationException {

    public CompanyNameException() {
        super("Can't update a company's name!");
    }
}

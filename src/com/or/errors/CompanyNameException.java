package com.or.errors;

public class CompanyNameException extends ApplicationException {

    public CompanyNameException() {

        super("This Company's name is already exists! Unable to update a company name!");
    }
}

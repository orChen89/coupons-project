package com.or.errors;

import com.or.enums.DBType;

public class DBError extends Error {

    //This is an error that thrown regarding db operations
    public DBError(final DBType dbType){
        super("Failed to perform Data-Base operation: " + "Failed to " +  dbType);
    }
}

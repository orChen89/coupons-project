package com.or.errors;

import com.or.enums.EntityType;

public class EntityExistException extends ApplicationException {

    public EntityExistException(final EntityType entityType){

        super("This " + entityType + " is already exists!");
    }
}

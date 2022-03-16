package com.or.errors;

import com.or.enums.EntityType;

public class EntityNotExistException extends ApplicationException {

    public EntityNotExistException(final EntityType entityType){

        super("This " + entityType + " is not exists!");
    }

}

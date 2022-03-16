package com.or.errors;

import com.or.enums.CrudOperation;
import com.or.enums.EntityType;

public class CrudException extends ApplicationException{

    public CrudException(final EntityType entityType, final CrudOperation operation){
        super("Failed to " + operation + " " + entityType);
    }
}

package com.or.errors;

import com.or.enums.EntityType;

public class PurchaseNotExistingException extends ApplicationException {

    public PurchaseNotExistingException(final EntityType entityType) {
        super("This purchase of specific: " + entityType + " is not exists!");
    }
}

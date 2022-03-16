package com.or.errors;

import com.or.enums.EntityType;

public class LoginException extends ApplicationException {

    public LoginException(final EntityType entityType) {

        super("Failed to perform login operation: " + entityType + " Failed to login");
    }
}

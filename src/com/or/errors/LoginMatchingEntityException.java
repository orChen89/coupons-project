package com.or.errors;

import com.or.enums.ClientType;

public class LoginMatchingEntityException extends ApplicationException {

    public LoginMatchingEntityException(final String email, final ClientType clientType) {
        super("Failed to authenticate: " + " The current " + clientType + "'s " + email +
                " is not matching to entity's login credentials! ");
    }
}

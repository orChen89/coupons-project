package com.or.errors;

import com.or.enums.ClientType;

public class AuthenticationException extends ApplicationException {

    public AuthenticationException(final String email, final ClientType clientType) {
        super("Failed to authenticate: " + clientType + " by email: "  + email);
    }
}

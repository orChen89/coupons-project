package com.or.facade;

import com.or.errors.ApplicationException;

public abstract class ClientFacade  {

    public abstract boolean login(String email, String password) throws ApplicationException;
}



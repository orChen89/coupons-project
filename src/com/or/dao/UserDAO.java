package com.or.dao;

import com.or.errors.CrudException;

public abstract class UserDAO <ID, ENTITY> implements CrudDAO<ID, ENTITY> {

    public abstract ENTITY readByEmail(final String email) throws CrudException;

    public abstract ENTITY readById(final Long id) throws CrudException;

    public abstract ENTITY readByName(final String name) throws CrudException;

    //Connecting between methods in Company & Customer DAOs
    //Returning a boolean if Entity exists by email address
    public boolean isExistsByEmail(final String email) throws CrudException {
        return readByEmail(email) != null;
    }

    //Connecting between methods in Company & Customer DAOs
    //Returning a boolean if Entity exists by Id
    public boolean isExistsById(final Long id) throws CrudException {
        return readById(id) != null;
    }

    //Connecting between methods in Company & Customer DAOs
    //Returning a boolean if Entity exists by name
    public boolean isExistsByName(final String name) throws CrudException {
        return readByName(name) != null;
    }
}

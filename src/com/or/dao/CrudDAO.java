package com.or.dao;

import com.or.errors.CrudException;
import java.util.List;

public interface CrudDAO <ID, Entity> {

        ID create(final Entity entity) throws CrudException;
        Entity read(final ID id) throws CrudException;
        void update(final Entity entity) throws CrudException;
        void delete(final ID id) throws CrudException;
        List<Entity> readAll() throws CrudException;
}


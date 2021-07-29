package dao;

import java.util.List;

import dao.exceptions.DaoAccessException;
import dao.exceptions.DaoInternalServerException;
import dao.exceptions.DaoUnauthorizedException;

/**
 * Data access object interface.
 * specifies standard method signatures to interact
 * with data model classes.
 * @param <T> model class to interact with.
 */
public interface Dao<T> {
    /**
     * Gets a single instance of the model.
     * @param id the id of the tuple.
     * @return an object of T hydrated with data.
     */
    T get(int id) throws DaoInternalServerException, DaoUnauthorizedException;

    /**
     * Gets all the instances of the model in the db.
     * @return a List of all T in the db.
     */
    List<T> getAll() throws DaoAccessException;;

    /**
     * Saves a passed T to the db.
     * @param t the object to be saved.
     */
    void save(T t) throws DaoAccessException;

    /**
     * Updates an existing object.
     * @param id the id of the existing object to edit.
     * @param updated the new object to attributes with.
     */
    void update(long id, T updated) throws DaoAccessException;;

    /**
     * Deletes an object from the database.
     * @param t the object to be deleted.
     */
    void delete(T t) throws DaoAccessException;
}

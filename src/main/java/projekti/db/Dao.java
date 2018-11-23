/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti.db;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * Data Access Object interface for accessing data.
 *
 * @author mila
 */
public interface Dao<T, K> {

    /**
     * Fetch one object from the database based on a certain key.
     *
     * @param key the given key
     *
     * @return object.
     */

    T findOne(K key) throws SQLException;

    /**
     * Fetch all objects of a certain type from the database.
     *
     *
     * @return list of objects.
     */

    List<T> findAll() throws SQLException;

    /**
     * Add a new object to the database.
     *
     * @param object the object that is added to the database
     *
     * @return new object.
     */

    T create(T object) throws SQLException;
    
    /**
     * Update object in the database
     * 
     * @param object the object that is updated
     * @return the updated object
     * @throws SQLException if problems ensue, object is not in db etc.
     */
    T update(T object) throws SQLException;

    void delete(K key) throws SQLException;

}

package ro.ubb.springjpa.service;

import ro.ubb.springjpa.domain.model.Client;
import ro.ubb.springjpa.domain.validators.BookstoreException;
import ro.ubb.springjpa.domain.validators.ValidatorException;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

public interface ClientService
{
    /**
     * Add a client to the client repository.
     * @param client instance of class Client
     * @throws ValidatorException if the entity is not valid
     */
    public void addClient(Client client) throws ValidatorException, BookstoreException, SQLException;

    /**
     * Get client list set.
     * @return the set containing all the clients inside the client repository
     */
    public Set<Client> getClientList() throws BookstoreException;

    /**
     * Delete a client from the client repository
     * @param id integer representing the id of a client
     */
    public void deleteClient(Integer id) throws BookstoreException;

    /**
     * Update a client from the client repository
     * @param client instance of the class Client
     * @throws ValidatorException if the client is not valid
     */
    public void updateClient(Client client) throws ValidatorException, BookstoreException;

    /**
     * Returns all the clients that contain the searchString in one or more of their attributes.
     * @param searchString string used for filtering the clients
     * @return a HashSet containing clients
     */
    public Set<Client> filter(String searchString) throws BookstoreException;

    public Optional<Client> findOne(Integer id) throws BookstoreException;
}

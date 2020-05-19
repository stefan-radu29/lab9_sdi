package ro.ubb.springjpa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.springjpa.domain.model.Client;
import ro.ubb.springjpa.domain.validators.BookstoreException;
import ro.ubb.springjpa.domain.validators.ClientValidator;
import ro.ubb.springjpa.domain.validators.ValidatorException;
import ro.ubb.springjpa.repository.ClientRepository;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ClientServiceImpl implements ClientService {

    public static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientValidator clientValidator;


    /**
     * Add a client to the client repository.
     * @param client instance of class Client
     * @throws ValidatorException if the entity is not valid
     */
    public void addClient(Client client) throws ValidatorException, BookstoreException, SQLException {
        log.trace("addClient - method entered: client = {}", client);
        clientValidator.validate(client);
        this.clientRepository.save(client);
        log.trace("addClient - method finished");
    }

    /**
     * Get client list set.
     * @return the set containing all the clients inside the client repository
     */
    public Set<Client> getClientList() throws BookstoreException {
        log.trace("getClientList - method entered");
        Iterable<Client> clientList = this.clientRepository.findAll();
        Set<Client> clientSet = StreamSupport.stream(clientList.spliterator(), false).collect(Collectors.toSet());
        log.trace("getClientList - method finished: clientSet={}", clientSet);
        return clientSet;
    }

    /**
     * Delete a client from the client repository
     * @param id integer representing the id of a client
     */
    public void deleteClient(Integer id) throws BookstoreException {
        log.trace("deleteClient - method entered: id={}", id);
        this.clientRepository.deleteById(id);
        log.trace("deleteClient - method finished");
    }

    /**
     * Update a client from the client repository
     * @param client instance of the class Client
     * @throws ValidatorException if the client is not valid
     */
    @Transactional
    public void updateClient(Client client) throws ValidatorException, BookstoreException {
        log.trace("updateClient - method entered: client={}", client);
        clientValidator.validate(client);
        clientRepository.findById(client.getId())
                .ifPresent(c -> {
                    c.setFirstName(client.getFirstName());
                    c.setLastName(client.getLastName());
                    c.setAddress(client.getAddress());
                    log.debug("updateClient - updated: c={}", c);
                });
        log.trace("updateClient - method finished");
    }

    /**
     * Returns all the clients that contain the searchString in one or more of their attributes.
     * @param searchString string used for filtering the clients
     * @return a HashSet containing clients
     */
    public Set<Client> filter(String searchString) throws BookstoreException {
        log.trace("filter (Client) - method entered: searchString = {}", searchString);
        Set<Client> clients = this.getClientList();
        Set<Client> filteredClients = new HashSet<Client>();
        clients.forEach(client ->
        {
            if(client.getAddress().contains(searchString) || client.getLastName().contains(searchString) || client.getFirstName().contains(searchString))
            {
                filteredClients.add(client);
            }
        });
        log.trace("filter (Client) - method finished: filteredClients={}", filteredClients);
        return filteredClients;
    }

    public Optional<Client> findOne(Integer id) throws BookstoreException {
        return this.clientRepository.findById(id);
    }
}

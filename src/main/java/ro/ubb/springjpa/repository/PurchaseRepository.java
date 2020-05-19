package ro.ubb.springjpa.repository;

import ro.ubb.springjpa.domain.model.Purchase;

public interface PurchaseRepository extends DatabaseRepository<Purchase, Integer> {
    void deleteByClientId(int clientId);
    void deleteByBookId(int bookId);
    Long countByBookId(int bookId);
}

package TicketMarket.demo.DAO;

import TicketMarket.demo.Entity.Ticket;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class TicketRepositoryImpl implements TicketCustomRepository{
    private EntityManager entityManager ;
    @Autowired
    public TicketRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override

    public List<Ticket> ticketsByEventId(int eventId) {
        TypedQuery<Ticket> qur = entityManager.createQuery("FROM Ticket WHERE event_id = :eventId" , Ticket.class);
        qur.setParameter("eventId", eventId);
        return qur.getResultList();
    }
    @Override
    public List<Ticket> availableTicketsByEventId(int eventId) {
        TypedQuery<Ticket> qur = entityManager.createQuery("FROM Ticket t WHERE t.event_id = :eventId AND t.for_sale = true ORDER BY t.price"  , Ticket.class);
        qur.setParameter("eventId", eventId);
        return qur.getResultList();
    }
 // ==============
@Override
public boolean verifyTicket(int eventId, String serialKey) {
    TypedQuery<Long> query = entityManager.createQuery(
        "SELECT COUNT(t) FROM Ticket t WHERE t.serial_key = :serialKey AND t.event_id = :eventId", Long.class
    );
    query.setParameter("serialKey", serialKey);
    query.setParameter("eventId", eventId);
    Long count = query.getSingleResult();
    return count > 0; // Returns true if the serial_key exists and matches the eventId, false otherwise
}
     // ==============

    
    @Override
    public List<Ticket> findBySellerId(int seller_id) {
        TypedQuery<Ticket> qur = entityManager.createQuery("FROM Ticket WHERE seller_id = :seller_id" , Ticket.class);
        qur.setParameter("seller_id", seller_id);
        return qur.getResultList();
    }
    @Override
    public boolean generatedByUsTicket(String serialKey) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(t) FROM Ticket t WHERE t.serial_key = :serialKey AND generated_by_us = TRUE", Long.class
        );
        query.setParameter("serialKey", serialKey);
        Long count = query.getSingleResult();
        return count == 0; // Returns true if the serial_key exists, false otherwise
    }
    
    @Override
    public Ticket findBySerialKey(String serialKey , int event_id) {
    TypedQuery<Ticket> query = entityManager.createQuery(
        "SELECT t FROM Ticket t WHERE t.serial_key = :serialKey AND t.event_id = :event_id", Ticket.class
    );
    query.setParameter("serialKey", serialKey);
    query.setParameter("event_id", event_id);

    return query.getSingleResult(); // Returns the ticket with the matching serialKey
}


}

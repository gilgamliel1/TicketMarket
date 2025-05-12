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
        TypedQuery<Ticket> qur = entityManager.createQuery("FROM Ticket WHERE event_id = :eventId AND status= 'available' ORDER BY price" , Ticket.class);
        qur.setParameter("eventId", eventId);
        return qur.getResultList();
    }

    @Override
    public boolean verifyTicket(String serialKey) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(t) FROM Ticket t WHERE t.serial_key = :serialKey", Long.class
        );
        query.setParameter("serialKey", serialKey);
        Long count = query.getSingleResult();
        return count == 0; // Returns true if the serial_key exists, false otherwise
    }
}

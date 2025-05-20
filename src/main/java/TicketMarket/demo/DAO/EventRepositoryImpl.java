package TicketMarket.demo.DAO;

import TicketMarket.demo.Entity.Event;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventRepositoryImpl implements EventCustomRepository {
    private EntityManager entityManager;

    @Autowired
    public EventRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public boolean isEventExist(String eventName) {
        TypedQuery<Event> qur = entityManager.createQuery(
            "FROM Event WHERE event_name = :eventName AND event_date > CURRENT_TIMESTAMP", Event.class
        );
        qur.setParameter("eventName", eventName);
        return qur.getResultList().size() >= 1;
    }

    @Override
    public List<Event> findAllEvents() {
        TypedQuery<Event> qur = entityManager.createQuery(
            "FROM Event WHERE event_date > CURRENT_TIMESTAMP ORDER BY event_date", Event.class
        );
        return qur.getResultList();
    }

    public List<Event> findByTag(String tag) {
        TypedQuery<Event> qur = entityManager.createQuery(
            "FROM Event WHERE tag = :tag AND event_date > CURRENT_TIMESTAMP ORDER BY event_date", Event.class
        );
        qur.setParameter("tag", tag);
        return qur.getResultList();
    }

    @Override
    public int amountOfAvilableTickets(int eventId) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(t) FROM Ticket t JOIN Event e ON t.event_id = e.event_id WHERE t.for_sale = TRUE AND t.event_id = :eventId AND e.event_date > CURRENT_TIMESTAMP", Long.class
        );
        query.setParameter("eventId", eventId);
        return query.getSingleResult().intValue();
    }

    @Override
    public int amountOfSoldTickets(int eventId) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(t) FROM Ticket t JOIN Event e ON t.event_id = e.event_id WHERE t.sold = TRUE AND t.for_sale = FALSE AND t.event_id = :eventId AND e.event_date > CURRENT_TIMESTAMP", Long.class
        );
        query.setParameter("eventId", eventId);
        return query.getSingleResult().intValue();
    }

    @Override
    public int amountOfLookingForTickets(int eventId) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(t) FROM Ticket t JOIN Event e ON t.event_id = e.event_id WHERE t.event_id = :eventId AND e.event_date > CURRENT_TIMESTAMP", Long.class
        );
        query.setParameter("eventId", eventId);
        return query.getSingleResult().intValue();
    }
}
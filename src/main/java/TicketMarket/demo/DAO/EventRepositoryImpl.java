package TicketMarket.demo.DAO;

import TicketMarket.demo.Entity.Event;
import TicketMarket.demo.Entity.Ticket;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventRepositoryImpl implements EventCustomRepository{
    private EntityManager entityManager ;
    @Autowired
    public EventRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
   public boolean isEventExist(String eventName){
    TypedQuery<Event> qur = entityManager.createQuery("FROM Event WHERE event_name =:eventName" , Event.class);
    qur.setParameter("eventName" , eventName);
    return qur.getResultList().size()>=1 ;
    }
    @Override
    public List<Event> findAllEvents(){
        TypedQuery<Event> qur = entityManager.createQuery("FROM Event ORDER BY event_date" , Event.class);
        return qur.getResultList();
    }
    public List<Event> findByTag(String tag){
        TypedQuery<Event> qur = entityManager.createQuery("FROM Event WHERE tag=:tag ORDER BY event_date" , Event.class);
        qur.setParameter("tag" , tag);
        return qur.getResultList();
    }
    @Override
    public int amountOfAvilableTickets(int eventId){
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'available' AND t.event_id = :eventId", Long.class);
        query.setParameter("eventId", eventId);
        return query.getSingleResult().intValue();
    }
    @Override
    public int amountOfSoldTickets(int eventId){
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'sold' AND t.event_id = :eventId", Long.class);
        query.setParameter("eventId", eventId);
        return query.getSingleResult().intValue();
    }
    @Override
    public int amountOfLookingForTickets(int eventId){
        return 0 ;
    }
}

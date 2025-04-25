package TicketMarket.demo.DAO;

import TicketMarket.demo.Entity.Event;

import java.util.List;

public interface EventCustomRepository {
    boolean isEventExist(String eventName);
    List<Event> findAllEvents();
    List<Event> findByTag(String tag);
    int amountOfAvilableTickets(int eventId);
    int amountOfSoldTickets(int eventId);
    int amountOfLookingForTickets(int eventId);
}

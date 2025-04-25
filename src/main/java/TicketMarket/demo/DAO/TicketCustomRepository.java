package TicketMarket.demo.DAO;

import TicketMarket.demo.Entity.Ticket;

import java.util.List;

public interface TicketCustomRepository {

    List<Ticket> ticketsByEventId(int eventId);
    List<Ticket> availableTicketsByEventId(int eventId);

}

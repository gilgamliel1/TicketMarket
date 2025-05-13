package TicketMarket.demo.DAO;

import TicketMarket.demo.Entity.Ticket;

import java.util.List;

public interface TicketCustomRepository {

    List<Ticket> ticketsByEventId(int eventId);
    List<Ticket> availableTicketsByEventId(int eventId);
    boolean verifyTicket(String serialKey); // Add this method
    boolean generatedByUsTicket(String serialKey); 

    List<Ticket> findBySellerId(int sellerId); // Automatically implemented by Spring Data JPA



}

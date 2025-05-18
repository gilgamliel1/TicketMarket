package TicketMarket.demo.DAO;

import TicketMarket.demo.Entity.Ticket;

import java.util.List;

public interface TicketCustomRepository {

    List<Ticket> ticketsByEventId(int eventId);
    List<Ticket> availableTicketsByEventId(int eventId);
    boolean verifyTicket(int eventId , String serialKey); // Add this method
    boolean isSerialKeyAlredayExists (String serialKey);
    boolean generatedByUsTicket(String serialKey); 
    Ticket findBySerialKey(String serialKey, int event_id); // Automatically implemented by Spring Data JPA
    List<Ticket> findBySellerId(int sellerId); // Automatically implemented by Spring Data JPA
    List<Ticket> findTicketsBySellerIdAndEventId(int seller_id , int event_id);
    boolean isTicketAlredayForSale(String serialKey,int eventId ); // Add this method


}

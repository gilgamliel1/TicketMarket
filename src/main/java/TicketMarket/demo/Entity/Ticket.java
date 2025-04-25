package TicketMarket.demo.Entity;

import TicketMarket.demo.DAO.EventRepository;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    private static EventRepository eventRepository;
    @Id
    @Column(name = "ticket_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticket_id ;
    @Column(name = "event_id")
    private int event_id ;
    @Column(name = "seller_id")
    private int seller_id ;
    @Column(name = "price")
    private int price ;
    @Column(name = "description")
    private String desc ;
    @Column(name = "status")
    private String status ;
    @Column(name = "created_at")
    private LocalDateTime localDateTime ;
    @Column(name = "serial_key")
    private String serial_key ;


    public Ticket() {
        this.ticket_id = -1;
        this.event_id = -1;
        this.seller_id = -1;
        this.price = -1;
        this.desc = "None";
        this.status = "None";
        this.localDateTime = LocalDateTime.now();
        this.serial_key = "None";
    }

    public Ticket(int event_id, int seller_id, int price, String desc, String status , String serial_key ) {
        this.ticket_id = 50 ;
        this.event_id = event_id;
        this.seller_id = seller_id;
        this.price = price;
        this.desc = desc;
        this.status = status;
        this.localDateTime = LocalDateTime.now();
        this.serial_key=serial_key;
    }

    public int getTicket_id() {
        return ticket_id;
    }
    public String getSerialKey() {
        return serial_key;
    }
    public void setSerialKey(String serialKey){
        this.serial_key = serialKey;
    }

    public void setTicket_id(int ticket_id) {
        this.ticket_id = ticket_id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public int getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(int seller_id) {
        this.seller_id = seller_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticket_id=" + ticket_id +
                ", event_id=" + event_id +
                ", seller_id=" + seller_id +
                ", price=" + price +
                ", desc='" + desc + '\'' +
                ", status='" + status + '\'' +
                ", localDateTime=" + localDateTime +
                '}';
    }
}

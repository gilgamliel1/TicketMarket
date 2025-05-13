package TicketMarket.demo.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @Column(name = "ticket_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticket_id;

    @Column(name = "event_id")
    private int event_id;

    @Column(name = "seller_id")
    private int seller_id;

    @Column(name = "price")
    private int price;

    @Column(name = "description")
    private String desc;

    @Column(name = "created_at")
    private LocalDateTime localDateTime;

    @Column(name = "serial_key", unique = true)
    private String serial_key;

    @Column(name = "for_sale")
    private boolean for_sale; // New field added
    
    @Column(name = "sold")
    private boolean sold; // New field added


    @Column(name = "generated_by_us")
    private boolean generated_by_us; // New field added

    public Ticket() {
        this.ticket_id = -1;
        this.event_id = -1;
        this.seller_id = -1;
        this.price = -1;
        this.desc = "None";
        this.localDateTime = LocalDateTime.now();
        this.serial_key = "None";
        this.for_sale = false; // Default value
        this.sold = false; // Default value
        this.generated_by_us = false; // Default value
    }

    public Ticket(int event_id, int seller_id, int price, String desc, String serial_key, boolean for_sale, boolean generated_by_us) {
        this.ticket_id = 50;
        this.event_id = event_id;
        this.seller_id = seller_id;
        this.price = price;
        this.desc = desc;
        this.sold = false;
        this.localDateTime = LocalDateTime.now();
        this.serial_key = serial_key;
        this.for_sale = for_sale;
        this.generated_by_us = generated_by_us;
    }

    public int getTicket_id() {
        return this.ticket_id;
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

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getSerialKey() {
        return this.serial_key;
    }

    public void setSerialKey(String serialKey) {
        this.serial_key = serialKey;
    }

    public boolean isFor_sale() {
        return for_sale;
    }

    public void setFor_sale(boolean for_sale) {
        this.for_sale = for_sale;
    }

    public boolean is_sold() {
        return sold;
    }

    public void set_is_sold(boolean for_sale) {
        this.sold = for_sale;
    }
    public boolean isGenerated_by_us() {
        return generated_by_us;
    }

    public void setGenerated_by_us(boolean generated_by_us) {
        this.generated_by_us = generated_by_us;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticket_id=" + ticket_id +
                ", event_id=" + event_id +
                ", seller_id=" + seller_id +
                ", price=" + price +
                ", desc='" + desc + '\'' +
                ", localDateTime=" + localDateTime +
                ", serial_key='" + serial_key + '\'' +
                ", for_sale=" + for_sale +
                ", generated_by_us=" + generated_by_us +
                '}';
    }
}
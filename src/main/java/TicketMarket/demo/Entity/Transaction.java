package TicketMarket.demo.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(name = "transaction_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transaction_id ;

    @Column(name = "ticket_id")
    private int ticket_id ;

    @Column(name = "buyer_id")
    private int buyer_id ;

    @Column(name = "seller_id")
    private int seller_id ;

    @Column(name = "transaction_date")
    private LocalDateTime transaction_date;

    @Column(name = "price")
    private int ticket_price ;

    public Transaction() {
        this.ticket_id = -1;
        this.buyer_id = -1;
        this.seller_id = -1;
        this.transaction_date = LocalDateTime.now();
        this.ticket_price = -1;
    }

    public Transaction( int ticket_id , int buyer_id, int seller_id, LocalDateTime transaction_date, int ticket_price) {
        this.ticket_id = ticket_id ;
        this.buyer_id = buyer_id;
        this.seller_id = seller_id;
        this.transaction_date = transaction_date;
        this.ticket_price = ticket_price;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(int ticket_id) {
        this.ticket_id = ticket_id;
    }

    public int getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(int buyer_id) {
        this.buyer_id = buyer_id;
    }

    public int getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(int seller_id) {
        this.seller_id = seller_id;
    }

    public LocalDateTime getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(LocalDateTime transaction_date) {
        this.transaction_date = transaction_date;
    }

    public int getTicket_price() {
        return ticket_price;
    }

    public void setTicket_price(int ticket_price) {
        this.ticket_price = ticket_price;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transaction_id=" + transaction_id +
                ", ticket_id=" + ticket_id +
                ", buyer_id=" + buyer_id +
                ", seller_id=" + seller_id +
                ", transaction_date=" + transaction_date +
                ", ticket_price=" + ticket_price +
                '}';
    }
}

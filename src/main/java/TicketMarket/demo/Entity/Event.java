package TicketMarket.demo.Entity;

import jakarta.persistence.*;

import java.sql.Time;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int event_id ;

    @Column(name = "event_name")
    private String event_name ;

    @Column(name = "event_date")
    private LocalDateTime event_date;

    @Column(name="event_loc")
    private String eventLoc ;

    @Column(name="event_desc")
    private String event_desc ;

    @Column(name = "event_owner")
    private String event_owner ;

    @Column(name = "event_created_at")
    private LocalDateTime event_created_at ;

    @Column(name = "tag")
    private String tag ;

    public Event() {
        this.event_id = 0;
        this.event_name = "NoEvent";
        this.event_date = LocalDateTime.now();
        this.eventLoc = "NoLoc";
        this.event_desc = null;
        this.event_owner = "NoOne";
        this.event_created_at = LocalDateTime.now();
        this.tag = "Other";
    }

    public Event(String event_name, LocalDateTime event_date, String eventLoc, String event_desc, String event_owner , String tag) {
        this.event_name = event_name;
        this.event_date = event_date;
        this.eventLoc = eventLoc;
        this.event_desc = event_desc;
        this.event_owner = event_owner;
        this.event_created_at = LocalDateTime.now();
        this.tag = tag ;
    }

    @Override
    public String toString() {
        return "Event{" +
                "event_id=" + event_id +
                ", event_name='" + event_name + '\'' +
                ", event_date=" + event_date +
                ", eventLoc='" + eventLoc + '\'' +
                ", event_desc='" + event_desc + '\'' +
                ", event_owner='" + event_owner + '\'' +
                ", event_created_at=" + event_created_at +
                '}';
    }

    public int getEvent_id() {
        return event_id;
    }
    public String getTag(){return this.tag;}
    public void setTag(String tag){this.tag = tag;}

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public LocalDateTime getEvent_date() {
        return event_date;
    }

    public void setEvent_date(LocalDateTime event_date) {
        this.event_date = event_date;
    }

    public String getEventLoc() {
        return eventLoc;
    }

    public void setEventLoc(String eventLoc) {
        this.eventLoc = eventLoc;
    }

    public String getEvent_desc() {
        return event_desc;
    }

    public void setEvent_desc(String event_desc) {
        this.event_desc = event_desc;
    }

    public String getEvent_owner() {
        return event_owner;
    }

    public void setEvent_owner(String event_owner) {
        this.event_owner = event_owner;
    }

    public LocalDateTime getEvent_created_at() {
        return event_created_at;
    }

    public void setEvent_created_at(LocalDateTime event_created_at) {
        this.event_created_at = event_created_at;
    }
}

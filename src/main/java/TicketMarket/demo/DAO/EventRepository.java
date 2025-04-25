package TicketMarket.demo.DAO;

import TicketMarket.demo.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventRepository extends JpaRepository<Event, Integer> , EventCustomRepository {
}

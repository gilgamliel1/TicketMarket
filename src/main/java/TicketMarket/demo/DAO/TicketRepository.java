package TicketMarket.demo.DAO;

import TicketMarket.demo.Entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends TicketCustomRepository , JpaRepository<Ticket , Integer> {
}

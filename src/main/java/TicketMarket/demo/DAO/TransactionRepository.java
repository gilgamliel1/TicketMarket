package TicketMarket.demo.DAO;

import TicketMarket.demo.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends TransactionCustomRepository , JpaRepository<Transaction , Integer> {
}

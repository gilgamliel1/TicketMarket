package TicketMarket.demo.DAO;

import TicketMarket.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User , Integer> , UserCustomRepository {
}

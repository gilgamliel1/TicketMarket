package TicketMarket.demo.DAO;

import TicketMarket.demo.Entity.User;

public interface UserCustomRepository {
    boolean isUsernameExist(String userName);
    boolean isUserIdExist (String id);
    boolean isEmailExist(String email);
    boolean isUsernameAndPasswordOK(String username , String password);
    User findByUsername(String username);
}

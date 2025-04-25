package TicketMarket.demo.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import TicketMarket.demo.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserCustomRepository{
    private EntityManager entityManager ;
    @Autowired
    public UserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User findByUsername(String username){
        TypedQuery<User> qur = entityManager.createQuery("FROM User WHERE user_name =:username" , User.class);
        qur.setParameter("username" , username);
        List<User> lis = qur.getResultList();
        if (lis.size() > 1) return null;
        else return lis.get(0);
    }

    @Override
    public boolean isUsernameAndPasswordOK(String username , String password){
        TypedQuery<User> qur = entityManager.createQuery("FROM User WHERE user_name =:username AND password_hash=:password" , User.class);
        qur.setParameter("username", username).setParameter("password" , password);
        return qur.getResultList().size() >= 1;
    }
    @Override
    public boolean isUsernameExist(String userName) {
        TypedQuery<User> qur = entityManager.createQuery("FROM User WHERE user_name = :userName", User.class);
        qur.setParameter("userName" , userName);
        return qur.getResultList().size() >= 1 ;
    }
    @Override
    public boolean isUserIdExist (int id){
        TypedQuery<User> qur = entityManager.createQuery("FROM User WHERE user_id_number=:id", User.class);
        qur.setParameter("id" , id);
        return qur.getResultList().size() >= 1 ;
    }

    @Override
    public boolean isEmailExist(String email){
        TypedQuery<User> qur = entityManager.createQuery("FROM User WHERE email =:email" , User.class);
        qur.setParameter("email" , email);
        return qur.getResultList().size() >= 1 ;
    }
}

package TicketMarket.demo.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name ="users",uniqueConstraints = @UniqueConstraint(columnNames ={ "user_id","user_name" }))
public class User {
    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id ;
    @Column(name = "user_name")
    private String user_name ="NoName";
    @Column(name = "user_first_name")
    private String user_first_name="NoName" ;
    @Column(name = "user_last_name")
    private String user_last_name ="NoName";
    @Column(name = "user_id_number")
    private int user_id_number ;
    @Column(name = "email")
    private String email="NoName" ;
    @Column(name = "password_hash")
    private String password_hash="NoName" ;
    @Column(name = "profile_picture_url" , length = 255)
    private String profile_picture_url ="NoName";
    @Column(name = "bio" , length = 100)
    private String bio ="NoName";
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "wallet_balance")
    private int balance ;

    public User(String user_name_ , String first_name_ , String last_name_ , int user_id_number_ , String email_ , String password_hash_ , String profile_picture_url_
    , String bio_){
        this.user_name = user_name_;
        this.user_first_name = first_name_;
        this.user_last_name = last_name_;
        this.user_id_number = user_id_number_;
        this.email = email_;
        this.password_hash = password_hash_;
        this.profile_picture_url = profile_picture_url_;
        this.bio = bio_;
        this.balance = 0 ;
    }
    public User(){
        this.user_name = null ;
        this.user_first_name = null ;
        this.user_last_name = null ;
        this.user_id_number = -1;
        this.email = null ;
        this.password_hash = null ;
        this.profile_picture_url = null;
        this.bio = null ;
        this.balance = -1 ;
    }
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getFirst_name() {
        return user_first_name;
    }

    public void setFirst_name(String first_name) {
        this.user_first_name = first_name;
    }

    public String getLast_name() {
        return user_last_name;
    }

    public void setLast_name(String last_name) {
        this.user_last_name = last_name;
    }

    public int getUser_id_number() {
        return user_id_number;
    }

    public void setUser_id_number(int user_id_number) {
        this.user_id_number = user_id_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getProfile_picture_url() {
        return profile_picture_url;
    }

    public void setProfile_picture_url(String profile_picture_url) {
        this.profile_picture_url = profile_picture_url;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "user_id:" + user_id +
                ", user_name=" + user_name +
                ", first_name=" + user_first_name +
                ", last_name=" + user_last_name +
                ", user_id_number=" + user_id_number +
                ", email=" + email +
                ", password_hash=" + password_hash +
                ", profile_picture_url=" + profile_picture_url +
                ", bio=" + bio +
                ", createdAt=" + createdAt +
                ", balance=" + balance +
                '}';
    }
}

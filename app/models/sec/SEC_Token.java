package models.sec;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table( name = "session_token" )
public class SEC_Token {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "id" )
    private int id;

    @OneToOne
    @JoinColumn( name = "user_id" )
    private SEC_User user;

    @Column( name = "token" )
    private String token;

    @Column( name = "created")
    @Temporal( TemporalType.TIMESTAMP )
    @CreationTimestamp
    private Date created;




    public SEC_Token(){}

    public SEC_Token( SEC_User user ) {
        this.user = user;
        this.token = createToken();
    }

    public SEC_Token( SEC_User user, String token ) {
        this.user = user;
        this.token = token;
    }

    private String createToken() {
       return UUID.randomUUID().toString();
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SEC_User getUser() {
        return user;
    }

    public void setUser( SEC_User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

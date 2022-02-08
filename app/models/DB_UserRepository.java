package models;

import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.concurrent.CompletionStage;

@ImplementedBy( DB_JPAUserRepository.class )
public interface DB_UserRepository {
    CompletionStage<SEC_User> findByToken( String token );

    CompletionStage<SEC_User> get( int userId );

    CompletionStage<SEC_User> findByUserName( String userName );

    CompletionStage<Boolean> userExists( String userName );

    CompletionStage<String> userNameFromToken( String token);

    CompletionStage<List<SEC_User>> getAll();

    void remove( int userId );

    CompletionStage<Integer> persist( SEC_User user );

    CompletionStage<Integer> merge( SEC_User user );

    CompletionStage<Integer> flush();
}

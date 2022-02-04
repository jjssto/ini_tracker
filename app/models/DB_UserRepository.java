package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;

@ImplementedBy( DB_JPAUserRepository.class )
public interface DB_UserRepository {
    CompletionStage<SEC_User> findByToken( String token );

    CompletionStage<SEC_User> findByUserName( String userName );

    CompletionStage<String> userNameFromToken( String token);

    CompletionStage<Integer> persist( SEC_User user );

    CompletionStage<Integer> merge( SEC_User user );

    CompletionStage<Integer> flush();
}

package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;

@ImplementedBy( JPAAuthorisedUserRepo.class )
public interface AuthorisedUserRepo {

    CompletionStage<AuthorisedUser> findById( int id );
    CompletionStage<AuthorisedUser> findByUserName( String userName );
}

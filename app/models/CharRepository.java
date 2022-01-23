package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.List;

@ImplementedBy( JPACharRepository.class )
public interface CharRepository {

    /* add new instance to DB */
    CompletionStage<SR4Char> persist(SR4Char chara );

    /* update */
    CompletionStage<SR4Char> merge(SR4Char chara );

    /* remove */
    CompletionStage<SR4Char> remove( SR4Char chara );



    /* Auswahl über ID */
    CompletionStage<SR4Char> getChar( Integer charId );

    /* Listen */
    CompletionStage<List<SR4Char>> allChars();
    CompletionStage<List<SR4Char>> allOthers( Integer combatId );
    CompletionStage<List<SR4Char>> inCombat( Integer combatId );
}



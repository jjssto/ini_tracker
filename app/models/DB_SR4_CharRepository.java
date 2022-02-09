package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.List;

@ImplementedBy( DB_SR4_CharRepositoryImp.class )
public interface DB_SR4_CharRepository {

    /* add new instance to DB */
    CompletionStage<SR4_Char> persist( SR4_Char chara );

    /* update */
    CompletionStage<SR4_Char> merge( SR4_Char chara );

    /* remove */
    CompletionStage<SR4_Char> remove( SR4_Char chara );



    /* Auswahl über ID */
    CompletionStage<SR4_Char> getChar( Integer charId );
    CompletionStage<Integer> getRecordId( Integer charId, Integer combatId );

    /* Listen */
    CompletionStage<List<SR4_Char>> allChars();
    CompletionStage<List<SR4_Char>> allOthers( Integer combatId );
    CompletionStage<List<SR4_Char>> inCombat( Integer combatId );
}



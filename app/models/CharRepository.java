package models;

import com.google.inject.ImplementedBy;
import java.util.concurrent.CompletionStage;
import java.util.List;

@ImplementedBy( JPACharRepository.class )
public interface CharRepository {

    /* add new instance to DB */
    CompletionStage<SR4Char> persist(SR4Char chara );
    CompletionStage<CharRecord> persist(CharRecord record );
    CompletionStage<Combat> persist(Combat combat );

    /* update */
    CompletionStage<SR4Char> merge(SR4Char chara );
    CompletionStage<CharRecord> merge(CharRecord record );
    CompletionStage<Combat> merge(Combat combat );

    /* remove */
    CompletionStage<SR4Char> remove( SR4Char chara );
    CompletionStage<CharRecord> remove( CharRecord record );
    CompletionStage<Combat> remove( Combat combat );



    /* Auswahl über ID */
    CompletionStage<SR4Char> getChar( Integer charId );
    CompletionStage<CharRecord> getRecord( Integer recordId );
    CompletionStage<Combat> getCombat( Integer combatId );

    /* Listen */
    CompletionStage<List<SR4Char>> allChars();
    CompletionStage<List<SR4Char>> allOthers( Integer combatId );
    CompletionStage<List<CombatShort>> allCombats();
    public CompletionStage<List<CharRecord>> iniList( Integer combatId ) ;
}



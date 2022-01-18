package models;

import com.google.inject.ImplementedBy;
import java.util.concurrent.CompletionStage;
import java.util.List;

@ImplementedBy( JPACharRepository.class )
public interface CharRepository {

    /* add new instance to DB */
    CompletionStage<SR4Char> add( SR4Char chara );
    CompletionStage<CharRecord> add( CharRecord record );
    CompletionStage<Combat> add( Combat combat );

    /* update */
    CompletionStage<SR4Char> update( SR4Char chara );
    CompletionStage<CharRecord> update( CharRecord record );
    CompletionStage<Combat> update( Combat combat );

    /* Auswahl über ID */
    CompletionStage<SR4Char> getChar( Integer charId );
    CompletionStage<CharRecord> getRecord( Integer recordId );
    CompletionStage<Combat> getCombat( Integer combatId );

    /* Listen */
    CompletionStage<List<SR4Char>> allChars();
    CompletionStage<List<CombatShort>> allCombats();
    public CompletionStage<List<CharRecord>> iniList( Integer combatId ) ;
}



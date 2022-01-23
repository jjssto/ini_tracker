package models;

import com.google.inject.ImplementedBy;
import java.util.concurrent.CompletionStage;
import java.util.List;

@ImplementedBy( JPACombatRepository.class )
public interface CombatRepository {


    CompletionStage<CharRecord> persist(CharRecord record );
    CompletionStage<Combat> persist(Combat combat );

    CompletionStage<CharRecord> merge(CharRecord record );
    CompletionStage<Combat> merge(Combat combat );

    CompletionStage<Object> remove( int recordId );
    CompletionStage<Combat> remove( Combat combat );

    CompletionStage<CharRecord> getRecord( Integer recordId );
    CompletionStage<Combat> getCombat( Integer combatId );

    CompletionStage<List<CharRecord>> iniList( Integer combatId );
    CompletionStage<List<CombatShort>> allCombats() ;
}


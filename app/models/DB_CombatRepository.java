package models;

import com.google.inject.ImplementedBy;
import java.util.concurrent.CompletionStage;
import java.util.List;

@ImplementedBy( DB_JPACombatRepository.class )
public interface DB_CombatRepository {


    CompletionStage<SR4_CharRecord> persist( SR4_CharRecord record );
    CompletionStage<SR4_Combat> persist( SR4_Combat combat );

    CompletionStage<SR4_CharRecord> merge( SR4_CharRecord record );
    CompletionStage<SR4_Combat> merge( SR4_Combat combat );

    CompletionStage<Object> remove( int recordId );
    CompletionStage<SR4_Combat> remove( SR4_Combat combat );

    CompletionStage<SR4_CharRecord> getRecord( Integer recordId );
    CompletionStage<SR4_Combat> getCombat( Integer combatId );

    CompletionStage<List<SR4_CharRecord>> iniList( Integer combatId );
    CompletionStage<List<SR4_CombatShort>> allCombats() ;
}


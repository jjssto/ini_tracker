
package models.db;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

/**
 * Custom execution context wired to "database.dispatcher" thread pool
 */
public class DB_DatabaseExecutionContext extends CustomExecutionContext {
    @Inject
    public DB_DatabaseExecutionContext( ActorSystem actorSystem) {
        super(actorSystem, "database.dispatcher");
    }
}

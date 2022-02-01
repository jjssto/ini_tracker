package security;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.cache.HandlerCache;

import javax.inject.Singleton;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@Singleton
public class MyHandlerCache implements HandlerCache
{
    private final DeadboltHandler defaultHandler = new MyDeadboltHandler();

    @Override
    public DeadboltHandler apply(final String key)
    {
        return defaultHandler;
    }

    @Override
    public DeadboltHandler get()
    {
        return defaultHandler;
    }
}
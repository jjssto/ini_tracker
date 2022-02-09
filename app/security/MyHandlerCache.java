package security;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.cache.HandlerCache;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MyHandlerCache implements HandlerCache
{
    private final DeadboltHandler defaultHandler;

    @Inject
    public MyHandlerCache(
        MyDeadboltHandler defaultHandler
    ) {
        this.defaultHandler = defaultHandler;
    }

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

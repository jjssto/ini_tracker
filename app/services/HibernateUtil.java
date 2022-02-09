package services;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {
    private static EntityManagerFactory emf;

    public HibernateUtil() {
        emf = Persistence.createEntityManagerFactory( "defaultPersistenceUnit" );
    }

    public EntityManagerFactory getEmf () {
        return emf;
    }
}

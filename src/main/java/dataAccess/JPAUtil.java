package dataAccess;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Rides24");

    public static EntityManager createEntityManager() {
        return emf.createEntityManager();
    }
}
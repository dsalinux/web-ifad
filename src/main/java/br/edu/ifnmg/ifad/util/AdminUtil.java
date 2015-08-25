package br.edu.ifnmg.ifad.util;

import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.ejb.EntityManagerImpl;

/**
 *
 * @author Danilo Souza Almeida
 */
public class AdminUtil {
    
    public static Session getSessionFromEntityManager(EntityManager entityManager) {
        Session session;
        if (entityManager == null) {
            throw new IllegalStateException("Session has not been set on DAO before usage");
        }

        Object object = entityManager.getDelegate();
        if (!(object instanceof Session)) {
            object = ((EntityManagerImpl) object).getSession();
        }

        session = (Session) object;
        return session;
    }
    
}

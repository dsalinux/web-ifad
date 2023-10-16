package br.edu.ifnmg.ifad.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Hibernate Utility class with a convenient method to get Session Factory object.
 *
 * @author danilo
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    private static Session session;
    
    static {
        try {
            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static Session getSession(){
        if(session == null){
            session = getSessionFactory().openSession();
        } else if(!session.isOpen()){
            session = sessionFactory.openSession();
        }
        return session;
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

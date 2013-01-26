package org.dao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.objects.AppBO;
import org.objects.CommentBO;
import org.objects.MarketBO;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: liorsolomon
 * Date: 11/17/12
 * Time: 8:54 PM
 * General dao session manager
 */
public class SessionFactoryHelper {
    private static SessionFactory sessionFactory = null;
    private DataSource dataSource;
    private static Session session = null;

    static {
        try {
            sessionFactory = new Configuration().configure()
                    .addAnnotatedClass(AppBO.class)
                    .addAnnotatedClass(CommentBO.class)
                    .addAnnotatedClass(MarketBO.class)
                    .buildSessionFactory();

            if (session == null) {
                session = sessionFactory.openSession();
            }
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static SessionFactory getOurSessionFactory() {
        return sessionFactory;
    }

    public static void setOurSessionFactory(SessionFactory ourSessionFactory) {
        SessionFactoryHelper.sessionFactory = ourSessionFactory;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected void finalize() throws Throwable {
        if (getSession().getTransaction().isActive()) {
            System.out.println("There is an open transaction when the DAO is finalize!!!! There is a connection that was not closed.");
        }
        super.finalize();
    }

    public void rollBack(){
        getSession().getTransaction().rollback();
    }

    public void commit() {
        getSession().getTransaction().commit();
    }

    public void closeSession() {
        getSession().close();
    }

    public void beginTransaction(){
        getSession().beginTransaction();
    }

    public Session getSession() {
        if (session == null) {
            return sessionFactory.openSession();
        }
        return session;
    }

    private void setSession(Session session) {
        this.session = session;
    }

    /**
     * Roll back the current transaction. Additionally, any exceptions are logged and then thrown away.
     */
    public void rollbackIgnoringExceptions() {
        try {
            getSession().getTransaction().rollback();
        } catch (HibernateException e) {
            System.out.println("Error rolling back a transaction" + e);
        }
    }

    /**
     * Flush the session into the DB, but do not commit the transaction
     */
    public void flush() {
        getSession().flush();
    }

    /**
     * Try to find all instances of the persistent class type
     *
     * @param clazz the requested entity class
     * @param <T>   a T object.
     * @return list of all entities of the requested class
     */
    public <T> List<T> findAll(Class<T> clazz) {
        return findByCriteria(clazz);
    }

    /**
     * Try to find an object by its id without locking it. The id is the primary key of the entity as defined in the
     * entity class and the hibernate mapping.
     *
     * @param clazz the requested entity class
     * @param id    The entity primary key (as defined in the hibernate mapping file)
     * @param <T>   a T object.
     * @return The entity
     */
    public <T> T findById(Class<T> clazz, Serializable id) {
        return (T) (getSession().get(clazz, id));
    }

    /**
     * Run a query according to given criteria, and return all the results
     *
     * @param clazz     the requested entity class
     * @param criterion list of criteria
     * @param <T>       a T object.
     * @return the results
     */
    protected <T> List<T> findByCriteria(Class<T> clazz, Criterion... criterion) {
        Map<String, String> aliasMap = null;
        return findByCriteria(clazz, aliasMap, criterion);
    }

    protected <T> List<T> findByCriteria(Class<T> clazz, Map<String, String> aliasMap, Criterion... criterion) {

        Criteria crit = session.createCriteria(clazz);

        if (aliasMap != null) {
            for (String key : aliasMap.keySet()) {
                crit.createAlias(key, aliasMap.get(key));
            }
        }

        for (Criterion c : criterion) {
            crit.add(c);
        }

        return crit.list();
    }

    public <T> T save(T entity) {
        getSession().save(entity);
        return entity;
    }
}

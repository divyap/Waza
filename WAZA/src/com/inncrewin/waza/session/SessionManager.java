package com.inncrewin.waza.session;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.inncrewin.waza.exception.DataException;

public class SessionManager {
	
	private static SessionFactory sessionFactory;
	
	public SessionManager() {
		if (sessionFactory == null)
			configureSessionFactory();
	}
	
	private static SessionFactory configureSessionFactory() throws HibernateException {
	    Configuration configuration = new Configuration();
	    configuration.configure("hibernate.cfg.xml");
	    ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();        
	    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	    return sessionFactory;
	}
	
	public Query createQuery(Session session, String queryString)
            throws HibernateException {
    	Query queryObject = session.createQuery(queryString);
        queryObject.setCacheable(true);
        
        return queryObject;
    }

    public List find(final String queryString) throws DataException {
    	
    	Session session = sessionFactory.openSession();
		List list = session.createQuery(queryString).setCacheable(true).list();
		
		// Is it possible to close the session?
		session.close();
		
		return list;
    }
    
	public List find(final String queryString, final Object value)
			throws DataException {
		Session session = sessionFactory.openSession();
		
		Query queryObject = createQuery(session, queryString);
		queryObject.setCacheable(true);
		queryObject.setParameter(0, value);
		return queryObject.list();
	}

	public int save(Object obj){
		int result = 0;
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.save(obj);
			tx.commit();
			result = 1;
		}catch(HibernateException he){
			 if (tx!=null) 
				 tx.rollback();
	         result = 0;
	         he.printStackTrace(); 
		}finally{
			session.close();
		}
		return result;
	}
	
	public int saveOrUpdate(Object obj){
		int result = 0;
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.saveOrUpdate(obj);
			tx.commit();
			result = 1;
		}catch(HibernateException he){
			 if (tx!=null) 
				 tx.rollback();
	         result = 0;
	         he.printStackTrace(); 
		}finally{
			session.close();
		}
		return result;
	}
	
	public Object load(Class clazz, Long id){
		Session session = sessionFactory.openSession();
		Object cls = null;
		try {
			session.load(clazz, id);
		} catch (HibernateException e) {
			throw new DataException(e.getMessage(), e);
		}finally{
			session.close();
		}
		return cls;
	}
}

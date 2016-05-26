package database;

import database.dao.UserDataSetDAO;
import database.datasets.UserDataset;
import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.annotations.TableBinder;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public final class DBService {
    private SessionFactory sessionFactory;

    public DBService(Configuration configuration) {
        configuration.addAnnotatedClass(UserDataset.class);
        sessionFactory = createSessionFactory(configuration);
    }

    public void save(UserDataset dataSet) {
        Session session = null;
        try
        {
            session = sessionFactory.openSession();
            final Transaction transaction = session.beginTransaction();
            final UserDataSetDAO dao = new UserDataSetDAO(session);
            dao.save(dataSet);
            transaction.commit();
        }
        finally {
            if (session != null)
            {
                session.close();
            }
        }
    }

    public UserDataset readByName(String name) {
        Session session = null;
        try
        {
            session = sessionFactory.openSession();
            final Transaction transaction = session.beginTransaction();
            final UserDataSetDAO dao = new UserDataSetDAO(session);
            final UserDataset dataset = dao.readByName(name);
            transaction.commit();
            return dataset;
        }
        catch (HibernateException e)
        {
            return null;
        }
        finally {
            if (session != null)
            {
                session.close();
            }
        }
    }

    public UserDataset readByEmail(String email) {
        Session session = null;
        try
        {
            session = sessionFactory.openSession();
            final Transaction transaction = session.beginTransaction();
            final UserDataSetDAO dao = new UserDataSetDAO(session);
            final UserDataset dataset = dao.readByEmail(email);
            transaction.commit();
            return dataset;
        }
        catch (HibernateException e)
        {
            return null;
        }
        finally {
            if (session != null)
            {
                session.close();
            }
        }
    }

    public UserDataset readById(Long id)
    {
        Session session = null;
        try
        {
            session = sessionFactory.openSession();
            final Transaction transaction = session.beginTransaction();
            final UserDataSetDAO dao = new UserDataSetDAO(session);
            final UserDataset dataset = dao.readById(id);
            transaction.commit();
            return dataset;
        }
        catch (HibernateException e)
        {
            return null;
        }
        finally {
            if (session != null)
            {
                session.close();
            }
        }
    }

    public void shutdown(){
        sessionFactory.close();
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        final ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public void deleteByLogin(String login)
    {
        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        try
        {
            final UserDataset user = readByName(login);
            final UserDataSetDAO dao = new UserDataSetDAO(session);
            dao.delete(user);
        }
        finally {
            transaction.commit();
            session.close();
        }
    }
}

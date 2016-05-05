package database;

import database.dao.UserDataSetDAO;
import database.datasets.UserDataset;
import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public final class DBService {
    private SessionFactory sessionFactory;

    private static DBService instance = new DBService();
    public static DBService getService()
    {
        return instance;
    }


    private DBService() {
        final Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UserDataset.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost/tictactoe");
        configuration.setProperty("hibernate.connection.username", "root");
        configuration.setProperty("hibernate.connection.password", "password");
        configuration.setProperty("hibernate.show_sql", "true");
        //configuration.setProperty("hibernate.hbm2ddl.auto", "create");

        sessionFactory = createSessionFactory(configuration);
    }

    public void save(UserDataset dataSet) {
        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        final UserDataSetDAO dao = new UserDataSetDAO(session);
        dao.save(dataSet);
        transaction.commit();
    }

    public void update(UserDataset dataSet)
    {
        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        final UserDataSetDAO dao = new UserDataSetDAO(session);
        dao.update(dataSet);
        transaction.commit();
    }

    public UserDataset readByName(String name) {
        final Session session = sessionFactory.openSession();
        final UserDataSetDAO dao = new UserDataSetDAO(session);
        return dao.readByName(name);
    }

    public UserDataset readByEmail(String email) {
        final Session session = sessionFactory.openSession();
        final UserDataSetDAO dao = new UserDataSetDAO(session);
        return dao.readByEmail(email);
    }

    public UserDataset readById(Long id)
    {
        final Session session = sessionFactory.openSession();
        final UserDataSetDAO dao = new UserDataSetDAO(session);
        return dao.readById(id);
    }

    public List<UserDataset> readAll() {
        final Session session = sessionFactory.openSession();
        final UserDataSetDAO dao = new UserDataSetDAO(session);
        return dao.readAll();
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

    public int readUsersSize() {
        final Session session = sessionFactory.openSession();
        final UserDataSetDAO dao = new UserDataSetDAO(session);
        return dao.readUsersSize();
    }

    public List<UserDataset> readTopN(int n) {
        final Session session = sessionFactory.openSession();
        final UserDataSetDAO dao = new UserDataSetDAO(session);
        return dao.readTopN(n);
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

    public void truncate()
    {
        final Session session = sessionFactory.openSession();
        try{
            final Transaction transaction = session.beginTransaction();
            session.createSQLQuery("truncate table users").executeUpdate();
            transaction.commit();
            session.close();
        }
        finally {
            session.close();
        }
    }
}

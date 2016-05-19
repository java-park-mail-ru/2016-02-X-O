package database.dao;

import database.datasets.UserDataset;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class UserDataSetDAO {
    private Session session;

    public UserDataSetDAO(Session session) {
        this.session = session;
    }

    public void save(UserDataset dataSet) {
        session.saveOrUpdate(dataSet);
        session.flush();
    }

    public void update(UserDataset dataset)
    {
        session.update(dataset);
    }

    public UserDataset readByName(String name) {
        final Criteria criteria = session.createCriteria(UserDataset.class);
        return (UserDataset) criteria.add(Restrictions.eq("login", name)).uniqueResult();
    }

    public UserDataset readById(Long id){
        final Criteria criteria = session.createCriteria(UserDataset.class);
        return (UserDataset) criteria.add(Restrictions.eq("id", id)).uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<UserDataset> readAll() {
        final Criteria criteria = session.createCriteria(UserDataset.class);
        return (List<UserDataset>) criteria.list();
    }

    public UserDataset readByEmail(String email) {
        final Criteria criteria = session.createCriteria(UserDataset.class);
        return (UserDataset) criteria.add(Restrictions.eq("email", email)).uniqueResult();
    }

    public int readUsersSize() {
        final Criteria criteria = session.createCriteria(UserDataset.class);
        return criteria.list().size();
    }

    @SuppressWarnings("unchecked")
    public List<UserDataset> readTopN(int n) {
        final Criteria criteria = session.createCriteria(UserDataset.class);
        criteria.addOrder(Order.asc("rating")).setMaxResults(n);
        return (List<UserDataset>) criteria.list();
    }

    public void delete(UserDataset user)
    {
        session.delete(user);
        session.flush();
    }
}
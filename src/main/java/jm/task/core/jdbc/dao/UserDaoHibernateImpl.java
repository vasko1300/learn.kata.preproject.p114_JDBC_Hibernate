package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Transaction;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        String sql = """
            CREATE TABLE if not exists users (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                name varchar(50),
                last_name varchar(50),
                age INT)""";
        Transaction tx = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error while creating users table", ex);
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";
        Transaction tx = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Failed to drop users table", ex);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction tx = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(new User(name, lastName, age));
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Failed to save user", ex);
        }
    }

    @Override
    public void removeUserById(long id) {
//        String hql = "delete from User where id = :id";
        Transaction tx = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
//            session.createQuery(hql)
//                    .setParameter("id", id)
//                    .executeUpdate();
            User user = session.get(User.class, id);
            session.remove(user);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Failed to drop users table", ex);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        String hql = "from User";
        Transaction tx = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            users = session.createQuery(hql, User.class).getResultList();
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Failed to get users", ex);
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String hql = "delete from User";
        Transaction tx = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.createQuery(hql).executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Failed to get users", ex);
        }
    }
}

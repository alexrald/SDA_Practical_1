package persistence;

import model.User;
import model.UserRole;
import util.DbUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class RepositoryUser {

    private EntityManager entityManager;

    public RepositoryUser() {
        entityManager = DbUtil.getEntityManager();
    }

    public boolean createUser(User user)
    {
        try
        {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
        }
        catch (Exception e)
        {
            entityManager.getTransaction().rollback();
            return false;
        }
        return true;
    }

    public boolean updateUser(User user)
    {
        try
        {
            entityManager.getTransaction().begin();
            entityManager.merge(user);
            entityManager.getTransaction().commit();
        }
        catch (Exception e)
        {
            entityManager.getTransaction().rollback();
            return false;
        }
        return true;

    }

    public List<User> listAllUsers()
    {
        List<User> list;
        try {
            list = entityManager.createQuery("FROM User", User.class).getResultList();
        }
        catch (Exception e)
        {
            list = null;
        }
        return list;
    }

    public List<User> listUsersByRole(UserRole userRole)
    {
        try
        {
            return entityManager
                    .createQuery("FROM User WHERE userRole = :role", User.class)
                    .setParameter("role", userRole)
                    .getResultList();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public User findUserByName(String name)
    {
        try
        {
            return entityManager
                    .createQuery("FROM User WHERE userName = :rName", User.class)
                    .setParameter("rName", name)
                    .getSingleResult();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public int countUsersTotal()
    {
        try
        {
            return entityManager
                    .createQuery("SELECT COUNT(*) FROM User", Long.class)
                    .getSingleResult()
                    .intValue();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public int countUsersByRole(UserRole userRole)
    {
        try
        {
            return entityManager
                    .createQuery("SELECT COUNT(*) FROM User WHERE userRole = :uRole", Long.class)
                    .setParameter("uRole", userRole)
                    .getSingleResult()
                    .intValue();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }

    }

    public User findUserById(int id)
    {
        return null;
    }

    //public User findUserByRole()

}

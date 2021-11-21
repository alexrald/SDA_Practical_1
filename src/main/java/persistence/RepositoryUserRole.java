package persistence;

import model.User;
import model.UserRole;
import util.DbUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class RepositoryUserRole {

    private EntityManager entityManager;

    public RepositoryUserRole() {
        entityManager = DbUtil.getEntityManager();
    }

    public boolean createUserRole(UserRole userRole) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(userRole);
            entityManager.getTransaction().commit();
            return true;
        }
        catch (Exception e)
        {
            entityManager.getTransaction().rollback();
            return false;
        }
    }

    public List<UserRole> listUserRoles()                   // Get all user roles
    {
        try
        {
            return entityManager.createQuery("FROM UserRole", UserRole.class).getResultList();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserRole getRoleById(int id)
    {
        try
        {
            return entityManager.find(UserRole.class, id);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public UserRole getRoleByName(String name)
    {
        try
        {
            return entityManager
                    .createQuery("FROM UserRole WHERE roleName = :rName", UserRole.class)
                    .setParameter("rName", name)
                    .getResultList()
                    .get(0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }



}

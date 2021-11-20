package persistence;

import model.Artist;
import util.DbUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class RepositoryArtist {

    private EntityManager entityManager;

    public RepositoryArtist() {
        entityManager = DbUtil.getEntityManager();
    }

    public boolean create(Artist artist)
    {
        try
        {
            entityManager.getTransaction().begin();
            entityManager.persist(artist);
            entityManager.getTransaction().commit();
        }
        catch (Exception e)
        {
            entityManager.getTransaction().rollback();
            return false;
        }
        return true;
    }

    public boolean update(Artist artist)
    {
        try
        {
            entityManager.getTransaction().begin();
            entityManager.merge(artist);
            entityManager.getTransaction().commit();
        }
        catch (Exception e)
        {
            entityManager.getTransaction().rollback();
            return false;
        }
        return true;

    }

    public List<Artist> listAll()
    {
        List<Artist> list;
        try {
            list = entityManager.createQuery("FROM Artist", Artist.class).getResultList();
        }
        catch (Exception e)
        {
            list = null;
        }
        return list;
    }

    public Artist findByName(String name)
    {
        try
        {
            return entityManager
                    .createQuery("FROM Artist WHERE artistName = :rName", Artist.class)
                    .setParameter("rName", name)
                    .getResultList()
                    .get(0);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public Artist findById(int id)
    {
        return null;
    }

}

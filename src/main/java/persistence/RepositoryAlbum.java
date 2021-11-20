package persistence;

import model.Album;
import model.Artist;
import util.DbUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class RepositoryAlbum {

    private EntityManager entityManager;

    public RepositoryAlbum() {
        entityManager = DbUtil.getEntityManager();
    }

    public boolean create(Album album)
    {
        try
        {
            entityManager.getTransaction().begin();
            entityManager.persist(album);
            entityManager.getTransaction().commit();
        }
        catch (Exception e)
        {
            entityManager.getTransaction().rollback();
            return false;
        }
        return true;
    }

    public boolean update(Album album)
    {
        try
        {
            entityManager.getTransaction().begin();
            entityManager.merge(album);
            entityManager.getTransaction().commit();
        }
        catch (Exception e)
        {
            entityManager.getTransaction().rollback();
            return false;
        }
        return true;

    }

    public List<Album> listAll()
    {
        List<Album> list;
        try {
            list = entityManager.createQuery("FROM Album", Album.class).getResultList();
        }
        catch (Exception e)
        {
            list = null;
        }
        return list;
    }

    public List<Album> listByArtist(Artist artist)
    {
        try {
            return entityManager
                    .createQuery("FROM Album WHERE artist = :objArtist", Album.class)
                    .setParameter("objArtist", artist)
                    .getResultList();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    public Album findByName(String name)
    {
        try
        {
            return entityManager
                    .createQuery("FROM Album WHERE albumName = :rName", Album.class)
                    .setParameter("rName", name)
                    .getResultList()
                    .get(0);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public Album findById(int id)
    {
        return null;
    }

}

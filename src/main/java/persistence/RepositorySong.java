package persistence;

import model.Album;
import model.Song;
import model.Artist;
import util.DbUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class RepositorySong {


    private EntityManager entityManager;

    public RepositorySong() {
        entityManager = DbUtil.getEntityManager();
    }

    public boolean create(Song song)
    {
        try
        {
            entityManager.getTransaction().begin();
            entityManager.persist(song);
            entityManager.getTransaction().commit();
        }
        catch (Exception e)
        {
            entityManager.getTransaction().rollback();
            return false;
        }
        return true;
    }

    public boolean update(Song song)
    {
        try
        {
            entityManager.getTransaction().begin();
            entityManager.merge(song);
            entityManager.getTransaction().commit();
        }
        catch (Exception e)
        {
            entityManager.getTransaction().rollback();
            return false;
        }
        return true;

    }

    public List<Song> listAll()
    {
        List<Song> list;
        try {
            list = entityManager.createQuery("FROM Song", Song.class).getResultList();
        }
        catch (Exception e)
        {
            list = null;
        }
        return list;
    }

    public List<Song> listByArtist(Artist artist)
    {
        try {
            return entityManager
                    .createQuery("FROM Song WHERE artist = :objArtist", Song.class)
                    .setParameter("objArtist", artist)
                    .getResultList();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public List<Song> listByAlbum(Album album)
    {
        try {
            return entityManager
                    .createQuery("FROM Song WHERE album = :objAlbum", Song.class)
                    .setParameter("objAlbum", album)
                    .getResultList();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    public Song findByName(String name)
    {
        try
        {
            return entityManager
                    .createQuery("FROM Song WHERE songName = :rName", Song.class)
                    .setParameter("rName", name)
                    .getResultList()
                    .get(0);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public Song findById(int id)
    {
        return null;
    }

}

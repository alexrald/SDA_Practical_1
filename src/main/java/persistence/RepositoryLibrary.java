package persistence;

import model.*;
import util.DbUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class RepositoryLibrary {

    private final EntityManager entityManager;

    public RepositoryLibrary() {
        entityManager = DbUtil.getEntityManager();
    }

    public boolean createRecord(Library library) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(library);
            entityManager.getTransaction().commit();
            return true;
        }
        catch (Exception e)
        {
            entityManager.getTransaction().rollback();
            return false;
        }
    }

    public List<Library> listAllRecords()                   // Get all records
    {
        try
        {
            return entityManager.createQuery("FROM Library", Library.class).getResultList();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Library> listUserRecords(User user)                   // Get records by user
    {
        try
        {
            return entityManager
                    .createQuery("FROM Library WHERE user = :oUser", Library.class)
                    .setParameter("oUser", user)
                    .getResultList();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Library> listUserRecords(User user, Album album)                   // Get records by user
    {
        try
        {
            return entityManager
                    .createQuery("FROM Library WHERE user = :oUser AND album = :oAlbum", Library.class)
                    .setParameter("oUser", user)
                    .setParameter("oAlbum", album)
                    .getResultList();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Library> listUserRecords(User user, Artist artist)                   // Get records by user
    {
        try
        {
            return entityManager
                    .createQuery("FROM Library WHERE user = :oUser AND artist = :oArtist", Library.class)
                    .setParameter("oUser", user)
                    .setParameter("oArtist", artist)
                    .getResultList();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean checkIfInLibrary(User user, Song song)
    {
        try {
            return entityManager
                    .createQuery("FROM Library WHERE user = :oUser AND song = :oSong", Library.class)
                    .setParameter("oUser", user)
                    .setParameter("oSong", song)
                    .getResultList().size() > 0;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public List<Artist> getAllArtists(User user)
    {
        try
        {
            return entityManager
                    .createQuery("SELECT DISTINCT l.artist FROM Library l INNER JOIN l.artist WHERE l.user = :oUser", Artist.class)
                    .setParameter("oUser", user)
                    .getResultList();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public List<Album> getAllAlbums(User user)
    {
        try
        {
            return entityManager
                    .createQuery("SELECT DISTINCT l.album FROM Library l INNER JOIN l.album WHERE l.user = :oUser", Album.class)
                    .setParameter("oUser", user)
                    .getResultList();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public List<Album> getAlbumsByArtist(User user, Artist artist)
    {
        try
        {
            return entityManager
                    .createQuery("SELECT DISTINCT l.album FROM Library l WHERE l.user = :oUser AND l.artist = :oArtist", Album.class)
                    .setParameter("oUser", user)
                    .setParameter("oArtist", artist)
                    .getResultList();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public List<Song> getAllSongs(User user)
    {
        try
        {
            return entityManager
                    .createQuery("SELECT l.song FROM Library l INNER JOIN l.song WHERE l.user = :oUser", Song.class)
                    .setParameter("oUser", user)
                    .getResultList();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public List<Song> getSongsByAlbum(User user, Album album)
    {
        try
        {
            return entityManager
                    .createQuery("SELECT DISTINCT l.song FROM Library l INNER JOIN l.album WHERE l.user = :oUser AND l.album = :oAlbum", Song.class)
                    .setParameter("oUser", user)
                    .setParameter("oAlbum", album)
                    .getResultList();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public int countForUser(User user)
    {
        try
        {
            return entityManager
                    .createQuery("SELECT COUNT(*) FROM Library WHERE user = :oUser", Long.class)
                    .setParameter("oUser", user)
                    .getSingleResult()
                    .intValue();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public int countByArtistForUser(User user)
    {
        try
        {
            return entityManager
                    .createQuery("SELECT COUNT(DISTINCT Artist) FROM Library WHERE user = :oUser", Long.class)
                    .setParameter("oUser", user)
                    .getSingleResult()
                    .intValue();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

}

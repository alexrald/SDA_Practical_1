package persistence;

import model.Artist;
import model.Library;
import model.Subscription;
import model.User;
import util.DbUtil;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

public class RepositorySubscription {


    private final EntityManager entityManager;

    public RepositorySubscription() {
        entityManager = DbUtil.getEntityManager();
    }

    public boolean createRecord(Subscription subscription) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(subscription);
            entityManager.getTransaction().commit();
            return true;
        }
        catch (Exception e)
        {
            entityManager.getTransaction().rollback();
            return false;
        }
    }

    public List<Subscription> listAllRecords()                   // Get all records
    {
        try
        {
            return entityManager.createQuery("FROM Subscription", Subscription.class).getResultList();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Subscription> listUserRecords(User user)                   // Get records by user
    {
        try
        {
            return entityManager
                    .createQuery("FROM Subscription WHERE user = :oUser", Subscription.class)
                    .setParameter("oUser", user)
                    .getResultList();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Artist> listUpdatedArtists(User user)             // Show all updated artists
    {
        try
        {
            return entityManager
                    .createQuery("SELECT s.artist FROM Subscription s WHERE s.user = :oUser AND s.hasNewSongs = true", Artist.class)
                    .setParameter("oUser", user)
                    .getResultList();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public List<Artist> listNotSubscribedArtistsForUser(User user)
    {
        try
        {
            return entityManager
                    .createQuery("FROM Artist a WHERE a NOT IN (SELECT s.artist FROM Subscription s WHERE s.user = :oUser)", Artist.class)
                    .setParameter("oUser", user)
                    .getResultList();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    public boolean setUpdateForArtist(Artist artist)
    {
        try
        {
            entityManager.getTransaction().begin();
            entityManager.createQuery("UPDATE Subscription SET hasNewSongs = true WHERE artist = :oArtist")
                    .setParameter("oArtist", artist)
                    .executeUpdate();
            entityManager.getTransaction().commit();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }


    public boolean clearUpdateForUser(User user)
    {
        try
        {
            entityManager.getTransaction().begin();
            entityManager.createQuery("UPDATE Subscription SET hasNewSongs = false WHERE user = :oUser")
                    .setParameter("oUser", user)
                    .executeUpdate();
            entityManager.getTransaction().commit();
            return true;
        }
        catch (Exception e)
        {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
    }

}

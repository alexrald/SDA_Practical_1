package model;

import javax.persistence.*;

@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int     idSubscriptions;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User    user;

    @ManyToOne
    @JoinColumn(name = "idArtist")
    private Artist  artist;

    private boolean     hasNewSongs;

    public Subscription() {
    }

    public Subscription(User user, Artist artist) {
        this.user = user;
        this.artist = artist;
        this.hasNewSongs = false;
    }

    public int getIdSubscriptions() {
        return idSubscriptions;
    }

    public void setIdSubscriptions(int idSubscriptions) {
        this.idSubscriptions = idSubscriptions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public boolean getHasNewSongs() {
        return hasNewSongs;
    }

    public void setHasNewSongs(boolean hasNewSongs) {
        this.hasNewSongs = hasNewSongs;
    }
}

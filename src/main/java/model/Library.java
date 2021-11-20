package model;

import javax.persistence.*;

@Entity
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idLibrary;

    @ManyToOne
    @JoinColumn(name = "idSong")
    private final Song    song;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private final User    user;

    public Library(Song song, User user) {
        this.song = song;
        this.user = user;
    }

    public Song getSong() {
        return song;
    }

    public User getUser() {
        return user;
    }
}

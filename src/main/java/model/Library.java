package model;

import javax.persistence.*;

@Entity
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idLibrary;

    @ManyToOne
    @JoinColumn(name = "idSong")
    private Song    song;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User    user;

    public Library(Song song, User user) {
        this.song = song;
        this.user = user;

        this.album = song.getAlbum();
        this.artist = song.getAlbum().getArtist();
    }

    @ManyToOne
    @JoinColumn(name = "idAlbum")
    private Album   album;

    @ManyToOne
    @JoinColumn(name = "idArtist")
    private Artist  artist;

    public Library() {
    }

    public int getIdLibrary() {
        return idLibrary;
    }

    public Album getAlbum() {
        return album;
    }

    public Artist getArtist() {
        return artist;
    }

    public Song getSong() {
        return song;
    }

    public User getUser() {
        return user;
    }

    public void setIdLibrary(int idLibrary) {
        this.idLibrary = idLibrary;
    }

    public void setSong(Song song) {
        this.song = song;
        this.album = song.getAlbum();
        this.artist = song.getAlbum().getArtist();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAlbum(Album album) {
        this.album = album;
        this.artist = album.getArtist();
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}

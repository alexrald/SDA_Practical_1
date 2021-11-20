package model;

import javax.persistence.*;

@Entity
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int     idAlbum;

    private String  albumName;
    private int     albumYear;
    private String  albumGenre;

    @ManyToOne
    @JoinColumn(name = "idArtist")
    private Artist  artist;

    public Album(String albumName, int albumYear, String albumGenre, Artist artist) {
        this.albumName = albumName;
        this.albumYear = albumYear;
        this.albumGenre = albumGenre;
        this.artist = artist;
    }

    public Album() {
    }

    public int getIdAlbum() {
        return idAlbum;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getAlbumYear() {
        return albumYear;
    }

    public void setAlbumYear(int albumYear) {
        this.albumYear = albumYear;
    }

    public String getAlbumGenre() {
        return albumGenre;
    }

    public void setAlbumGenre(String albumGenre) {
        this.albumGenre = albumGenre;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}

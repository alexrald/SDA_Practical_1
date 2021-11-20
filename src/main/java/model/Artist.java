package model;

import javax.persistence.*;

@Entity
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int     idArtist;

    @Column(unique = true)
    private String  artistName;
    private String  artistGenre;
    private boolean isActive;

    public Artist()
    {

    }

    public Artist(String artistName, String artistGenre, boolean isActive) {
        this.artistName = artistName;
        this.artistGenre = artistGenre;
        this.isActive = isActive;
    }

    public int getIdArtist() {
        return idArtist;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistGenre() {
        return artistGenre;
    }

    public void setArtistGenre(String artistGenre) {
        this.artistGenre = artistGenre;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

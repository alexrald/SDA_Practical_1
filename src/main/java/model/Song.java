package model;

import javax.persistence.*;

@Entity
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int     idSong;

    private String  songName;
    private int     songYear;
    private int     songRating;
    private String  songPath;

    @ManyToOne
    @JoinColumn(name = "idAlbum")
    private Album   album;

    public Song(String songName, int songYear, int songRating, String songPath, Album album) {
        this.songName = songName;
        this.songYear = songYear;
        this.songRating = songRating;
        this.songPath = songPath;
        this.album = album;
    }

    public Song() {
    }

    public int getIdSong() {
        return idSong;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public int getSongYear() {
        return songYear;
    }

    public void setSongYear(int songYear) {
        this.songYear = songYear;
    }

    public int getSongRating() {
        return songRating;
    }

    public void setSongRating(int songRating) {
        this.songRating = songRating;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void play()
    {
        System.out.println("Playing " + songName + "...");
    }

    public void displaySongInfo()
    {
        System.out.println("");
        System.out.println("Song Name:     " + songName);
        System.out.println("Song Album:    " + album.getAlbumName());
        System.out.println("Song Artist:   " + album.getArtist().getArtistName());
        System.out.println("Album Year:    " + album.getAlbumYear());
        System.out.println("Genre:         " + album.getAlbumGenre());
        System.out.println("Artist active: " + album.getArtist().isActive());
        System.out.println("Song Rating:   " + songRating);
        System.out.println("Song Path:     " + songPath);
    }
}

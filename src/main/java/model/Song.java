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

    public int getIdSong() {
        return idSong;
    }

//    public void setIdSong(int idSong) {
//        this.idSong = idSong;
//    }

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
}

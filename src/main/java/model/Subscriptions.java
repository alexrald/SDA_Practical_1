package model;

import javax.persistence.*;

@Entity
public class Subscriptions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int     idSubscriptions;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User    user;

    @ManyToOne
    @JoinColumn(name = "idArtist")
    private Artist  artist;

}

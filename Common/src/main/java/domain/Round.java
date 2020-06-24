package domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="Rounds")
public class Round implements Serializable {
    private Integer gameID;
    private Integer round;
    private String player;
    private String country;
    private String city;
    private String sea;
    private Integer points;

    public Round() {
    }

    public Round(Integer gameID, String player, Integer round) {
        this.gameID = gameID;
        this.player = player;
        this.round = round;
    }

    public Round(Integer gameID, Integer round, String player, String country, String city, String sea, Integer points) {
        this.gameID = gameID;
        this.round = round;
        this.player = player;
        this.country = country;
        this.city = city;
        this.sea = sea;
        this.points = points;
    }

    @Id
    @Column(name="gameID")
    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }

    @Id
    @Column(name="round")
    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    @Id
    @Column(name="player")
    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    @Column(name="country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name="city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name="sea")
    public String getSea() {
        return sea;
    }

    public void setSea(String sea) {
        this.sea = sea;
    }

    @Column(name="points")
    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Round{" +
                "gameID=" + gameID +
                ", round=" + round +
                ", player='" + player + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", sea='" + sea + '\'' +
                ", points=" + points +
                '}';
    }
}



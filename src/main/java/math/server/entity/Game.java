package math.server.entity;

import math.server.repository.utils.Entity;

import java.sql.Timestamp;
import java.time.Instant;

@Entity(value = "game")
@SuppressWarnings("unused")
public class Game {

    private Integer ID;
    private Timestamp time;

    public Game() {
        this.time = Timestamp.from(Instant.now());
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}

package math.server.model;

import java.sql.Timestamp;

public class Game {

    private Integer ID;
    private Timestamp time;

    public Game() {}

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

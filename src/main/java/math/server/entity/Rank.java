package math.server.entity;

import math.server.repository.utils.Entity;

@Entity(value = "rank")
@SuppressWarnings("unused")
public class Rank {

    Integer ID, userID, score;

    public Rank() {}

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}

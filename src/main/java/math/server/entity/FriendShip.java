package math.server.entity;

import math.server.repository.utils.Entity;

@Entity("friendship")
@SuppressWarnings("unused")
public class FriendShip {

    private Integer ID, userID, friendID, status;

    public FriendShip() {}

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

    public Integer getFriendID() {
        return friendID;
    }

    public void setFriendID(Integer friendID) {
        this.friendID = friendID;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

package math.server.dto.response;

@SuppressWarnings("unused")
public class GameInvitation {

    private String inviter, roomID;

    public GameInvitation() {}

    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }
}

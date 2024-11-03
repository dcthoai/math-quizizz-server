package math.server.dto.request;

@SuppressWarnings("unused")
public class FriendShipStatusRequest {

    private Integer ID, status;

    public FriendShipStatusRequest() {}

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

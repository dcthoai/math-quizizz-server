package math.server.dto.response;

@SuppressWarnings("unused")
public class FriendRequestDTO {

    private Integer ID, status;
    private String userSendRequest;

    public FriendRequestDTO() {}

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

    public String getUserSendRequest() {
        return userSendRequest;
    }

    public void setUserSendRequest(String userSendRequest) {
        this.userSendRequest = userSendRequest;
    }
}

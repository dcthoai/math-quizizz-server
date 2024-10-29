package math.server.dto.response;

import java.util.List;

public class RoomDTO {

    private String roomID;
    private Boolean isPlayingGame;
    private List<String> users;

    public RoomDTO() {}

    public RoomDTO(String roomID, Boolean isPlayingGame, List<String> users) {
        this.roomID = roomID;
        this.isPlayingGame = isPlayingGame;
        this.users = users;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomID() {
        return roomID;
    }

    public Boolean getPlayingGame() {
        return isPlayingGame;
    }

    public void setPlayingGame(Boolean playingGame) {
        isPlayingGame = playingGame;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}

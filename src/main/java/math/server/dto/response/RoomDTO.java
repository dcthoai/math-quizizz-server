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

    @SuppressWarnings("unused")
    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    @SuppressWarnings("unused")
    public String getRoomID() {
        return roomID;
    }

    @SuppressWarnings("unused")
    public Boolean getPlayingGame() {
        return isPlayingGame;
    }

    @SuppressWarnings("unused")
    public void setPlayingGame(Boolean playingGame) {
        isPlayingGame = playingGame;
    }

    @SuppressWarnings("unused")
    public List<String> getUsers() {
        return users;
    }

    @SuppressWarnings("unused")
    public void setUsers(List<String> users) {
        this.users = users;
    }
}

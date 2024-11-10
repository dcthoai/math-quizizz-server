package math.server.repository;

import math.server.dto.response.FriendRequestDTO;
import math.server.dto.response.UserDTO;

import java.util.List;

public interface IFriendShipRepository {

    boolean checkFriendShipRequest(Integer userID, Integer friendID);

    List<FriendRequestDTO> getFriendRequestByStatus(Integer userID, Integer status);

    List<UserDTO> getAllFriend(Integer userID);
}

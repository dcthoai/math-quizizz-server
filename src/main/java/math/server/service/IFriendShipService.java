package math.server.service;

import math.server.dto.request.FriendShipStatusRequest;
import math.server.dto.response.FriendRequestDTO;
import math.server.dto.response.UserDTO;

import java.util.List;

public interface IFriendShipService {

    int saveFriendShip(Integer userID, Integer friendID);

    boolean updateFriendShipStatus(FriendShipStatusRequest request);

    List<FriendRequestDTO> getPendingFriendRequest(Integer userID);

    List<UserDTO> getAllFriend(Integer userID);
}

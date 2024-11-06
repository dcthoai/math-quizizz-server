package math.server.service;

import math.server.dto.request.FriendShipStatusRequest;
import math.server.dto.response.UserDTO;
import math.server.entity.FriendShip;

import java.util.List;

public interface IFriendShipService {

    int saveFriendShip(Integer userID, Integer friendID);

    boolean updateFriendShipStatus(FriendShipStatusRequest request);

    List<FriendShip> getFriendShipByStatus(Integer userID, Integer status);

    List<UserDTO> getAllFriend(Integer userID);
}

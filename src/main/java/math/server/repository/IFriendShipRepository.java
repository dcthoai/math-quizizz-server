package math.server.repository;

import math.server.dto.response.FriendShipDTO;
import math.server.entity.FriendShip;

import java.util.List;

public interface IFriendShipRepository {

    List<FriendShip> getFriendShipByStatus(Integer userID, Integer status);

    List<FriendShipDTO> getAllFriend(Integer userID);
}

package math.server.repository;

import math.server.dto.response.UserDTO;
import math.server.entity.FriendShip;

import java.util.List;

public interface IFriendShipRepository {

    List<FriendShip> getFriendShipByStatus(Integer userID, Integer status);

    List<UserDTO> getAllFriend(Integer userID);
}

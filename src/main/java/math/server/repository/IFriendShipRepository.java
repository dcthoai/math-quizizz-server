package math.server.repository;

import math.server.dto.response.FriendRequestDTO;
import math.server.dto.response.UserDTO;

import java.util.List;

public interface IFriendShipRepository {

    List<FriendRequestDTO> getFriendRequestByStatus(Integer userID, Integer status);

    List<UserDTO> getAllFriend(Integer userID);
}

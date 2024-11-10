package math.server.service.impl;

import math.server.common.Constants;
import math.server.dto.request.FriendShipStatusRequest;
import math.server.dto.response.FriendRequestDTO;
import math.server.dto.response.UserDTO;
import math.server.entity.FriendShip;
import math.server.repository.impl.FriendShipRepository;
import math.server.service.IFriendShipService;

import java.util.List;
import java.util.Objects;

public class FriendShipService implements IFriendShipService {

    private final FriendShipRepository friendShipRepository;

    public FriendShipService() {
        this.friendShipRepository = new FriendShipRepository();
    }

    @Override
    public int saveFriendShip(Integer userID, Integer friendID) {
        FriendShip friendShip = new FriendShip();

        friendShip.setUserID(userID);
        friendShip.setFriendID(friendID);
        friendShip.setStatus(Constants.FRIENDSHIP_PENDING);

        return friendShipRepository.save(friendShip);
    }

    @Override
    public boolean checkFriendShipRequest(Integer userID, Integer friendID) {
        return friendShipRepository.checkFriendShipRequest(userID, friendID);
    }

    @Override
    public boolean updateFriendShipStatus(FriendShipStatusRequest request) {
        FriendShip friendShip = friendShipRepository.findOne(request.getID());

        if (Objects.nonNull(friendShip)) {
            friendShip.setStatus(request.getStatus());
            return friendShipRepository.update(friendShip);
        }

        return false;
    }

    @Override
    public List<FriendRequestDTO> getPendingFriendRequest(Integer userID) {
        return friendShipRepository.getFriendRequestByStatus(userID, Constants.FRIENDSHIP_PENDING);
    }

    @Override
    public List<UserDTO> getAllFriend(Integer userID) {
        return friendShipRepository.getAllFriend(userID);
    }
}

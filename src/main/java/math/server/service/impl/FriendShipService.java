package math.server.service.impl;

import math.server.common.Constants;
import math.server.dto.request.FriendShipStatusRequest;
import math.server.dto.response.FriendShipDTO;
import math.server.entity.FriendShip;
import math.server.repository.impl.FriendShipRepository;
import math.server.service.IFriendShipService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class FriendShipService implements IFriendShipService {

    private static final Logger log = LoggerFactory.getLogger(FriendShipService.class);
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
    public boolean updateFriendShipStatus(FriendShipStatusRequest request) {
        FriendShip friendShip = friendShipRepository.findOne(request.getID());

        if (Objects.nonNull(friendShip)) {
            friendShip.setStatus(request.getStatus());
            return friendShipRepository.update(friendShip);
        }

        return false;
    }

    @Override
    public List<FriendShip> getFriendShipByStatus(Integer userID, Integer status) {
        return friendShipRepository.getFriendShipByStatus(userID, status);
    }

    @Override
    public List<FriendShipDTO> getAllFriend(Integer userID) {
        return friendShipRepository.getAllFriend(userID);
    }
}

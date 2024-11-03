package math.server.controller;

import com.google.gson.Gson;
import math.server.common.Constants;
import math.server.dto.request.BaseRequest;
import math.server.dto.request.FriendShipStatusRequest;
import math.server.dto.response.BaseResponse;
import math.server.dto.response.FriendShipDTO;
import math.server.entity.FriendShip;
import math.server.router.EndPoint;
import math.server.router.RouterMapping;
import math.server.service.impl.FriendShipService;
import math.server.service.utils.UserSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@EndPoint("/api/friendship")
@SuppressWarnings("unused")
public class FriendShipController implements RouterMapping {

    private static final Logger log = LoggerFactory.getLogger(FriendShipController.class);
    private final FriendShipService friendShipService;
    private final Gson gson = new Gson();

    public FriendShipController() {
        this.friendShipService = new FriendShipService();
    }

    @EndPoint("/add")
    public BaseResponse friendRequest(UserSession userSession, BaseRequest request) {
        try {
            Integer friendID = Integer.parseInt(request.getRequest());
            int recordID = friendShipService.saveFriendShip(userSession.getUserID(), friendID);

            if (recordID > 0) {
                return new BaseResponse(request.getAction(), Constants.NO_CONTENT);
            }

            return new BaseResponse(Constants.INTERNAL_SERVER_ERROR, false, request.getAction(), "Cannot send friend request.");
        } catch (Exception e) {
            log.error("Cannot parse data from request", e);
            return new BaseResponse(Constants.SOCKET_INVALID_DATA, false, request.getAction(), "Cannot parse data from request");
        }
    }

    @EndPoint("/update")
    public BaseResponse updateFriendShipStatus(UserSession userSession, BaseRequest request) {
        try {
            FriendShipStatusRequest friendShipStatusRequest = gson.fromJson(request.getRequest(), FriendShipStatusRequest.class);
            boolean isSuccess = friendShipService.updateFriendShipStatus(friendShipStatusRequest);

            if (isSuccess) {
                return new BaseResponse(request.getAction(), Constants.NO_CONTENT);
            }

            return new BaseResponse(Constants.INTERNAL_SERVER_ERROR, false, request.getAction(), "Cannot update friendship status.");
        } catch (Exception e) {
            log.error("Cannot parse data from request", e);
            return new BaseResponse(Constants.SOCKET_INVALID_DATA, false, request.getAction(), "Cannot parse data from request");
        }
    }

    @EndPoint("/pending-request")
    public BaseResponse getPendingFriendRequest(UserSession userSession, BaseRequest request) {
        List<FriendShip> friendShips = friendShipService.getFriendShipByStatus(userSession.getUserID(), Constants.FRIENDSHIP_PENDING);
        return new BaseResponse(request.getAction(), gson.toJson(friendShips));
    }

    @EndPoint("/pending-refused")
    public BaseResponse getRefusedFriendRequest(UserSession userSession, BaseRequest request) {
        List<FriendShip> friendShips = friendShipService.getFriendShipByStatus(userSession.getUserID(), Constants.FRIENDSHIP_REFUSED);
        return new BaseResponse(request.getAction(), gson.toJson(friendShips));
    }

    @EndPoint("/all-friend")
    public BaseResponse getAllFriend(UserSession userSession, BaseRequest request) {
        List<FriendShipDTO> friendShipDTOS = friendShipService.getAllFriend(userSession.getUserID());
        return new BaseResponse(request.getAction(), gson.toJson(friendShipDTOS));
    }
}

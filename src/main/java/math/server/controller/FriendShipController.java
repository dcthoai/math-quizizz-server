package math.server.controller;

import com.google.gson.Gson;
import math.server.common.Constants;
import math.server.dto.request.BaseRequest;
import math.server.dto.request.FriendShipStatusRequest;
import math.server.dto.response.BaseResponse;
import math.server.dto.response.FriendRequestDTO;
import math.server.dto.response.UserDTO;
import math.server.router.EndPoint;
import math.server.router.RouterMapping;
import math.server.service.impl.FriendShipService;
import math.server.service.utils.SessionManager;
import math.server.service.utils.UserSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

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
        log.debug("Socket request to send friend request. Endpoint: /api/friendship/add");

        try {
            Integer friendID = Integer.parseInt(request.getRequest());

            if (friendID.equals(userSession.getUserID()))
                return new BaseResponse(Constants.BAD_REQUEST, false, request.getAction(), "Cannot send friend request for yourself");

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
        log.debug("Socket request to update friend request status. Endpoint: /api/friendship/update");

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

    @EndPoint("/pending")
    public BaseResponse getPendingFriendRequest(UserSession userSession, BaseRequest request) {
        log.debug("Socket request to get all pending friend request. Endpoint: /api/friendship/pending");
        List<FriendRequestDTO> friendRequests = friendShipService.getPendingFriendRequest(userSession.getUserID());
        return new BaseResponse(request.getAction(), gson.toJson(friendRequests));
    }

    @EndPoint("/all")
    public BaseResponse getAllFriend(UserSession userSession, BaseRequest request) {
        log.debug("Socket request to get all friendship. Endpoint: /api/friendship/all");
        List<UserDTO> friends = friendShipService.getAllFriend(userSession.getUserID());

        friends.forEach(friend -> {
            UserSession session = SessionManager.getInstance().getSession(friend.getUsername(), false);

            if (Objects.nonNull(session))
                friend.setLoginStatus(session.getLoginState());
            else
                friend.setLoginStatus(false);
        });

        return new BaseResponse(request.getAction(), gson.toJson(friends));
    }
}

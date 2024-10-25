package math.server.controller;

import com.google.gson.Gson;
import math.server.common.Constants;
import math.server.dto.request.BaseRequest;
import math.server.dto.request.UserRequest;
import math.server.dto.response.BaseResponse;
import math.server.model.User;
import math.server.router.EndPoint;
import math.server.router.RouterMapping;
import math.server.service.impl.UserService;
import math.server.service.utils.SessionManager;
import math.server.service.utils.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@EndPoint(value = "/api/user")
@SuppressWarnings("unused")
public class UserController implements RouterMapping {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    @EndPoint("/register")
    public BaseResponse<?> register(UserSession session, BaseRequest request) {
        log.debug("Socket request to register new account. EndPoint: /api/user/login");

        try {
            Gson gson = new Gson();
            UserRequest userRequest = gson.fromJson(request.getRequest(), UserRequest.class);
            User user = userService.findUserByUsername(userRequest.getUsername());

            if (Objects.nonNull(user))
                return new BaseResponse<>(Constants.BAD_REQUEST, false, request.getAction(), "Account is exists");

            Integer userID = userService.save(userRequest);

            if (Objects.nonNull(userID) && userID > 0) {
                return new BaseResponse<>(Constants.SUCCESS, true, request.getAction(), "Register successfully!", session.getUserID());
            }

            return new BaseResponse<>(Constants.SUCCESS, false, request.getAction(), "Register failed! Cannot create your account");
        } catch (Exception e) {
            log.error("Failed to register new account", e);
            return new BaseResponse<>(Constants.INTERNAL_SERVER_ERROR, false, request.getAction(), "Register failed!");
        }
    }

    @EndPoint(value = "/login")
    public BaseResponse<?> login(UserSession session, BaseRequest request) {
        log.debug("Socket request to login. EndPoint: /api/user/login");

        try {
            Gson gson = new Gson();
            UserRequest userRequest = gson.fromJson(request.getRequest(), UserRequest.class);
            boolean isSuccess = userService.checkLogin(userRequest);

            if (isSuccess) {
                session.setUsername(userRequest.getUsername());
                session.setLoginState(true);
                log.info("Login success. Saved user login status information to session");

                return new BaseResponse<>(Constants.SUCCESS, true, request.getAction(), "Login successfully!", session.getUserID());
            }

            return new BaseResponse<>(Constants.SUCCESS, false, request.getAction(), "Login failed! Invalid username or password.");
        } catch (Exception e) {
            log.error("Failed to check login", e);
            return new BaseResponse<>(Constants.INTERNAL_SERVER_ERROR, false, request.getAction(), "Failed to check login info.");
        }
    }

    @EndPoint(value = "/logout")
    public BaseResponse<?> logout(UserSession session, BaseRequest request) {
        if (Objects.nonNull(session)) {
            SessionManager.getInstance().invalidSession(session.getUserID());
            return new BaseResponse<>(Constants.SUCCESS, true, request.getAction(), "Logout successfully!");
        }

        return new BaseResponse<>(Constants.BAD_REQUEST, false, request.getAction(), "Logout failed! Not found session");
    }
}

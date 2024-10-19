package main.java.math.server.controller;

import com.google.gson.Gson;
import main.java.math.server.dto.request.UserRequest;
import main.java.math.server.dto.response.BaseResponse;
import main.java.math.server.model.User;
import main.java.math.server.router.EndPoint;
import main.java.math.server.router.RouterMapping;
import main.java.math.server.service.impl.UserService;
import main.java.math.server.service.utils.SessionManager;
import main.java.math.server.service.utils.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@EndPoint(value = "/user")
@SuppressWarnings("unused")
public class UserController implements RouterMapping {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    @EndPoint(value = "/login")
    public BaseResponse<?> login(String jsonRequest) {
        if (Objects.nonNull(jsonRequest)) {
            try {
                Gson gson = new Gson();
                UserRequest userRequest = gson.fromJson(jsonRequest, UserRequest.class);
                User user = new User(userRequest.getUsername(), userRequest.getPassword());
                boolean isSuccess = userService.checkLogin(user);

                if (isSuccess) {
                    UserSession session = SessionManager.getInstance().getSession();    // Create new session for this user
                    session.setUsername(user.getUsername());
                    session.setLoginState(true);

                    return new BaseResponse<>(200, true, "/login", "Login successfully!", session.getUserID());
                }

                return new BaseResponse<>(200, false, "/login", "Login failed! Invalid username or password.");
            } catch (Exception e) {
                log.error("Failed to check login: " + e.getMessage());
                return new BaseResponse<>(500, false, "/login", "Failed to check login info.");
            }
        }

        return new BaseResponse<>(400, false,"Missing data from request!");
    }

    @EndPoint(value = "/logout")
    public BaseResponse<?> logout(String userID) {
        if (Objects.nonNull(userID)) {
            SessionManager.getInstance().invalidSession(userID);

            return new BaseResponse<>(200, true, "/logout", "Logout successfully!");
        }

        return new BaseResponse<>(400, false, "/logout", "Logout failed! Please provide your ID.");
    }
}

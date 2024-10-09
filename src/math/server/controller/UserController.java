package math.server.controller;

import com.google.gson.Gson;
import math.server.dto.request.UserRequest;
import math.server.dto.response.BaseResponse;
import math.server.router.EndPoint;
import math.server.router.RouterMapping;

@EndPoint(value = "/user")
@SuppressWarnings("unused")
public class UserController implements RouterMapping {

    @EndPoint(value = "/login")
    public BaseResponse<?> login(String jsonRequest) {
        Gson gson = new Gson();
        UserRequest userRequest = gson.fromJson(jsonRequest, UserRequest.class);

        return new BaseResponse<>(200, "Login successfully");
    }

    @EndPoint(value = "/logout")
    public void logout() {

    }

    @EndPoint(value = "/info")
    public BaseResponse<?> getInfo(String jsonRequest) {
        return new BaseResponse<>(200, "Get user info successfully");
    }
}

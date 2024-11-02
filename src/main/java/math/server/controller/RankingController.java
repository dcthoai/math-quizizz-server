package math.server.controller;

import com.google.gson.Gson;
import math.server.dto.request.BaseRequest;
import math.server.dto.response.BaseResponse;
import math.server.dto.response.RankDTO;
import math.server.router.EndPoint;
import math.server.router.RouterMapping;
import math.server.service.impl.RankingService;
import math.server.service.utils.UserSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@EndPoint("/api/ranking")
@SuppressWarnings("unused")
public class RankingController implements RouterMapping {

    private static final Logger log = LoggerFactory.getLogger(RankingController.class);
    private final RankingService rankingService;
    private final Gson gson = new Gson();

    public RankingController() {
        this.rankingService = new RankingService();
    }

    @EndPoint("/all")
    public BaseResponse getAllRanking(UserSession session, BaseRequest request) {
        log.debug("Socket request to get all users rank. EndPoint: /api/ranking/all");
        List<RankDTO> rankDTOS = rankingService.getAllUserRanks();
        return new BaseResponse(request.getAction(), gson.toJson(rankDTOS));
    }
}

package math.server.repository.impl;

import math.server.common.Constants;
import math.server.dto.response.FriendShipDTO;
import math.server.entity.FriendShip;
import math.server.repository.IFriendShipRepository;
import math.server.repository.utils.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class FriendShipRepository extends EntityManager<FriendShip> implements IFriendShipRepository {


    @Override
    public List<FriendShip> getFriendShipByStatus(Integer userID, Integer status) {
        String sql = "SELECT * " +
                     "FROM `friendship` f " +
                     "JOIN `user` u ON f.friendID = u.ID " +
                     "WHERE u.ID = ? AND f.status = ? ";

        return query(sql, List.of(userID, status));
    }

    @Override
    public List<FriendShipDTO> getAllFriend(Integer userID) {
        String sql = "SELECT " +
                     "    f.ID AS ID, " +
                     "    f.status, " +
                     "    u1.username AS username, " +
                     "    u2.username AS friendName " +
                     "FROM friendship f " +
                     "JOIN user u1 ON u1.ID = ? " +
                     "JOIN user u2 ON u2.ID = ( " +
                     "    CASE " +
                     "        WHEN f.userID = ? THEN f.friendID " +
                     "        WHEN f.friendID = ? THEN f.userID " +
                     "    END " +
                     ") " +
                     "WHERE (f.userID = ? OR f.friendID = ?) AND f.status = " + Constants.FRIENDSHIP_ACCEPTED;

        List<Object> params = new ArrayList<>();

        for (int i = 0; i < 5; ++i)
            params.add(userID);

        return query(sql, params, FriendShipDTO.class);
    }
}

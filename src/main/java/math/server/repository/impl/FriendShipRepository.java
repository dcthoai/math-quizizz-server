package math.server.repository.impl;

import math.server.common.Constants;
import math.server.dto.response.UserDTO;
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
    public List<UserDTO> getAllFriend(Integer userID) {
        String sql = "SELECT " +
                     "    u2.ID as `ID`, " +
                     "    u2.username as `username`, " +
                     "    u2.score as `score`, " +
                     "    u2.rank AS `rank` " +
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

        return query(sql, params, UserDTO.class);
    }
}

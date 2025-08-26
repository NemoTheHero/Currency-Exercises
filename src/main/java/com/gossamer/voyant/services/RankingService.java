package com.gossamer.voyant.services;

import com.gossamer.voyant.dao.UserUserScoreDao;
import com.gossamer.voyant.entities.User;
import com.gossamer.voyant.entities.UserUserScore;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RankingService {

    UserUserScoreDao userUserScoreDao;

    public RankingService(UserUserScoreDao userUserScoreDao) {
        this.userUserScoreDao = userUserScoreDao;
    }

    public Optional<UserUserScore> getUserUserScoreForUsers(User user1, User user2) {
        return userUserScoreDao.findUserUserScoreByUser1IdOrUser2Id(user1.getId(),user2.getId());
    }


//
//    public saveUserUserScore(User user1, User user2, Long score) {
//
//    }



}

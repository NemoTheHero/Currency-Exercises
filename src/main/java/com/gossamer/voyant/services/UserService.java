package com.gossamer.voyant.services;

import com.gossamer.voyant.dao.KeywordsDao;
import com.gossamer.voyant.dao.UserDao;
import com.gossamer.voyant.dao.UserKeywordsDao;
import com.gossamer.voyant.dao.UserUserScoreDao;
import com.gossamer.voyant.entities.Keywords;
import com.gossamer.voyant.entities.User;
import com.gossamer.voyant.entities.UserKeywords;
import com.gossamer.voyant.entities.UserUserScore;
import com.gossamer.voyant.model.UserScore;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.apache.commons.lang3.math.NumberUtils.min;

@Service
public class UserService {

    private final UserDao userDao;
    private final UserUserScoreDao userUserScoreDao;
    private final UserKeywordsDao userKeywordsDao;
    private final KeywordsDao keywordsDao;
    private final RankingService rankingService;

    public UserService(UserDao userDao, UserKeywordsDao userKeywordsDao, KeywordsDao keywordsDao, UserUserScoreDao userUserScoreDao, RankingService rankingService) {
        this.userDao = userDao;
        this.userKeywordsDao = userKeywordsDao;
        this.keywordsDao = keywordsDao;
        this.userUserScoreDao = userUserScoreDao;
        this.rankingService = rankingService;
    }
    public Optional<User> getUser(Long userId) {

        return userDao.findById(userId);
    }

    public List<Keywords> getInterests(Long userId) {
        List<UserKeywords> userKeywords = userKeywordsDao.findUserKeywordsByUserId(userId);
        List<Keywords> interests = new ArrayList<>();
        for (UserKeywords userKeyword : userKeywords) {
            keywordsDao.findById(userKeyword.getKeywordId()).ifPresent(interests::add);
        }
        return interests;
    }

    public List<UserKeywords> findUserKeywordsByKeyword(Long userKeywordsId) {
        return userKeywordsDao.findUserKeywordsByKeywordId(userKeywordsId);
    }
    public List<User> findAllUsersByKeyword(Long userKeywordsId) {
        List<UserKeywords> findUserKeywordsByKeyword = userKeywordsDao.findUserKeywordsByKeywordId(userKeywordsId);
        List<User> users = new ArrayList<>();
        for (UserKeywords userKeywords : findUserKeywordsByKeyword) {
            userDao.findById(userKeywords.getUserId()).ifPresent(users::add);
        }
        return users;
    }

    public List<UserScore> getMatchesForUserScoreDesc(Long userId) {
        List<UserUserScore> userUserScores = userUserScoreDao.findUserUserScoreByUser1IdOrUser2IdOrderByScoreDesc(userId, userId);
        List<UserScore> userScores = new ArrayList<>();

        for (UserUserScore userUserScore : userUserScores) {
            Long otherUserId = userUserScore.getUser1Id();
            if (userId.equals(otherUserId)) {
                otherUserId = userUserScore.getUser2Id();
            }
            User user = userDao.findById(otherUserId).orElse(null);
            if (user != null) {
                userScores.add(UserScore.builder().userId(otherUserId).name(user.getUserName()).score(userUserScore.getScore()).build());

            }
        }
        return userScores;
    }

    private void updateRankingsForUser(User user) {
        List<UserKeywords> userKeywords = userKeywordsDao.findUserKeywordsByUserId(user.getId());
        rankingService.updateAllRankingsForUser(user, userKeywords);
    }

}

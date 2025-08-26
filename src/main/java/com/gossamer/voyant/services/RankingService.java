package com.gossamer.voyant.services;

import com.gossamer.voyant.dao.UserKeywordsDao;
import com.gossamer.voyant.dao.UserUserScoreDao;
import com.gossamer.voyant.entities.User;
import com.gossamer.voyant.entities.UserKeywords;
import com.gossamer.voyant.entities.UserUserScore;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.apache.commons.lang3.math.NumberUtils.min;

@Service
public class RankingService {

    UserUserScoreDao userUserScoreDao;
    UserKeywordsDao userKeywordsDao;

    public RankingService(UserUserScoreDao userUserScoreDao, UserKeywordsDao userKeywordsDao) {
        this.userUserScoreDao = userUserScoreDao;
        this.userKeywordsDao = userKeywordsDao;
    }

    public Optional<UserUserScore> getUserUserScoreForUsers(User user1, User user2) {
        return userUserScoreDao.findUserUserScoreByUser1IdOrUser2Id(user1.getId(),user2.getId());
    }


    public void updateAllRankingsForUser(User user, List<UserKeywords> userKeywords) {
        Map<Long, Long> userMatchRanking = new HashMap<>();
        // find users who like each keyword the user submits and start creating the shared interest ranking
        for (UserKeywords userKeyword : userKeywords) {
            List<UserKeywords> userKeywordsList =
                    userKeywordsDao.findUserKeywordsByKeywordIdAndUserIdIsNot(userKeyword.getKeywordId(),user.getId());
            for (UserKeywords otherKeyWord : userKeywordsList) {
                //compare the user keyword with the other user and take the lowest value
                Long sharedInterestValue = min(userKeyword.getScore(), otherKeyWord.getScore());
                if (userMatchRanking.containsKey(otherKeyWord.getUserId())) {
                    // add value to the existing value
                    userMatchRanking.put(otherKeyWord.getUserId(), userMatchRanking.get(otherKeyWord.getUserId()) + sharedInterestValue);
                } else {
                    userMatchRanking.put(otherKeyWord.getUserId(), sharedInterestValue);
                }
            }
        }
        for (Map.Entry<Long, Long> pair : userMatchRanking.entrySet()) {
            Long otherUserId = pair.getKey();
            Long sharedInterestValue = pair.getValue();
            Long userId1 = user.getId();
            Long userId2 = otherUserId;
            // store the lower userId in column userId1 to avoid having duplicate inverse columns
            // ex. we do not want 1 | 2 and 2 | 1 in the table
            if (user.getId() > otherUserId) {
                userId1 = otherUserId;
                userId2 = user.getId();
            }

            Optional<UserUserScore> existing = userUserScoreDao.findUserUserScoreByUser1IdOrUser2Id(userId1,userId2);

            if (existing.isPresent()) {
                existing.get().setScore(sharedInterestValue);
                userUserScoreDao.save(existing.get());
            } else {
                userUserScoreDao.save(UserUserScore.builder()
                        .user1Id(userId1)
                        .user2Id(userId2).score(sharedInterestValue).build());

            }
        }
    }





}

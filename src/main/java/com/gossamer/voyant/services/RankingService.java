package com.gossamer.voyant.services;

import com.gossamer.voyant.dao.UserKeywordsDao;
import com.gossamer.voyant.dao.UserUserScoreDao;
import com.gossamer.voyant.entities.User;
import com.gossamer.voyant.entities.UserKeywords;
import com.gossamer.voyant.entities.UserUserScore;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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


    public void updateAllRankingsForUser(User user1, List<UserKeywords> userKeywords) {
        Map<Long, Long> userMatchRanking = new HashMap<>();
        // find users who like each keyword the user submits and start creating the shared interest ranking
        for (UserKeywords userKeyword : userKeywords) {
            List<UserKeywords> userKeywordsList = userKeywordsDao.findUserKeywordsByKeywordId(userKeyword.getKeywordId());
            for (UserKeywords otherUser : userKeywordsList) {
                //compare the user keyword with the other user and take the lowest value
                Long sharedInterestValue = min(userKeyword.getScore(), otherUser.getScore());
                if (userMatchRanking.containsKey(otherUser.getUserId())) {
                    // add value to the existing value
                    userMatchRanking.put(otherUser.getUserId(), userMatchRanking.get(otherUser.getUserId()) + sharedInterestValue);
                } else {
                    userMatchRanking.put(otherUser.getUserId(), sharedInterestValue);
                }
            }
        }
        for (Map.Entry<Long, Long> pair : userMatchRanking.entrySet()) {
            Long otherUserId = pair.getKey();
            Long sharedInterestValue = pair.getValue();
            Long userId1 = user1.getId();
            Long userId2 = otherUserId;
            // store the lower userId in column userId1 to avoid having duplicate inverse columns
            // ex. we do not want 1 | 2 and 2 | 1 in the table
            if (user1.getId() > otherUserId) {
                userId2 = user1.getId();
                userId1 = otherUserId;
            }

            UserUserScore userUserScore = UserUserScore.builder()
                    .user1Id(userId1)
                    .user2Id(userId2).score(sharedInterestValue).build();

            userUserScoreDao.save(userUserScore);
        }
    }





}

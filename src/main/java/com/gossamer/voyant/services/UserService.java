package com.gossamer.voyant.services;

import com.gossamer.voyant.dao.KeywordsDao;
import com.gossamer.voyant.dao.UserDao;
import com.gossamer.voyant.dao.UserKeywordsDao;
import com.gossamer.voyant.dao.UserUserScoreDao;
import com.gossamer.voyant.entities.Keyword;
import com.gossamer.voyant.entities.KeywordDTO;
import com.gossamer.voyant.entities.User;
import com.gossamer.voyant.entities.UserKeywords;
import com.gossamer.voyant.entities.UserUserScore;

import com.gossamer.voyant.model.UserScore;
import org.springframework.stereotype.Service;

import java.util.*;


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

    public List<Keyword> getInterests(Long userId) {
        List<UserKeywords> userKeywords = userKeywordsDao.findUserKeywordsByUserId(userId);
        List<Keyword> interests = new ArrayList<>();
        for (UserKeywords userKeyword : userKeywords) {
            keywordsDao.findById(userKeyword.getKeywordId()).ifPresent(interests::add);
        }
        return interests;
    }

    public List<UserKeywords> findUserKeywordsByKeyword(Long userKeywordsId) {
        return userKeywordsDao.findUserKeywordsByKeywordId(userKeywordsId);
    }

    public List<UserScore> getUserInterestsByUserId(Long userId) {
        List<UserScore> userScores = new ArrayList<>();
        User user = userDao.findById(userId).orElse(null);
        if (user == null) {
            return new ArrayList<>();
        }
        List<UserKeywords> userKeywords = userKeywordsDao.findUserKeywordsByUserId(userId);

        userKeywords.forEach(userKeyword -> {
            Optional<Keyword> keyword = keywordsDao.findById(userKeyword.getKeywordId());

            keyword.ifPresent(value -> userScores.add(UserScore.builder().userId(user.getId()).score(userKeyword.getScore()).name(value.getKeyword()).build()));
        });

        return userScores;
    }
    public List<UserScore> findAllUsersByKeyword(Long userKeywordsId) {
        List<UserKeywords> findUserKeywordsByKeyword = userKeywordsDao.findUserKeywordsByKeywordId(userKeywordsId);
        List<UserScore> users = new ArrayList<>();
        for (UserKeywords userKeyword : findUserKeywordsByKeyword) {
            userDao.findById(userKeyword.getUserId()).ifPresent(user ->
                    users.add(UserScore.builder().name(user.getUserName()).userId(userKeyword.getUserId()).score(userKeyword.getScore()).build()));
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


    public void addInterests(Long userId, List<KeywordDTO> keywords) {
        List<UserKeywords> userKeywordsList = new ArrayList<>();
        List<UserKeywords> existingUserKeywords = userKeywordsDao.findUserKeywordsByUserId(userId);

        for ( KeywordDTO keywordDTO : keywords) {
            Optional<Keyword> keywordOpt = keywordsDao.findByKeyword(keywordDTO.getKeyword());

            if (keywordOpt.isPresent()) {
                UserKeywords existingItem = existingUserKeywords.stream()
                        .filter(uk -> uk.getKeywordId().equals(keywordOpt.get().getId()))
                        .findFirst()
                        .orElse(null);

                if (existingItem != null) {
                    if (existingItem.getScore() < keywordDTO.getScore()) {
                        existingItem.setScore((long)keywordDTO.getScore());
                        userKeywordsList.add(existingItem);
                    }
                } else {
                    UserKeywords existingElem = userKeywordsList.stream().filter(o -> o.getKeywordId().equals(keywordOpt.get().getId())).findFirst().orElse(null);
                    if (existingElem != null && existingElem.getScore() < keywordDTO.getScore()) {
                        existingElem.setScore((long)keywordDTO.getScore());
                    } else if (existingElem == null) {
                        UserKeywords userKeywords = UserKeywords.builder()
                                .userId(userId)
                                .keywordId(keywordOpt.get().getId())
                                .score((long)keywordDTO.getScore())
                                .build();
                        userKeywordsList.add(userKeywords);
                    }
                }
            }
        }
        userKeywordsDao.saveAll(userKeywordsList);
        User user = userDao.findById(userId).orElse(null);
        updateRankingsForUser(user);
    }

}

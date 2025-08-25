package com.gossamer.voyant.services;

import com.gossamer.voyant.dao.UserDao;
import com.gossamer.voyant.dao.UserKeywordsDao;
import com.gossamer.voyant.entities.User;
import com.gossamer.voyant.entities.UserKeywords;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;
    private final UserKeywordsDao userKeywordsDao;

    public UserService(UserDao userDao, UserKeywordsDao userKeywordsDao) {
        this.userDao = userDao;
        this.userKeywordsDao = userKeywordsDao;
    }
    public Optional<User> getUser(Long userId) {

        return userDao.findById(userId);
    }

    public List<UserKeywords> findUserKeywordsByKeyword(Long userKeywordsId) {
        return userKeywordsDao.findUserKeywordsByKeywordId(userKeywordsId);
    }
}

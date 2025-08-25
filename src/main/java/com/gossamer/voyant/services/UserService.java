package com.gossamer.voyant.services;

import com.gossamer.voyant.dao.UserDao;
import com.gossamer.voyant.entities.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }
    public Optional<User> getUser(Long userId) {

        return userDao.findById(userId);
    }
}

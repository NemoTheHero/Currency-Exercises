package com.gossamer.voyant.controllers;

import com.gossamer.voyant.entities.User;
import com.gossamer.voyant.entities.UserKeywords;
import com.gossamer.voyant.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/findById")
    Optional<User> getUser(@RequestParam Long userId) {
        return userService.getUser(userId) ;
    }

    @GetMapping("/getUsersKeywords")
    List<UserKeywords> getUsersKeywords(@RequestParam Long keywordId) {
        return userService.findUserKeywordsByKeyword(keywordId) ;
    }
}

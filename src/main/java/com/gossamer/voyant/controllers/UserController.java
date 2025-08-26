package com.gossamer.voyant.controllers;

import com.gossamer.voyant.entities.KeywordDTO;
import com.gossamer.voyant.entities.User;
import com.gossamer.voyant.entities.UserKeywords;
import com.gossamer.voyant.model.UserInterest;
import com.gossamer.voyant.services.KeywordService;
import com.gossamer.voyant.model.UserScore;
import com.gossamer.voyant.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final KeywordService keywordService;

    public UserController(UserService userService, KeywordService keywordService) {
        this.keywordService = keywordService;
        this.userService = userService;
    }

    @GetMapping("/findById")
    Optional<User> getUser(@RequestParam Long userId) {
        return userService.getUser(userId) ;
    }

    @GetMapping("/interests")
    List<UserInterest> getUserInterests(@RequestParam Long userId) {
        return userService.getUserInterestsByUserId(userId) ;
    }

    @GetMapping("/getUsersKeywords")
    List<UserKeywords> getUsersKeywords(@RequestParam Long keywordId) {
        return userService.findUserKeywordsByKeyword(keywordId) ;
    }

    @GetMapping("/getAllUsersScoresByKeywordId")
    List<UserScore> getAllUsersByKeywords(@RequestParam Long keywordId) {
        return userService.findAllUsersByKeyword(keywordId) ;
    }

    @GetMapping("/getMatches")
    List<UserScore> getMatches(@RequestParam Long userId) {
        return userService.getMatchesForUserScoreDesc(userId) ;
    }

    @PostMapping("/addInterests")
    void addInterests(@RequestParam Long userId, @RequestBody List<KeywordDTO> keywords) {
        List<String> keywordNames = keywords.stream().map(keywordDTO -> keywordDTO.getKeyword().toLowerCase())
                .toList();
        keywordService.addNewKeywords(keywordNames);
        userService.addInterests(userId, keywords);
    }

}

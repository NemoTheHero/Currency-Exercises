package com.gossamer.voyant.controllers;

import com.gossamer.voyant.entities.User;
import com.gossamer.voyant.services.UserRecommendationsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("recommendations")
public class UserRecsController {
    private final UserRecommendationsService userRecommendationsService;
    public UserRecsController(UserRecommendationsService userRecommendationsService) {
        this.userRecommendationsService = userRecommendationsService;
    }

    @GetMapping("/hobby")
    String getUser(@RequestParam String hobby) throws IOException, InterruptedException {
        return userRecommendationsService.getUserRecsFromChatGpt(hobby) ;
    }
}

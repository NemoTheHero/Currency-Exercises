package com.gossamer.voyant.services;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserRecommendationsService {

    ChatGPTService chatGPTService;

    UserRecommendationsService(ChatGPTService chatGPTService) {
        this.chatGPTService = chatGPTService;
    }

    String promptString =
            "give me the top 4 hobbies that correlate to %s. from those 4 give me the top 4 hobbies that correlate to each one. Structure the graph in json format. each object other than the last one should be wrapped in a correlated_hobbies.";

    String getStringPrompt(String hobby) {
        return String.format(promptString, hobby);
    }

    public String getUserRecsFromChatGpt(String hobby) throws IOException, InterruptedException {
        return chatGPTService.sendPromptAndGetJson(getStringPrompt(hobby));
    }
}

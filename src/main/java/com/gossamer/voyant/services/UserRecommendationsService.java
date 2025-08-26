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
            "In json format, give me the top 4 hobbies that correlate to %s, for each result give me the top 4 hobbies for each result. each result should have an object called correlated_hobbies. only return the json in this format\n" +
                    "{\n" +
                    "    \"%s\": {\n" +
                    "        \"correlated_hobbies\": [\n" +
                    "            {\n" +
                    "                \"name\": \"result\",\n" +
                    "                \"correlated_hobbies\": [\n" +
                    "                    {\n" +
                    "                        \"name\": \"result\"\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"name\": \"result\"\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"name\": \"result\"\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"name\": \"result\"\n" +
                    "                    }\n" +
                    "                ]\n" +
                    "            }\n" +
                    "}\n";

    String getStringPrompt(String hobby) {
        return String.format(promptString, hobby, hobby.replaceAll(" ", "_"));
    }

    public String getUserRecsFromChatGpt(String hobby) throws IOException, InterruptedException {
        return chatGPTService.getChatReply(getStringPrompt(hobby));
    }
}

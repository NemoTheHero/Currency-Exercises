package com.gossamer.voyant.services;

public class UserRecommendationsService {

    String chatGipitiUrl = "url";
    String apiKey = "key";

    String promptString =
            "give me the top 4 hobbies that correlate to %s. from those 4 give me the top 4 hobbies that correlate to each one. Structure the graph in json format. each object other than the last one should be wrapped in a correlated_hobbies.";

    String getStringPrompt(String hobby) {
        return String.format(promptString, hobby);
    }

    String getJsonPrompt(String hobby) {
        return String.format(promptString, hobby);
    }
}

package com.gossamer.voyant.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class ChatGPTService {

    private static final String API_URL = "https://rgras-merc5ve7-westeurope.cognitiveservices.azure.com/openai/deployments/gpt-4o/chat/completions?api-version=2025-01-01-preview";
    private static final String API_KEY = ""; // or hardcode (not recommended)

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ChatGPTService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public String getChatReply(String userMessage) throws IOException, InterruptedException {
        // Prepare the request body
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-5",
                "messages", List.of(
                        Map.of("role", "user", "content", userMessage)
                ),
                "temperature", 0.7
        );

        String jsonRequest = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return extractAssistantMessage(response.body());
        } else {
            throw new IOException("OpenAI API error: " + response.statusCode() + "\n" + response.body());
        }
    }

    private String extractAssistantMessage(String responseBody) throws IOException {
        Map<?, ?> fullResponse = objectMapper.readValue(responseBody, Map.class);
        List<?> choices = (List<?>) fullResponse.get("choices");

        if (choices != null && !choices.isEmpty()) {
            Map<?, ?> choice = (Map<?, ?>) choices.get(0);
            Map<?, ?> message = (Map<?, ?>) choice.get("message");
            return (String) message.get("content");
        }

        return "No response from ChatGPT.";
    }

}
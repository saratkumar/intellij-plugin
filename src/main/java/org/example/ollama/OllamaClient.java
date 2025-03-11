package org.example.ollama;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;

public class OllamaClient {
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private static final OkHttpClient client = new OkHttpClient();

    public static String analyzeCode(String code) {
        String jsonRequest = "{ \"model\": \"mistral\", \"prompt\": \"Analyze this Java code and suggest improvements: " + code + "\", \"stream\": false }";
        RequestBody body = RequestBody.create(jsonRequest, MediaType.get("application/json"));
        Request request = new Request.Builder().url(OLLAMA_URL).post(body).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Error: Could not connect to Ollama";
    }
}

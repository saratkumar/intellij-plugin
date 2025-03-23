package org.example.ollama;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OllamaClient {
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
//    private static final OkHttpClient client = new OkHttpClient();
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();
    public static String analyzeCode(String code) {
        String jsonRequest = "{ \"model\": \"tinyllama\", \"prompt\": \"Is there any issue with this Java code and suggest improvements: " + escapeRequestPayload(code) + "\", \"stream\": false }";
        RequestBody body = RequestBody.create(jsonRequest, MediaType.get("application/json"));
        Request request = new Request.Builder().url(OLLAMA_URL).post(body).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                String responseBody =  response.body().string();
                // Parse JSON string to a JSON object
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                // Extract a specific field from the JSON response
                String result = jsonObject.get("response").getAsString();

                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Error: Could not connect to Ollama";
    }

    private static String escapeRequestPayload(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\\", "\\\\")   // Escape backslashes
                .replace("\"", "\\\"")   // Escape double quotes
                .replace("\n", "\\n");   // Escape newline characters
    }


}

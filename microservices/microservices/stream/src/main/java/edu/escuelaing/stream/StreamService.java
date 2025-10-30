package edu.escuelaing.stream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class StreamService implements RequestHandler<Map<String, Object>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String POST_SERVICE_URL = "https://1ej3zap597.execute-api.us-east-1.amazonaws.com/Beta/posts";  // Gateway URL para /posts (POST for get)
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public String handleRequest(Map<String, Object> input, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Input: " + input.toString());

        try {
            String action = (String) input.get("action");
            if (action == null) {
                throw new IllegalArgumentException("Missing 'action' in input");
            }

            if ("get".equals(action)) {
                // HttpClient POST to post-service for posts (usa POST con body {action: "get"})
                Map<String, Object> getPostsBody = new HashMap<>();
                getPostsBody.put("action", "get");
                String postsBodyJson = objectMapper.writeValueAsString(getPostsBody);

                HttpRequest postsRequest = HttpRequest.newBuilder()
                        .uri(URI.create(POST_SERVICE_URL))
                        .POST(HttpRequest.BodyPublishers.ofString(postsBodyJson))
                        .header("Content-Type", "application/json")
                        .build();
                HttpResponse<String> postsResponse = httpClient.send(postsRequest, HttpResponse.BodyHandlers.ofString());

                if (postsResponse.statusCode() != 200) {
                    throw new RuntimeException("Failed to fetch posts from post-service (status: " + postsResponse.statusCode() + ")");
                }

                // Parse posts array from response (handle double-stringified if needed)
                com.fasterxml.jackson.databind.JsonNode node = objectMapper.readTree(postsResponse.body());
                String innerBody = node.asText();
                if (innerBody == null || innerBody.isEmpty()) {
                    innerBody = postsResponse.body();
                }
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> posts = objectMapper.readValue(innerBody, List.class);

                // Sort por timestamp desc
                posts.sort((p1, p2) -> {
                    String ts1 = (String) p1.get("timestamp");
                    String ts2 = (String) p2.get("timestamp");
                    if (ts1 == null && ts2 == null) return 0;
                    if (ts1 == null) return 1;
                    if (ts2 == null) return -1;
                    return ts2.compareTo(ts1);
                });

                // Wrap in global stream (in-memory)
                Map<String, Object> stream = new HashMap<>();
                stream.put("id", 1L);
                stream.put("name", "Global Feed");
                stream.put("posts", posts);

                logger.log("Stream retrieved with " + posts.size() + " posts");
                return objectMapper.writeValueAsString(stream);  // JSON string del stream
            } else {
                throw new IllegalArgumentException("Invalid action: " + action + ". Use 'get'");
            }
        } catch (Exception e) {
            logger.log("Error: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            try {
                return objectMapper.writeValueAsString(error);
            } catch (Exception ex) {
                return "{\"error\": \"Internal server error\"}";
            }
        }
    }
}
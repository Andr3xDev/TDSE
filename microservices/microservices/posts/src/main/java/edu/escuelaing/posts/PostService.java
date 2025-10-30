package edu.escuelaing.posts;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class PostService implements RequestHandler<Map<String, Object>, String> {

    private static final String TABLE_NAME = "PostsTable";  // Tu tabla DynamoDB
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final DynamoDbClient dynamoDbClient = DynamoDbClient.create();
    private static final DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
    private static final DynamoDbTable<Post> postTable = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(Post.class));
    private static final String USER_SERVICE_URL = "https://1ej3zap597.execute-api.us-east-1.amazonaws.com/Beta/users";  // Gateway URL
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

            if ("create".equals(action)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> postData = (Map<String, Object>) input.get("post");
                String cognitoId = (String) input.get("cognitoId");

                if (postData == null || !postData.containsKey("content") || cognitoId == null) {
                    throw new IllegalArgumentException("Missing required fields: post.content, cognitoId");
                }

                String content = (String) postData.get("content");
                if (content.length() > 140) {
                    throw new IllegalArgumentException("Post exceeds 140 characters");
                }

                // HttpClient POST to user-service for validation/fetch
                Map<String, Object> getUserBody = new HashMap<>();
                getUserBody.put("action", "get");
                getUserBody.put("cognitoId", cognitoId);
                String userBodyJson = objectMapper.writeValueAsString(getUserBody);

                HttpRequest userRequest = HttpRequest.newBuilder()
                        .uri(URI.create(USER_SERVICE_URL))
                        .POST(HttpRequest.BodyPublishers.ofString(userBodyJson))
                        .header("Content-Type", "application/json")
                        .build();
                HttpResponse<String> userResponse = httpClient.send(userRequest, HttpResponse.BodyHandlers.ofString());

                if (userResponse.statusCode() != 200) {
                    throw new RuntimeException("User not found with cognitoId: " + cognitoId + " (status: " + userResponse.statusCode() + ")");
                }

                // Parse user (handle double-stringified if needed)
                com.fasterxml.jackson.databind.JsonNode node = objectMapper.readTree(userResponse.body());
                String innerBody = node.asText();
                if (innerBody == null || innerBody.isEmpty()) {
                    innerBody = userResponse.body();
                }
                @SuppressWarnings("unchecked")
                Map<String, Object> user = objectMapper.readValue(innerBody, Map.class);

                // **NUEVO: Check for error in user response or missing cognitoId**
                if (user.containsKey("error") || user.get("cognitoId") == null) {
                    throw new RuntimeException("User not found with cognitoId: " + cognitoId + " (response: " + userResponse.body() + ")");
                }

                // Crea post con flattened user
                Post post = new Post();
                post.setId(String.valueOf(System.currentTimeMillis()));  // Unique ID as string
                post.setContent(content);
                post.setTimestamp(Instant.now().toString());
                post.setUserCognitoId((String) user.get("cognitoId"));
                post.setUserUsername((String) user.get("username"));
                post.setUserEmail((String) user.get("email"));

                // Save to DynamoDB
                postTable.putItem(post);

                // Response as Map for JSON
                Map<String, Object> responsePost = new HashMap<>();
                responsePost.put("id", post.getId());
                responsePost.put("content", post.getContent());
                responsePost.put("timestamp", post.getTimestamp());
                responsePost.put("user", user);  // Full user Map for response

                logger.log("Post created: ID " + post.getId() + " for user " + cognitoId);
                return objectMapper.writeValueAsString(responsePost);
            } else if ("get".equals(action)) {
                // Scan all posts and reconstruct with null-safe checks
                ScanRequest scanRequest = ScanRequest.builder().tableName(TABLE_NAME).build();
                ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);

                List<Map<String, Object>> allPosts = scanResponse.items().stream()
                        .filter(item -> item.containsKey("content"))  // Filter invalid items
                        .map(item -> {
                            Map<String, Object> postMap = new HashMap<>();
                            postMap.put("id", getSafeString(item, "id"));
                            postMap.put("content", getSafeString(item, "content"));
                            postMap.put("timestamp", getSafeString(item, "timestamp"));

                            // Reconstruct user Map from flattened fields with null-safe
                            Map<String, Object> user = new HashMap<>();
                            user.put("cognitoId", getSafeString(item, "userCognitoId"));
                            user.put("username", getSafeString(item, "userUsername"));
                            user.put("email", getSafeString(item, "userEmail"));
                            postMap.put("user", user);

                            return postMap;
                        })
                        .collect(Collectors.toList());

                // Sort desc by timestamp (skip if null)
                allPosts.sort((p1, p2) -> {
                    String ts1 = (String) p1.get("timestamp");
                    String ts2 = (String) p2.get("timestamp");
                    if (ts1 == null && ts2 == null) return 0;
                    if (ts1 == null) return 1;
                    if (ts2 == null) return -1;
                    return ts2.compareTo(ts1);
                });

                logger.log("Retrieved " + allPosts.size() + " posts");
                return objectMapper.writeValueAsString(allPosts);
            } else {
                throw new IllegalArgumentException("Invalid action: " + action + ". Use 'create' or 'get'");
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

    // Helper for null-safe AttributeValue.s()
    private String getSafeString(Map<String, AttributeValue> item, String key) {
        AttributeValue av = item.get(key);
        if (av == null) return null;
        return av.s();  // s() returns null if not String or empty
    }
}
package edu.escuelaing.users;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.HashMap;
import java.util.Map;

public class UserService implements RequestHandler<Map<String, Object>, String> {

    private static final String TABLE_NAME = "UsersTable";  // Tu tabla DynamoDB
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static DynamoDbClient dynamoDbClient = DynamoDbClient.create();
    private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
    private static DynamoDbTable<User> userTable = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(User.class));

    @Override
    public String handleRequest(Map<String, Object> input, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Input: " + input.toString());

        try {
            String action = (String) input.get("action");
            Map<String, Object> response = new HashMap<>();

            if ("create".equals(action)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> userData = (Map<String, Object>) input.get("user");

                if (userData == null || !userData.containsKey("cognitoId") || !userData.containsKey("username") || !userData.containsKey("email")) {
                    throw new IllegalArgumentException("Missing required fields: cognitoId, username, email");
                }

                User user = new User();
                user.setCognitoId((String) userData.get("cognitoId"));
                user.setUsername((String) userData.get("username"));
                user.setEmail((String) userData.get("email"));

                // Check if exists
                userTable.getItem(r -> r.key(k -> k.partitionValue(user.getCognitoId())));

                // Save to DynamoDB
                userTable.putItem(user);
                logger.log("User created: " + user.getCognitoId());

                return objectMapper.writeValueAsString(user);
            } else if ("get".equals(action)) {
                String cognitoId = (String) input.get("cognitoId");

                User user = userTable.getItem(r -> r.key(k -> k.partitionValue(cognitoId)));
                if (user == null) {
                    throw new RuntimeException("User not found with cognitoId: " + cognitoId);
                }

                logger.log("User retrieved: " + cognitoId);
                return objectMapper.writeValueAsString(user);
            } else {
                throw new IllegalArgumentException("Invalid action: " + action + ". Use 'create' or 'get'");
            }
        } catch (Exception e) {
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
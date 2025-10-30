package edu.escuelaing.users;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class User {
    private String cognitoId;
    private String username;
    private String email;

    @DynamoDbPartitionKey
    public String getCognitoId() { return cognitoId; }
    public void setCognitoId(String cognitoId) { this.cognitoId = cognitoId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
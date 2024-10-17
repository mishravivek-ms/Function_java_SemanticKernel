package com.aiaudit.function;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with HTTP Trigger.
 */
public class httpTriggerJavaStubAudit {
    /**
     * This function listens at endpoint "/api/HttpTriggerJavaStubAudit". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpTriggerJavaStubAudit
     * 2. curl {your host}/api/HttpTriggerJavaStubAudit?name=HTTP%20Query
     */
    @FunctionName("HttpTriggerJavaStubAudit")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Path to your JSON file
        // String filePath = "audit.json";
        String jsonResponse = "{\"message\": \"Hello, World!\"}";     
        // String jsonResponse;
        // try {
        //     // Read file content into a string
        //     jsonResponse = new String(Files.readAllBytes(Paths.get(filePath)));
        // } catch (IOException e) {
        //     context.getLogger().severe("Failed to read the file: " + e.getMessage());
        //     return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
        //                   .body("Failed to read the file.")
        //                   .build();
        // }
     
        return request.createResponseBuilder(HttpStatus.OK)
                      .header("Content-Type", "application/json")
                      .body(jsonResponse)
                      .build();

    }
}
